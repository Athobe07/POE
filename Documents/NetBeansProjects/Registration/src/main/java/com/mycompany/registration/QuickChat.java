package com.mycompany.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class QuickChat {

    private static final Scanner scanner = new Scanner(System.in);

    private static final Gson gson =
            new GsonBuilder().setPrettyPrinting().create();

    private static final String FILE_NAME = "messages.json";

    // Main list containing all processed messages
    private static List<Message> messages = new ArrayList<>();

    // REQUIRED PARALLEL ARRAYS

    private static Message[] sentMessages = new Message[0];

    private static Message[] disregardedMessages = new Message[0];

    private static Message[] storedMessages = new Message[0];

    private static String[] messageHashes = new String[0];

    private static String[] messageIDs = new String[0];

    // MAIN METHOD

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("       WELCOME TO QUICKCHAT");
        System.out.println("=================================");

        // Load messages saved previously
        loadStoredMessages();

        rebuildArrays();

        System.out.print("\nHow many messages would you like to enter? ");

        int numberOfMessages = readPositiveInteger();

        int messageNumber = 0;

        boolean running = true;

        while (running) {

            System.out.println("\n========== QUICKCHAT MENU ==========");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");

            System.out.print("Choose an option: ");

            int option = readInteger();

            switch (option) {

                case 1:

                    while (messageNumber < numberOfMessages) {

                        System.out.println("\nMessage "
                                + (messageNumber + 1)
                                + " of " + numberOfMessages);

                        System.out.print("Enter recipient number: ");

                        String recipient = scanner.nextLine().trim();

                        System.out.print("Enter your message: ");

                        String messageText = scanner.nextLine();

                        Message message = new Message(
                                messageNumber,
                                recipient,
                                messageText
                        );

                        // Validate message
                        System.out.println(message.validateMessage());

                        // Validate recipient
                        if (!message.isRecipientValid()) {

                            System.out.println(
                                    message.checkRecipientCell());

                            continue;
                        }

                        // Validate message length
                        if (messageText.length() > 250) {

                            System.out.println(
                                    "Message exceeds 250 characters.");

                            continue;
                        }

                        System.out.println("\nMessage ID: "
                                + message.getMessageID());

                        System.out.println("Message Hash: "
                                + message.getMessageHash());

                        System.out.println("\nChoose what to do:");

                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message to send later");

                        System.out.print("Your choice: ");

                        int choice = readInteger();

                        String result = message.SentMessage(choice);

                        System.out.println(result);

                        // Add processed messages
                        if (choice == 1
                                || choice == 2
                                || choice == 3) {

                            messages.add(message);

                            if (choice == 3) {

                                saveStoredMessages();
                            }

                            messageNumber++;

                            rebuildArrays();

                        } else {

                            System.out.println(
                                    "Invalid choice. Message not processed.");
                        }
                    }

                    break;

                case 2:

                    displaySentMessages();

                    break;

                case 3:

                    System.out.println("\nThank you for using QuickChat.");

                    System.out.println("Total messages successfully sent: "
                            + Message.returnTotalMessages());

                    running = false;

                    break;

                case 4:

                    storedMessagesMenu();

                    break;

                default:

                    System.out.println(
                            "Invalid option. Choose 1, 2, 3 or 4.");
            }
        }

        scanner.close();
    }

    // READ INTEGER
    private static int readInteger() {

        while (true) {

            String input = scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.print(
                        "Please enter a valid number: ");
            }
        }
    }

    // READ POSITIVE INTEGER
    private static int readPositiveInteger() {

        while (true) {

            int number = readInteger();

            if (number > 0) {

                return number;
            }

            System.out.print(
                    "Enter a number greater than zero: ");
        }
    }

    // POPULATE THE FIVE PARALLEL ARRAYS
    private static void rebuildArrays() {

        List<Message> sent = new ArrayList<>();

        List<Message> disregarded = new ArrayList<>();

        List<Message> stored = new ArrayList<>();

        List<String> hashes = new ArrayList<>();

        List<String> ids = new ArrayList<>();

        for (Message message : messages) {

            String status = message.getMessageStatus();

            if ("Sent".equalsIgnoreCase(status)) {

                sent.add(message);

            } else if ("Discarded".equalsIgnoreCase(status)
                    || "Disregard".equalsIgnoreCase(status)) {

                disregarded.add(message);

            } else if ("Stored".equalsIgnoreCase(status)) {

                stored.add(message);
            }

            hashes.add(message.getMessageHash());

            ids.add(message.getMessageID());
        }

        sentMessages = sent.toArray(new Message[0]);

        disregardedMessages =
                disregarded.toArray(new Message[0]);

        storedMessages = stored.toArray(new Message[0]);

        messageHashes = hashes.toArray(new String[0]);

        messageIDs = ids.toArray(new String[0]);
    }

    // SAVE STORED MESSAGES TO JSON
    private static void saveStoredMessages() {

        rebuildArrays();

        try (FileWriter writer = new FileWriter(FILE_NAME)) {

            gson.toJson(storedMessages, writer);

            System.out.println(
                    "Stored messages saved successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Error saving stored messages.");

            e.printStackTrace();
        }
    }

    // LOAD STORED MESSAGES FROM JSON INTO AN ARRAY
    private static void loadStoredMessages() {

        Path path = Path.of(FILE_NAME);

        if (!Files.exists(path)) {

            System.out.println(
                    "No previous stored messages found.");

            return;
        }

        try (FileReader reader = new FileReader(FILE_NAME)) {

            Type messageArrayType =
                    new TypeToken<Message[]>() {}.getType();

            Message[] loadedMessages =
                    gson.fromJson(reader, messageArrayType);

            if (loadedMessages != null) {

                for (Message message : loadedMessages) {

                    if (message != null
                            && "Stored".equalsIgnoreCase(
                                    message.getMessageStatus())) {

                        messages.add(message);
                    }
                }
            }

            System.out.println(
                    "Stored messages loaded successfully.");

        } catch (IOException | RuntimeException e) {

            System.out.println(
                    "Could not read the JSON file.");

            e.printStackTrace();
        }
    }

    // DISPLAY SENT MESSAGES
    private static void displaySentMessages() {

        rebuildArrays();

        if (sentMessages.length == 0) {

            System.out.println(
                    "No sent messages available.");

            return;
        }

        System.out.println("\n===== SENT MESSAGES =====");

        for (Message message : sentMessages) {

            System.out.println(message.printMessages());

            System.out.println("-------------------------");
        }
    }

    // STORED MESSAGES MENU
    private static void storedMessagesMenu() {

        boolean menuRunning = true;

        while (menuRunning) {

            rebuildArrays();

            System.out.println("\n===== STORED MESSAGES MENU =====");

            System.out.println("1. Display all stored messages");
            System.out.println("2. Display the longest message");
            System.out.println("3. Search for a message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete a message using its hash");
            System.out.println("6. Display full message report");
            System.out.println("7. Read messages from JSON");
            System.out.println("8. Return to main menu");

            System.out.print("Choose an option: ");

            int option = readInteger();

            switch (option) {

                case 1:

                    displayStoredMessages();

                    break;

                case 2:

                    displayLongestMessage();

                    break;

                case 3:

                    searchMessageID();

                    break;

                case 4:

                    searchRecipient();

                    break;

                case 5:

                    deleteMessageByHash();

                    break;

                case 6:

                    displayMessageReport();

                    break;

                case 7:

                    loadStoredMessages();

                    rebuildArrays();

                    displayStoredMessages();

                    break;

                case 8:

                    menuRunning = false;

                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1 to 8.");
            }
        }
    }

    // DISPLAY ALL STORED MESSAGES
    private static void displayStoredMessages() {

        rebuildArrays();

        if (storedMessages.length == 0) {

            System.out.println(
                    "No stored messages available.");

            return;
        }

        System.out.println("\n===== STORED MESSAGES =====");

        for (Message message : storedMessages) {

            System.out.println("Message ID: "
                    + message.getMessageID());

            System.out.println("Message Hash: "
                    + message.getMessageHash());

            System.out.println("Recipient: "
                    + message.getRecipient());

            System.out.println("Message: "
                    + message.getMessage());

            System.out.println("Status: "
                    + message.getMessageStatus());

            System.out.println("-------------------------");
        }
    }

    // DISPLAY THE LONGEST MESSAGE
    private static void displayLongestMessage() {

        rebuildArrays();

        if (messages.isEmpty()) {

            System.out.println(
                    "No messages available.");

            return;
        }

        Message longest = messages.get(0);

        for (Message message : messages) {

            if (message.getMessage().length()
                    > longest.getMessage().length()) {

                longest = message;
            }
        }

        System.out.println("\n===== LONGEST MESSAGE =====");

        System.out.println("Recipient: "
                + longest.getRecipient());

        System.out.println("Message: "
                + longest.getMessage());

        System.out.println("Status: "
                + longest.getMessageStatus());
    }

    // SEARCH FOR MESSAGE USING MESSAGE ID
    private static void searchMessageID() {

        System.out.print("Enter Message ID: ");

        String searchID = scanner.nextLine().trim();

        rebuildArrays();

        for (Message message : messages) {

            if (message.getMessageID().equals(searchID)) {

                System.out.println("\nMessage found.");

                System.out.println("Message ID: "
                        + message.getMessageID());

                System.out.println("Recipient: "
                        + message.getRecipient());

                System.out.println("Message: "
                        + message.getMessage());

                System.out.println("Status: "
                        + message.getMessageStatus());

                return;
            }
        }

        System.out.println("Message ID not found.");
    }

    // SEARCH SENT AND STORED MESSAGES BY RECIPIENT
    private static void searchRecipient() {

        System.out.print("Enter recipient number: ");

        String recipient = scanner.nextLine().trim();

        rebuildArrays();

        boolean found = false;

        for (Message message : messages) {

            String status = message.getMessageStatus();

            if (message.getRecipient().equals(recipient)
                    && ("Sent".equalsIgnoreCase(status)
                    || "Stored".equalsIgnoreCase(status))) {

                System.out.println("\nMessage: "
                        + message.getMessage());

                System.out.println("Status: "
                        + message.getMessageStatus());

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No sent or stored messages found for this recipient.");
        }
    }

    // DELETE MESSAGE USING MESSAGE HASH
    private static void deleteMessageByHash() {

        rebuildArrays();

        if (storedMessages.length == 0) {

            System.out.println(
                    "No stored messages available to delete.");

            return;
        }

        System.out.print("Enter Message Hash: ");

        String hash = scanner.nextLine().trim();

        boolean deleted = false;

        for (int i = 0; i < messages.size(); i++) {

            Message message = messages.get(i);

            if ("Stored".equalsIgnoreCase(
                    message.getMessageStatus())
                    && message.getMessageHash().equals(hash)) {

                System.out.println("Message: "
                        + message.getMessage());

                messages.remove(i);

                rebuildArrays();

                saveStoredMessages();

                System.out.println(
                        "Message successfully deleted.");

                deleted = true;

                break;
            }
        }

        if (!deleted) {

            System.out.println(
                    "Message hash not found.");
        }
    }

    // DISPLAY FULL MESSAGE REPORT
    private static void displayMessageReport() {

        rebuildArrays();

        if (messages.isEmpty()) {

            System.out.println(
                    "No messages available for the report.");

            return;
        }

        System.out.println("\n===== MESSAGE REPORT =====");

        for (Message message : messages) {

            System.out.println("Message ID: "
                    + message.getMessageID());

            System.out.println("Message Hash: "
                    + message.getMessageHash());

            System.out.println("Recipient: "
                    + message.getRecipient());

            System.out.println("Message: "
                    + message.getMessage());

            System.out.println("Status: "
                    + message.getMessageStatus());

            System.out.println("-------------------------");
        }

        System.out.println("Total messages: "
                + messages.size());

        System.out.println("Total sent messages: "
                + sentMessages.length);

        System.out.println("Total stored messages: "
                + storedMessages.length);

        System.out.println("Total disregarded messages: "
                + disregardedMessages.length);
    }
}