package com.mycompany.registration;
import java.util.Scanner;

public class Main {
    // Start the registration and login application
    public static void main(String[] args) {
        // Creates a Scanner object to allow the user to enter information
        Scanner scanner = new Scanner(System.in);

        // Displays the registration system
        System.out.println("       REGISTRATION SYSTEM");
        
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your South African cell phone number: ");
        String cellPhoneNumber = scanner.nextLine();
        
        // Creates a Login object using the information entered by the user
        Login user = new Login(
                firstName,
                lastName,
                username,
                password,
                cellPhoneNumber
        );
        
        System.out.println();
        System.out.println("Registration Result:");

        String registrationMessage = user.registerUser();

        System.out.println(registrationMessage);
        
        // Checks if the registration was successful
        if (registrationMessage.equals("User registered successfully.")) {

            System.out.println();
            
            // Displays the login system
            System.out.println("             LOGIN");
            
            // Asks the user to enter their username to log in
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            
            // Asks the user to enter their password to log in
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();
            
            // Checks whether the entered login details are correct
            boolean loginStatus =
                    user.loginUser(loginUsername, loginPassword);

            System.out.println();
            System.out.println(user.returnLoginStatus(loginStatus));

        } else {
            // Informs the user that their registration details need to be corrected
            System.out.println();
            System.out.println(
                    "Please correct your registration details and try again."
            );
        }
        scanner.close();
    }
}