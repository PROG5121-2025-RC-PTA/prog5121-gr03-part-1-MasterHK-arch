/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.logindetails;

import java.util.Scanner;

/**
 *
 * @author RC_Student_lab
 */
public class LoginDetails {

    public static void main(String[] args) {
                LoginDetails loginDetails = new LoginDetails();
        
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== User Registration ===\n");
        
        
        System.out.print("Enter username (must contain an underscore and be 5 characters or less): ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password (must be at least 8 characters with a capital letter, number, and special character): ");
        String password = scanner.nextLine();
        
        System.out.print("Enter South African cell phone number (format: +27XXXXXXXXX): ");
        String cellPhone = scanner.nextLine();
        
        User user = loginDetails.new User(username, password, cellPhone);
        String registrationResult = user.registerUser();
        
        System.out.println("\n" + registrationResult);
        

        if (registrationResult.equals("Registration successful")) {
            System.out.println("\n=== User Login ===\n");
            
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();
            
            System.out.print("Enter your first name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine();
            
            String loginStatus = user.returnLoginStatus(loginUsername, loginPassword, firstName, lastName);
            System.out.println("\n" + loginStatus);
        }
        
        scanner.close();
    }
    public class User {
      private final String username;                                     
      private final String password;
      private final String cellPhone; 


   
      public User(String username,String password, String cellPhone){
           
          this.username = username;
          this.password = password;
          this.cellPhone = cellPhone;
      }
      
       public boolean checkUserName(){
           return username.contains("_") && username.length() <= 5;
       }
          
       public boolean checkPasswordComplexity(){
           return password.length() >= 8 && 
                  password.matches(".*[A-Z].*") && 
                  password.matches(".*\\d.*") && 
                  password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};'].*");
       }
       public boolean checkCellPhoneNumber(){
           return cellPhone.matches("^\\+27\\d{9}$"); // e.g., +27831234567
       }
          
       public String registerUser(){
           if(!checkUserName()){
               return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length";
           }
           if (!checkPasswordComplexity()){
               return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
           }
           
           if (!checkCellPhoneNumber()){
               return "Cell phone number incorrectly formatted or does not contain international code";
           }
           return "Registration successful";
       }
       public boolean loginUser(String inputUsername, String inputPassword){
           return
                   
            username.equals (inputUsername)
                   &&
            password.equals(inputPassword);
       }
       public String returnLoginStatus(String inputUsername, String inputPassword, String firstName, String lastName){
           if (loginUser(inputUsername, inputPassword)){
               return "Welcome " + firstName + ", " + lastName + " it is great to see you again";
           } else {
               return "Username or password incorrect, please try again";
           }
        }
      }
}


