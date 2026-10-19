package com.mycompany.registration;

import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    // Test 1: Message ID must not exceed 10 characters
    @Test
    public void testCheckMessageID() {

        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    // Test 2: Correct recipient number
    @Test
    public void testValidRecipient() {

        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    // Test 3: Incorrect recipient number
    @Test
    public void testInvalidRecipient() {

        Message message = new Message(
                1,
                "0718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted "
                + "or does not contain an international code. "
                + "Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    // Test 4: Message length is valid
    @Test
    public void testValidMessageLength() {

        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message ready to send.",
                message.validateMessage()
        );
    }

    // Test 5: Message exceeds 250 characters
    @Test
    public void testMessageTooLong() {

        String longMessage = "A".repeat(251);

        Message message = new Message(
                1,
                "+27718693002",
                longMessage
        );

        assertEquals(
                "Please enter a message of less than 250 characters.",
                message.validateMessage()
        );
    }

    // Test 6: Message hash is generated correctly
    @Test
    public void testCreateMessageHash() {

        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        String expectedHash =
                message.getMessageID().substring(0, 2)
                + ":0:HITONIGHT";

        assertEquals(
                expectedHash,
                message.createMessageHash()
        );
    }

    // Test 7: Send a valid message
    @Test
    public void testSendMessage() {

        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );

        assertEquals("Sent", message.getMessageStatus());
    }

    // Test 8: Disregard a message
    @Test
    public void testDisregardMessage() {

        Message message = new Message(
                2,
                "08575975889",
                "Hi Keegan, did you receive the payment?"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );

        assertEquals("Discarded", message.getMessageStatus());
    }

    // Test 9: Store a message
    @Test
    public void testStoreMessage() {

        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3)
        );

        assertEquals("Stored", message.getMessageStatus());
    }
    
    // Test 10: Invalid menu option
    @Test
    public void testInvalidOption() {

        Message message = new Message(
                1,
                "+27718693002",
                "Hello"
        );
        assertEquals(
                "Invalid option selected.",
                message.SentMessage(4)
        );
    }
    
    // Test 11: Message details are displayed
    @Test
    public void testPrintMessages() {

        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );
        String details = message.printMessages();

        assertTrue(details.contains("Message ID:"));
        assertTrue(details.contains("Message Hash:"));
        assertTrue(details.contains("Recipient: +27718693002"));
        assertTrue(details.contains("Message: Hi Mike"));
        assertTrue(details.contains("Status: Pending"));
    }
}
