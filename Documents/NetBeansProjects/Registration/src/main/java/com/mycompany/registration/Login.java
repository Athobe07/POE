package com.mycompany.registration;

public class Login {
    // Stores the user's first name
    public String firstName;
    // Stores the user's last name
    public String lastName;
    // Stores the user's username
    public String username;
    // Stores the user's password
    public String password;
    // Stores the user's cell phone number
    public String cellPhoneNumber;
  
// Constructor used to store the user's registration details  
  public Login(String firstName, String lastName, String username,
                 String password, String cellPhoneNumber) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }
    // Checks the user name meets the required complexity
    public boolean checkUserName() {

        return username.contains("_") && username.length() <= 5;
    }
    // Checks the password meets the required complexity
    public boolean checkPasswordComplexity() {

        if (password.length() < 8) {
            return false;
        }
        // Variables used to check the password requirements
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapital && hasNumber && hasSpecialCharacter;
    }
    // Checks the phone number is it a SA number
    public boolean checkCellPhoneNumber() {

        return cellPhoneNumber.matches("^\\+27\\d{9}$");
    }
    // Register the user after checking the username, password and cell phone number
    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a " + "special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted or does not contain international code.";
        }

        return "User registered successfully.";
    }
    // Compares the entered username and password with the users details
    public boolean loginUser(String enteredUsername, String enteredPassword) {

        return enteredUsername.equals(username)
                && enteredPassword.equals(password);
    }
    // Returns a message if the Login was successful
    public String returnLoginStatus(boolean loginStatus) {

        if (loginStatus) {

            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";

        } else {

            return "Username or password incorrect, please try again.";
        }
    }
}

