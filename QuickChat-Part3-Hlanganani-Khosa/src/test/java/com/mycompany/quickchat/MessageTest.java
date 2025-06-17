package com.mycompany.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test class for the Message functionality in QuickChat application.
 * Tests message validation, recipient formatting, message hash generation,
 * and message handling actions.
 */
public class MessageTest {

    /**
     * Tests successful message creation with valid length.
     * Expected: Message should be stored and return "Message ready to send."
     */
    @Test
    public void testMessageLengthSuccess() {
        String validMessage = "Hello, this is a test message.";
        Message message = new Message("+27718693002", validMessage);
        assertNotNull(message.getMessage());
        assertEquals(validMessage, message.getMessage());
    }

    /**
     * Tests message creation with length exceeding 250 characters.
     * Expected: Message should be null and system should indicate excess characters.
     */
    @Test
    public void testMessageLengthFailure() {
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            longMsg.append("a");
        }
        Message message = new Message("+27718693002", longMsg.toString());
        assertNull(message.getMessage());
        int excessChars = longMsg.length() - 250;
        assertTrue(excessChars > 0);
    }

    /**
     * Tests recipient number with correct international format.
     * Expected: Should return "Cell phone number successfully captured."
     */
    @Test
    public void testRecipientNumberSuccess() {
        Message message = new Message("+27718693002", "Test message");
        assertTrue(message.checkRecipientCell());
    }

    /**
     * Tests recipient number with incorrect format (missing + symbol).
     * Expected: Should return error message about incorrect format.
     */
    @Test
    public void testRecipientNumberFailure() {
        Message message = new Message("27718693002", "Test message");
        assertFalse(message.checkRecipientCell());
    }

    /**
     * Tests message hash generation format.
     * Expected: Hash should be in format "00:0:HITONIGHT" for test case 1.
     */
    @Test
    public void testMessageHashFormat() {
        Message message = new Message("+27718693002", "Hi tonight");
        String hash = message.getMessageHash();
        assertTrue(hash.startsWith(message.getMessageID().substring(0, 2)));
        assertTrue(hash.contains(":HITONIGHT"));
    }

    /**
     * Tests message ID generation.
     * Expected: Should return "Message ID generated: <Message ID>"
     */
    @Test
    public void testMessageIDGeneration() {
        Message message = new Message("+27718693002", "Test message");
        String messageID = message.getMessageID();
        assertNotNull(messageID);
        assertEquals(10, messageID.length());
        assertTrue(messageID.matches("\\d{10}"));
    }

    /**
     * Tests message send action.
     * Expected: Should return "Message successfully sent."
     * Note: Requires manual interaction with dialog.
     */
    @Test
    public void testMessageSendAction() {
        Message message = new Message("+27718693002", "Test message");
        String result = message.SentMessage();
        assertNotNull(result);
        assertTrue(result.equals("Send") || result.equals("Store") || result.equals("Disregard"));
    }

    /**
     * Tests message storage functionality.
     * Expected: Message should be stored in messages.json file.
     */
    @Test
    public void testMessageStoreAction() {
        Message message = new Message("+27718693002", "Test store message");
        message.storeMessage();
        
        File messagesFile = new File("messages.json");
        assertTrue(messagesFile.exists());
        
        try {
            String content = Files.readString(Paths.get("messages.json"));
            assertTrue(content.contains("Test store message"));
            assertTrue(content.contains("+27718693002"));
        } catch (Exception e) {
            fail("Failed to read messages.json: " + e.getMessage());
        }
    }

    /**
     * Tests message hash generation for multiple messages in a loop.
     * Expected: Each hash should be properly formatted and uppercase.
     */
    @Test
    public void testMessageHashLoop() {
        String[] testMessages = {
            "First test message",
            "Second test message",
            "Third test message"
        };

        for (String msg : testMessages) {
            Message message = new Message("+27718693002", msg);
            String hash = message.getMessageHash();
            assertNotNull(hash);
            assertTrue(hash.contains(":"));
            assertTrue(hash.equals(hash.toUpperCase()));
        }
    }

    /**
     * Tests the total message count functionality.
     * Expected: Should return correct count of messages created.
     */
    @Test
    public void testTotalMessagesCount() {
        int initialCount = Message.returnTotalMessages();
        Message message = new Message("+27718693002", "Test message 1");
        Message message1 = new Message("+27718693002", "Test message 2");
        assertEquals(initialCount + 2, Message.returnTotalMessages());
    }
}