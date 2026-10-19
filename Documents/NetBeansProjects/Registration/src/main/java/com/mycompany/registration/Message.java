package com.mycompany.registration;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String messageStatus;

    private static int totalMessagesSent = 0;

    public Message(int messageNumber, String recipient, String message) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.messageStatus = "Pending";
    }

    // Generate a random 10-digit message ID
    private String generateMessageID() {
        Random random = new Random();

        long number = 1_000_000_000L
                + (long) (random.nextDouble() * 9_000_000_000L);

        return String.valueOf(number);
    }

    // Check that the message ID contains no more than 10 characters
    public boolean checkMessageID() {
        return messageID != null
                && messageID.length() <= 10;
    }

    // Validate the recipient's international phone number
    public String checkRecipientCell() {

        if (recipient == null) {
            return "Cell phone number is incorrectly formatted "
                    + "or does not contain an international code. "
                    + "Please correct the number and try again.";
        }

        String number = recipient.trim();

        if (number.matches("^\\+[1-9]\\d{7,11}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted "
                + "or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // Check whether the recipient number is valid
    public boolean isRecipientValid() {
        return checkRecipientCell().equals(
                "Cell phone number successfully captured.");
    }

    // Validate the message length
    public String validateMessage() {

        if (message == null) {
            return "Please enter a message of less than 250 characters.";
        }

        if (message.length() > 250) {
            return "Please enter a message of less than 250 characters.";
        }

        return "Message ready to send.";
    }

    // Create the message hash
    public String createMessageHash() {

    String firstTwoDigits = messageID.substring(0, 2);

        String firstWord = "";
        String lastWord = "";

        if (message != null && !message.trim().isEmpty()) {

            String[] words = message.trim().split("\\s+");

            firstWord = words[0];
            lastWord = words[words.length - 1];
        }

        return (firstTwoDigits + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Allow the user to send, discard, or store a message
    public String SentMessage(int choice) {

        switch (choice) {

            case 1:
                if (!checkMessageID()) {
                    return "Invalid message ID.";
                }

                if (!isRecipientValid()) {
                    return checkRecipientCell();
                }

                if (message == null || message.length() > 250) {
                    return validateMessage();
                }

                messageStatus = "Sent";
                totalMessagesSent++;

                return "Message successfully sent.";

            case 2:
                messageStatus = "Discarded";

                return "Press 0 to delete the message.";

            case 3:
                messageStatus = "Stored";

                return "Message successfully stored.";

            default:
                return "Invalid option selected.";
        }
    }

    // Return the message details
    public String printMessages() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + message
                + "\nStatus: " + messageStatus;
    }

    // Return the total number of messages sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}