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
            choice = JOptionPane.showInputDialog(
                "Menu:\n" +
                "1. Send Messages\n" +
                "2. Show Recently Sent Messages\n" +
                "3. Display Sender and Recipients\n" +
                "4. Find Longest Message\n" +
                "5. Search by Message ID\n" +
                "6. Search by Recipient\n" +
                "7. Delete Message by Hash\n" +
                "8. Display Full Report\n" +
                "9. Display Disregarded Messages\n" +
                "10. Display Stored Messages\n" +
                "11. Quit"
            );
            if (choice == null) choice = "9";
            switch (choice) {
                case "1" -> {
                    if (Message.returnTotalMessages() < maxMessages) {
                        createMessage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Message limit reached.");
                    }
                }
                case "2" -> JOptionPane.showMessageDialog(null, Message.printMessages());
                case "3" -> JOptionPane.showMessageDialog(null, Message.displaySenderRecipients());
                case "4" -> JOptionPane.showMessageDialog(null, "Longest Message: " + Message.findLongestMessage());
                case "5" -> {
                    String messageID = JOptionPane.showInputDialog("Enter Message ID to search:");
                    if (messageID != null) {
                        JOptionPane.showMessageDialog(null, Message.searchByMessageID(messageID));
                    }
                }
                case "6" -> {
                    String recipient = JOptionPane.showInputDialog("Enter recipient number to search:");
                    if (recipient != null) {
                        JOptionPane.showMessageDialog(null, Message.searchByRecipient(recipient));
                    }
                }
                case "7" -> {
                    String hash = JOptionPane.showInputDialog("Enter message hash to delete:");
                    if (hash != null) {
                        JOptionPane.showMessageDialog(null, Message.deleteByHash(hash));
                    }
                }
                case "8" -> JOptionPane.showMessageDialog(null, Message.displayReport());
                case "9" -> JOptionPane.showMessageDialog(null, Message.displayDisregardedMessages());
                case "10" -> JOptionPane.showMessageDialog(null, Message.displayStoredMessages());
                case "11" -> JOptionPane.showMessageDialog(null, "Total messages sent: " + Message.returnTotalMessages());
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");

            }
        } while (!choice.equals("11"));
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