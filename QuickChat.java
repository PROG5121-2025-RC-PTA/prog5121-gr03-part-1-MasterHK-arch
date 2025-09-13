
package com.mycompany.quickchat;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Message class handles the creation, validation, and management of chat messages.
 * It includes functionality for message ID generation, recipient validation,
 * message hashing, and storage.
 */
final class Message {
    private final String messageID;        // Unique identifier for each message
    private final int messageNumber;       // Sequential number for message ordering
    private final String recipient;        // Recipient's phone number
    private final String message;          // Message content
    private final String messageHash;      // Hash value for message verification
    private static int totalMessages = 0;  // Counter for total messages created
    private static final List<Message> sentMessages = new ArrayList<>();  // Storage for sent messages

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
            sentMessages.add(this);
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
        return switch (choice) {
            case 0 -> "Send";
            case 1 -> "Store";
            case 2 -> "Disregard";
            default -> "Disregard";
        };
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

    // Getter methods for message properties
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
}

/**
 * Main QuickChat application class.
 * Handles user login, message creation, and menu interface.
 */
public class QuickChat {
    /**
     * Main entry point for the application.
     * Manages login and message limit setup.
     */
    public static void main(String[] args) {
        if (!login()) {
            System.exit(0);
        }
        
        String numMessagesStr = JOptionPane.showInputDialog("How many messages to send?");
        int maxMessages;
        try {
            maxMessages = Integer.parseInt(numMessagesStr);
            if (maxMessages <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number. Exiting.");
            System.exit(0);
            return;
        }
        
        showMenu(maxMessages);
    }

    /**
     * Handles user login process.
     * @return true if login successful, false otherwise
     */
    private static boolean login() {
        String username = JOptionPane.showInputDialog("Enter username:");
        if (username != null && !username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Login successful");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Login failed. Exiting.");
            return false;
        }
    }

    /**
     * Displays and handles the main menu interface.
     * @param maxMessages Maximum number of messages allowed
     */
    private static void showMenu(int maxMessages) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat");
        String choice;
        do {
            choice = JOptionPane.showInputDialog("Menu:\n1. Send Messages\n2. Show Recently Sent Messages\n3. Quit");
            if (choice == null) choice = "3";
            switch (choice) {
                case "1" -> {
                    if (Message.returnTotalMessages() < maxMessages) {
                        createMessage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Message limit reached.");
                    }
                }
                case "2" -> JOptionPane.showMessageDialog(null, "Coming Soon.");
                case "3" -> JOptionPane.showMessageDialog(null, "Total messages sent: " + Message.returnTotalMessages());
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
            }
        } while (!choice.equals("3"));
    }

    /**
     * Handles the creation and processing of new messages.
     * Validates input and manages message actions.
     */
    private static void createMessage() {
        String recipient = JOptionPane.showInputDialog("Enter recipient cell number (e.g., +123456789):");
        String message = JOptionPane.showInputDialog("Enter message (max 250 characters):");
        
        if (message != null && message.length() > 250) {
            JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
            return;
        }
        
        Message msg = new Message(recipient, message);
        if (!msg.checkMessageID() || !msg.checkRecipientCell()) {
            JOptionPane.showMessageDialog(null, "Invalid message ID or recipient. Disregarding.");
            return;
        }
        
        String action = msg.SentMessage();
        switch (action) {
            case "Send" -> {
                msg.storeMessage();
                JOptionPane.showMessageDialog(null, "Message sent\nID: " + msg.getMessageID() +
                        "\nHash: " + msg.getMessageHash() + "\nRecipient: " + msg.getRecipient() +
                        "\nMessage: " + msg.getMessage());
            }
            case "Store" -> {
                msg.storeMessage();
                JOptionPane.showMessageDialog(null, "Message stored.");
            }
            default -> JOptionPane.showMessageDialog(null, "Message disregarded.");
        }
    }
}