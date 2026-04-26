package com.polling.queue.management;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class PollingBoothManagementApp {

    static MongoCollection<Document> boothCollection;
    static MongoCollection<Document> userCollection;
    static MongoCollection<Document> queueCollection;

    public static void main(String[] args) {

        // Connect to MongoDB
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("queue_management");

        boothCollection = db.getCollection("polling_booth");
        userCollection  = db.getCollection("users");
        queueCollection = db.getCollection("queue_record");

        System.out.println("✅ Connected to MongoDB successfully!");
        System.out.println("📂 Database: queue_management");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== SMART QUEUE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Polling Booth");
            System.out.println("2. View All Booths");
            System.out.println("3. Update Queue Length");
            System.out.println("4. Delete Booth");
            System.out.println("5. View All Voters");
            System.out.println("6. View Queue Records");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    insertBooth(sc);
                    break;
                case 2:
                    viewBooths();
                    break;
                case 3:
                    updateQueue(sc);
                    break;
                case 4:
                    deleteBooth(sc);
                    break;
                case 5:
                    viewVoters();
                    break;
                case 6:
                    viewQueueRecords();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 7);

        client.close();
        System.out.println("🔌 MongoDB connection closed.");
    }

    // ── CREATE ────────────────────────────────
    public static void insertBooth(Scanner sc) {
        System.out.print("Enter Booth Name: ");
        String boothName = sc.nextLine();

        System.out.print("Enter Booth Number: ");
        String boothNumber = sc.nextLine();

        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        System.out.print("Enter District: ");
        String district = sc.nextLine();

        System.out.print("Enter Constituency: ");
        String constituency = sc.nextLine();

        System.out.print("Enter Capacity: ");
        int capacity = sc.nextInt();
        sc.nextLine();

        Document doc = new Document("boothName", boothName)
                .append("boothNumber", boothNumber)
                .append("address", address)
                .append("district", district)
                .append("constituency", constituency)
                .append("capacity", capacity)
                .append("currentQueueLength", 0)
                .append("crowdLevel", "LOW")
                .append("isActive", true)
                .append("estimatedWaitMinutes", 0)
                .append("totalUpdates", 0)
                .append("averageQueueSize", 0.0)
                .append("peakQueueLevel", 0);

        boothCollection.insertOne(doc);
        System.out.println("✅ Booth Inserted Successfully!");
    }

    // ── READ ──────────────────────────────────
    public static void viewBooths() {
        System.out.println("\n--- Polling Booth List ---");

        if (boothCollection.countDocuments() == 0) {
            System.out.println("No booths found!");
            return;
        }

        for (Document d : boothCollection.find()) {
            System.out.println("==============================");
            System.out.println("Booth Name   : " + d.getString("boothName"));
            System.out.println("Booth Number : " + d.getString("boothNumber"));
            System.out.println("Address      : " + d.getString("address"));
            System.out.println("District     : " + d.getString("district"));
            System.out.println("Constituency : " + d.getString("constituency"));
            System.out.println("Capacity     : " + d.getInteger("capacity"));
            System.out.println("Queue Length : " + d.getInteger("currentQueueLength"));
            System.out.println("Crowd Level  : " + d.getString("crowdLevel"));
            System.out.println("Active       : " + d.getBoolean("isActive"));
        }
        System.out.println("==============================");
    }

    // ── UPDATE ────────────────────────────────
    public static void updateQueue(Scanner sc) {
        System.out.print("Enter Booth Number to Update: ");
        String boothNumber = sc.nextLine();

        System.out.print("Enter New Queue Length: ");
        int newQueue = sc.nextInt();
        sc.nextLine();

        // Calculate crowd level
        String crowdLevel;
        if (newQueue < 15) crowdLevel = "LOW";
        else if (newQueue < 30) crowdLevel = "MEDIUM";
        else crowdLevel = "HIGH";

        boothCollection.updateOne(
                new Document("boothNumber", boothNumber),
                new Document("$set", new Document("currentQueueLength", newQueue)
                        .append("crowdLevel", crowdLevel)
                        .append("estimatedWaitMinutes", newQueue * 2))
        );

        System.out.println("✅ Queue Updated Successfully!");
        System.out.println("New Queue Length : " + newQueue);
        System.out.println("Crowd Level      : " + crowdLevel);
        System.out.println("Estimated Wait   : " + (newQueue * 2) + " minutes");
    }

    // ── DELETE ────────────────────────────────
    public static void deleteBooth(Scanner sc) {
        System.out.print("Enter Booth Number to Delete: ");
        String boothNumber = sc.nextLine();

        boothCollection.deleteOne(new Document("boothNumber", boothNumber));
        System.out.println("✅ Booth Deleted Successfully!");
    }

    // ── VIEW VOTERS ───────────────────────────
    public static void viewVoters() {
        System.out.println("\n--- Voter List ---");

        if (userCollection.countDocuments() == 0) {
            System.out.println("No voters found!");
            return;
        }

        for (Document d : userCollection.find()) {
            System.out.println("==============================");
            System.out.println("Full Name : " + d.getString("fullName"));
            System.out.println("Email     : " + d.getString("email"));
            System.out.println("Voter ID  : " + d.getString("voterId"));
            System.out.println("Role      : " + d.getString("role"));
        }
        System.out.println("==============================");
    }

    // ── VIEW QUEUE RECORDS ────────────────────
    public static void viewQueueRecords() {
        System.out.println("\n--- Queue Records ---");

        if (queueCollection.countDocuments() == 0) {
            System.out.println("No queue records found!");
            return;
        }

        for (Document d : queueCollection.find()) {
            System.out.println("==============================");
            System.out.println("Booth Number  : " + d.getString("boothNumber"));
            System.out.println("Queue Length  : " + d.getInteger("queueLength"));
            System.out.println("Crowd Level   : " + d.getString("crowdLevel"));
            System.out.println("Timestamp     : " + d.get("timestamp"));
        }
        System.out.println("==============================");
    }
}
