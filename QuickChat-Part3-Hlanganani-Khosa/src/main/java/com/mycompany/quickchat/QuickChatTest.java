  package com.mycompany.quickchat;

import com.mycompany.quickchat.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuickChatTest {
    private Message message1; // Sent
    private Message message2; // Stored
    private Message message3; // Disregarded
    private Message message4; // Sent
    private Message message5; // Stored

    @BeforeEach
    void setUp() {
        // Initialize test messages with exact test data
        message1 = new Message("+27834557896", "Did you get the cake?");
        message2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message("0838884567", "It is dinner time!");
        message5 = new Message("+27838884567", "Ok, I am leaving without you.");

        // Set the appropriate flags for each message
        message1.SentMessage(); // Set to Sent
        message2.SentMessage(); // Set to Stored
        message3.SentMessage(); // Set to Disregard
        message4.SentMessage(); // Set to Sent
        message5.SentMessage(); // Set to Stored
    }

    @Test
    void testSentMessagesArray() {
        // Test that sent messages array contains the expected messages
        String sentMessages = Message.printMessages();
        assertTrue(sentMessages.contains("Did you get the cake?"));
        assertTrue(sentMessages.contains("It is dinner time!"));
    }

    @Test
    void testStoredMessages() {
        // Test that stored messages array contains the expected messages
        String storedMessages = Message.displayStoredMessages();
        assertTrue(storedMessages.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(storedMessages.contains("Ok, I am leaving without you."));
    }

    @Test
    void testDisregardedMessages() {
        // Test that disregarded messages array contains the expected messages
        String disregardedMessages = Message.displayDisregardedMessages();
        assertTrue(disregardedMessages.contains("Yohoooo, I am at your gate."));
    }

    @Test
    void testLongestMessage() {
        // Test finding the longest message
        String longestMessage = Message.findLongestMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longestMessage);
    }

    @Test
    void testSearchByMessageID() {
        // Test searching for a specific message by ID
        String messageID = message4.getMessageID();
        String foundMessage = Message.searchByMessageID(messageID);
        assertTrue(foundMessage.contains("It is dinner time!"));
    }

    @Test
    void testSearchByRecipient() {
        // Test searching for all messages sent to a specific recipient
        String recipientMessages = Message.searchByRecipient("+27838884567");
        assertTrue(recipientMessages.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(recipientMessages.contains("Ok, I am leaving without you."));
    }

    @Test
    void testDeleteByHash() {
        // Test deleting a message using its hash
        String messageHash = message2.getMessageHash();
        String deleteResult = Message.deleteByHash(messageHash);
        assertEquals("Message successfully deleted.", deleteResult);
        
        // Verify the message is no longer in the sent messages
        String sentMessages = Message.printMessages();
        assertFalse(sentMessages.contains("Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    void testDisplayReport() {
        // Test the display report functionality
        String report = Message.displayReport();
        
        // Verify the report contains all required information
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
        
        // Verify specific message content
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));
    }

    @Test
    void testMessageFlags() {
        // Test that messages are correctly categorized based on their flags
        String sentMessages = Message.printMessages();
        String storedMessages = Message.displayStoredMessages();
        String disregardedMessages = Message.displayDisregardedMessages();

        // Verify sent messages
        assertTrue(sentMessages.contains("Did you get the cake?"));
        assertTrue(sentMessages.contains("It is dinner time!"));

        // Verify stored messages
        assertTrue(storedMessages.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(storedMessages.contains("Ok, I am leaving without you."));

        // Verify disregarded messages
        assertTrue(disregardedMessages.contains("Yohoooo, I am at your gate."));
    }
}