

import java.util.Scanner;

public class Login {
    private static final String CORRECT_USERNAME = "1";
    private static final String CORRECT_PASSWORD = "1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isAuthenticated = false;

        System.out.println("===== Hospital Management System Login =====");

        while (!isAuthenticated) {
            // Take username input
            System.out.print("Enter Username: ");
            String enteredUsername = scanner.nextLine().trim();

            // Take password input
            System.out.print("Enter Password: ");
            String enteredPassword = scanner.nextLine().trim();

            // Check credentials
            if (enteredUsername.equals(CORRECT_USERNAME) && enteredPassword.equals(CORRECT_PASSWORD)) {
                System.out.println("\n Login Successful! Redirecting to Main Menu...\n");
                isAuthenticated = true;  // Exit loop
                MainMenu.display();  // Call Main Menu (Will be created later)
            } else {
                System.out.println("\n Incorrect Username or Password. Try again.\n");
            }
        }

        scanner.close();
    }
}