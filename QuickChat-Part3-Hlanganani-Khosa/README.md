![image](https://github.com/user-attachments/assets/be20b69c-325d-4116-acb7-40712a7c6cec)

# QuickChat Application

A Java-based messaging application that allows users to send, store, and manage messages with various features for message tracking and management.

## Features

- **Message Management**
  - Send messages to recipients
  - Store messages for later use
  - Disregard unwanted messages
  - Track message status (Sent, Stored, Disregarded)

- **Message Tracking**
  - Unique message IDs
  - Message hashing for verification
  - Message status tracking
  - Message history

- **Search and Display**
  - Search messages by ID
  - Search messages by recipient
  - Find longest message
  - Display sender and recipient information
  - Generate comprehensive message reports

- **Message Validation**
  - Recipient number validation
  - Message length validation (max 250 characters)
  - Message ID format validation

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven for build management
- JUnit 5 for testing

## Installation

1. Clone the repository:
```bash
git clone https://github.com/MasterHK-arch/QuickChat_v3.git
```

2. Navigate to the project directory:
```bash
cd QuickChat
```

3. Build the project using Maven:
```bash
mvn clean install
```

## Usage

1. Run the application:
```bash
mvn exec:java
```

2. Login with your username

3. Set the maximum number of messages you want to send

4. Use the menu to:
   - Send new messages
   - View sent messages
   - Search for messages
   - Generate reports
   - Manage stored and disregarded messages

## Message Format

- **Recipient Format**: Must start with '+' and be no longer than 14 characters
- **Message Length**: Maximum 250 characters
- **Message ID**: 10-digit unique identifier
- **Message Hash**: Format: ID.substring(0,2):messageNumber:firstWord+lastWord

## Test Data

The application includes test data for verification:

1. Message 1
   - Recipient: +27834557896
   - Message: "Did you get the cake?"
   - Status: Sent

2. Message 2
   - Recipient: +27838884567
   - Message: "Where are you? You are late! I have asked you to be on time."
   - Status: Stored

3. Message 3
   - Recipient: +27834484567
   - Message: "Yohoooo, I am at your gate."
   - Status: Disregarded

4. Message 4
   - Recipient: 0838884567
   - Message: "It is dinner time!"
   - Status: Sent

5. Message 5
   - Recipient: +27838884567
   - Message: "Ok, I am leaving without you."
   - Status: Stored

## Running Tests

To run the test suite:
```bash
mvn test
```

The test suite verifies:
- Message array population
- Message status tracking
- Search functionality
- Message deletion
- Report generation

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── mycompany/
│               └── quickchat/
│                   ├── QuickChat.java
│                   └── Message.java
└── test/
    └── java/
        └── com/
            └── mycompany/
                └── quickchat/
                    └── QuickChatTest.java
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

Hlanganani Khosa

## Acknowledgments

- The Independent Institute of Education (Pty) Ltd 2025
- All contributors and testers
