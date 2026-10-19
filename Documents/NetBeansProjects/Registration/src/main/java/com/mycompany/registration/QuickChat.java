package com.mycompany.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class QuickChat {

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Message> messages = new ArrayList<>();

    public static void main(String[] args) {
 
        System.out.println("       WELCOME TO QUICKCHAT");
        
        System.out.println("Welcome to QuickChat.");

        System.out.print("How many messages would you like to enter? ");

        int numberOfMessages = readPositiveInteger();

        int messageNumber = 0;

        boolean running = true;

        while (running) {

            System.out.println("    QUICKCHAT MENU ");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
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

                        System.out.println("\n"
                                + message.validateMessage());

                        if (!message.isRecipientValid()) {
                            System.out.println(
                                    message.checkRecipientCell()
                            );
                            continue;
                        }

                        if (messageText.length() > 250) {
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

                        if (choice == 1
                                && message.getMessageStatus().equals("Sent")) {

                            messages.add(message);

                            System.out.println("\nMessage details:");
                            System.out.println(message.printMessages());

                            messageNumber++;

                        } else if (choice == 2
                                || choice == 3) {

                            messages.add(message);

                            if (choice == 3) {
                                storeMessage(message);
                            }

                            messageNumber++;

                        } else {
                            System.out.println(
                                    "The message was not processed. "
                                    + "Please try again."
                            );
                        }

                    }

                    if (messageNumber >= numberOfMessages) {
                        System.out.println(
                                "\nYou have entered all "
                                + numberOfMessages
                                + " messages."
                        );
                    }

                    break;

                case 2:

                    System.out.println("Coming Soon.");

                    break;

                case 3:

                    System.out.println("\nThank you for using QuickChat.");

                    System.out.println("Total messages successfully sent: "
                            + Message.returnTotalMessages());

                    running = false;

                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1, 2 or 3."
                    );
            }
        }

        scanner.close();
    }

    // Read an integer 
    private static int readInteger() {

        while (true) {

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    // Read a positive integer
    private static int readPositiveInteger() {

        while (true) {

            int number = readInteger();

            if (number > 0) {
                return number;
            }

            System.out.print(
                    "Please enter a number greater than zero: "
            );
        }
    }

    // Store message details in a JSON file
    private static void storeMessage(Message message) {

        String json = "{\n"
                + "  \"messageID\": \""
                + escapeJson(message.getMessageID()) + "\",\n"
                + "  \"messageNumber\": "
                + message.getMessageNumber() + ",\n"
                + "  \"recipient\": \""
                + escapeJson(message.getRecipient()) + "\",\n"
                + "  \"message\": \""
                + escapeJson(message.getMessage()) + "\",\n"
                + "  \"messageHash\": \""
                + escapeJson(message.getMessageHash()) + "\",\n"
                + "  \"status\": \""
                + escapeJson(message.getMessageStatus()) + "\"\n"
                + "}\n";

        try (FileWriter writer =
                     new FileWriter("messages.json", true)) {

            writer.write(json);
            writer.write(",\n");

            System.out.println("Message successfully stored.");

        } catch (IOException e) {

            System.out.println(
                    "An error occurred while storing the message."
            );

            e.printStackTrace();
        }
    }

    // Escape special characters so message text can be stored safely
    private static String escapeJson(String text) {

        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
} 
