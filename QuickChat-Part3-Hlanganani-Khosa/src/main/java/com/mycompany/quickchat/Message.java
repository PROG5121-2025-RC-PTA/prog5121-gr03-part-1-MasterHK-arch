package com.mycompany.quickchat;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Message class handles the creation, validation, and management of chat messages.
 * It includes functionality for message ID generation, recipient validation,
 * message hashing, and storage.
 */
public class Message {
    private final String messageID;        // Unique identifier for each message
    private final int messageNumber;       // Sequential number for message ordering
    private final String recipient;        // Recipient's phone number
    private final String message;          // Message content
    private final String messageHash;      // Hash value for message verification
    private static int totalMessages = 0;  // Counter for total messages created
    private static final List<Message> sentMessages = new ArrayList<>();  // Storage for sent messages
    private static final List<Message> disregardedMessages = new ArrayList<>(); // Storage for disregarded messages
    private static final List<Message> storedMessages = new ArrayList<>();    // Storage for stored messages
    private static final List<String> messageHashes = new ArrayList<>();      // Storage for message hashes
    private static final List<String> messageIDs = new ArrayList<>();         // Storage for message IDs

    /**
     * Creates a new message with recipient and content.
     * Validates message length and recipient format.
     * @param recipient The recipient's phone number (must start with +)
     * @param message The message content (max 250 characters)
     */
    public Message(String recipient, String message) {
        this.messageID = generateMessageID();
        this.messageNumber = ++totalMessages;
        this.recipient = recipient;
        this.message = (message != null && message.length() > 250) ? null : message;
        this.messageHash = createMessageHash();
        if (checkMessageID() && checkRecipientCell() && (this.message == null || this.message.length() <= 250)) {
            messageIDs.add(this.messageID);
            messageHashes.add(this.messageHash);
        } else {
            totalMessages--;
        }
    }

    /**
     * Generates a random 10-digit message ID.
     * @return A formatted string containing the message ID
     */
    private String generateMessageID() {
        Random random = new Random();
        long number = Math.abs(random.nextLong() % 10000000000L);
        return String.format("%010d", number);
    }

    /**
     * Validates the message ID format.
     * @return true if ID is valid (10 digits), false otherwise
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    /**
     * Validates the recipient's phone number format.
     * Must start with + and be no longer than 14 characters.
     * @return true if number is valid, false otherwise
     */
    public boolean checkRecipientCell() {
        return recipient != null && recipient.startsWith("+") && recipient.length() <= 14;
    }

    /**
     * Creates a hash from message ID, number and content.
     * Format: ID.substring(0,2):messageNumber:firstWord+lastWord
     * @return The uppercase hash string
     */
    public String createMessageHash() {
        if (message == null || message.trim().isEmpty()) {
            return messageID.substring(0, 2) + ":" + messageNumber + ":NOMESSAGE";
        }
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * Displays a dialog for message action selection.
     * @return Selected action: "Send", "Store", or "Disregard"
     */
    public String SentMessage() {
        String[] options = {"Send", "Store", "Disregard"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an action for the message:", "Message Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String action = switch (choice) {
            case 0 -> "Send";
            case 1 -> "Store";
            case 2 -> "Disregard";
            default -> "Disregard";
        };
        
        // Add message to appropriate array based on action
        switch (action) {
            case "Send" -> sentMessages.add(this);
            case "Store" -> storedMessages.add(this);
            case "Disregard" -> disregardedMessages.add(this);
        }
        
        return action;
    }

    /**
     * Returns a formatted string of all sent messages.
     * @return String containing all message details
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message msg : sentMessages) {
            sb.append("ID: ").append(msg.messageID)
              .append(", Hash: ").append(msg.messageHash)
              .append(", Recipient: ").append(msg.recipient)
              .append(", Message: ").append(msg.message)
              .append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the total number of messages created.
     * @return Total message count
     */
    public static int returnTotalMessages() {
        return totalMessages;
    }

    /**
     * Stores the message in JSON format to messages.json file.
     * Appends new messages to existing file.
     */
    public void storeMessage() {
        JSONObject json = new JSONObject();
        json.put("messageID", messageID);
        json.put("messageNumber", messageNumber);
        json.put("recipient", recipient);
        json.put("message", message);
        json.put("messageHash", messageHash);
        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json.toString() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
        }
    }


    /**
 * Displays all stored messages.
 * @return String containing all stored message details
 */
public static String displayStoredMessages() {
    if (storedMessages.isEmpty()) {
        return "No stored messages found.";
    }
    StringBuilder sb = new StringBuilder("Stored Messages:\n\n");
    for (Message msg : storedMessages) {
        sb.append("ID: ").append(msg.messageID)
          .append("\nHash: ").append(msg.messageHash)
          .append("\nRecipient: ").append(msg.recipient)
          .append("\nMessage: ").append(msg.message)
          .append("\n\n");
    }
    return sb.toString();
}


    /**
     * Displays sender and recipient of all sent messages.
     */
    public static String displaySenderRecipients() {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message msg : sentMessages) {
            sb.append("Recipient: ").append(msg.recipient)
              .append(", Message: ").append(msg.message)
              .append("\n");
        }
        return sb.toString();
    }

    /**
     * Finds and returns the longest sent message.
     */
    public static String findLongestMessage() {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        return sentMessages.stream()
            .max((m1, m2) -> Integer.compare(m1.message.length(), m2.message.length()))
            .map(m -> m.message)
            .orElse("No messages found");
    }

    /**
     * Searches for a message by its ID.
     */
    public static String searchByMessageID(String messageID) {
        return sentMessages.stream()
            .filter(m -> m.messageID.equals(messageID))
            .findFirst()
            .map(m -> String.format("Recipient: %s\nMessage: %s", m.recipient, m.message))
            .orElse("Message not found");
    }

    /**
     * Searches for all messages sent to a particular recipient.
     */
    public static String searchByRecipient(String recipient) {
        List<Message> recipientMessages = sentMessages.stream()
            .filter(m -> m.recipient.equals(recipient))
            .collect(Collectors.toList());
        
        if (recipientMessages.isEmpty()) {
            return "No messages found for recipient: " + recipient;
        }
        
        StringBuilder sb = new StringBuilder();
        for (Message msg : recipientMessages) {
            sb.append("Message: ").append(msg.message).append("\n");
        }
        return sb.toString();
    }

    /**
     * Deletes a message using its hash.
     */
    public static String deleteByHash(String hash) {
        boolean removed = sentMessages.removeIf(m -> m.messageHash.equals(hash));
        if (removed) {
            messageHashes.remove(hash);
            return "Message successfully deleted.";
        }
        return "Message not found.";
    }

     /**
     * Displays all disregarded messages.
     * @return String containing all disregarded message details
     */
    public static String displayDisregardedMessages() {
        if (disregardedMessages.isEmpty()) {
            return "No disregarded messages found.";
        }
        StringBuilder sb = new StringBuilder("Disregarded Messages:\n\n");
        for (Message msg : disregardedMessages) {
            sb.append("ID: ").append(msg.messageID)
              .append("\nHash: ").append(msg.messageHash)
              .append("\nRecipient: ").append(msg.recipient)
              .append("\nMessage: ").append(msg.message)
              .append("\n\n");
        }
        return sb.toString();
    }

    /**
     * Displays a report of all sent messages.
     */
    public static String displayReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to report.";
        }
        
        StringBuilder sb = new StringBuilder("Message Report:\n\n");
        for (Message msg : sentMessages) {
            sb.append("Message Hash: ").append(msg.messageHash)
              .append("\nRecipient: ").append(msg.recipient)
              .append("\nMessage: ").append(msg.message)
              .append("\n\n");
        }
        return sb.toString();
    }

    // Getter methods for message properties
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
}