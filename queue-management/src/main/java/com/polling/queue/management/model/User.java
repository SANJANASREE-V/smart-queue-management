package com.polling.queue.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String fullName;
    private String voterId;
    private String role; // "VOTER" or "ADMIN"
    private String assignedBoothId;
    private boolean alertEnabled;
    private int alertThreshold; // alert when queue drops below this
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User(String email, String password, String fullName,
                String voterId, String role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.voterId = voterId;
        this.role = role;
        this.alertEnabled = false;
        this.alertThreshold = 10;
        this.createdAt = LocalDateTime.now();
    }

    public void setUsername(Object username) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setUsername'");
    }
}

