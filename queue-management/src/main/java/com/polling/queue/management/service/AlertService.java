package com.polling.queue.management.service;

import com.polling.queue.management.dto.AlertSettingsRequest;
import com.polling.queue.management.model.Alert;
import com.polling.queue.management.model.PollingBooth;
import com.polling.queue.management.model.User;
import com.polling.queue.management.repository.AlertRepository;
import com.polling.queue.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void triggerQueueAlerts(String boothId, int currentQueue, PollingBooth booth) {
        List<User> subscribedUsers = userRepository.findAll().stream()
            .filter(user -> boothId.equals(user.getAssignedBoothId()))
            .filter(User::isAlertEnabled)
            .filter(user -> currentQueue <= user.getAlertThreshold())
            .toList();

        for (User user : subscribedUsers) {
            String message = String.format(
                "Queue at %s has reduced to %d people. Estimated wait: %d minutes. Good time to vote!",
                booth.getBoothName(), currentQueue, booth.getEstimatedWaitMinutes()
            );
            Alert alert = new Alert(user.getId(), boothId, message, currentQueue);
            Alert saved = alertRepository.save(alert);

            // Push real-time alert via WebSocket
            messagingTemplate.convertAndSendToUser(
                user.getId(), "/queue/alerts", saved);
        }
    }

    public List<Alert> getUserAlerts(String userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long getUnreadAlertCount(String userId) {
        return alertRepository.countByUserIdAndIsReadFalse(userId);
    }

    public void markAllRead(String userId) {
        List<Alert> unread = alertRepository.findByUserIdAndIsReadFalse(userId);
        unread.forEach(alert -> alert.setRead(true));
        alertRepository.saveAll(unread);
    }

    public void updateAlertSettings(String userId, AlertSettingsRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAlertEnabled(request.isAlertEnabled());
        user.setAlertThreshold(request.getAlertThreshold());
        userRepository.save(user);
    }

    public Alert saveAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public List<Alert> getAlertsByUserId(String userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void deleteAlert(String id) {
        alertRepository.deleteById(id);
    }
}

