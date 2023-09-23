package User.abstracts;

import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
import LinesHandler.FiltersType;

import java.util.Scanner;

public abstract class User {
    public static final String usersFilePath = "./src/database/portManagers.txt";
    private String type;
    private String username;
    private String password;

    public User(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }
    public void changePassword() {
        Scanner scanner = new Scanner(System.in);

        // Create a menu to ask for the current password
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        // Check if the entered current password matches the user's current password
        if (!currentPassword.equals(password)) {
            System.out.println("Incorrect current password. Password change failed.");
            return;
        }

        // Ask the user to enter a new password
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();

        // Update the password in the database
        LineFilters filters = new LineFilters();
        filters.addFilter(1,username, FiltersType.INCLUDE);
        String userLine = LinesHandler.getLinesFromDatabase(usersFilePath,filters).get(0);

        String[] userParts = userLine.split(",");

        userParts[1] = newPassword;

        String newLine = String.join(",",userParts);

        LinesHandler.updateLinesFromDatabase(usersFilePath,newLine,filters);



        System.out.println("Password changed successfully.");
        this.password = newPassword;
    }
    public void changeUsername() {
        Scanner scanner = new Scanner(System.in);

        // Create a menu to ask for the current password
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        // Check if the entered current password matches the user's current password
        if (!currentPassword.equals(password)) {
            System.out.println("Incorrect current password. Username change failed.");
            return;
        }

        // Ask the user to enter a new username
        System.out.print("Enter your new username: ");
        String newUsername = scanner.nextLine();

        // Update the username in the database
        LineFilters filters = new LineFilters();
        filters.addFilter(1,username, FiltersType.INCLUDE);
        String userLine = LinesHandler.getLinesFromDatabase(usersFilePath,filters).get(0);

        String[] userParts = userLine.split(",");

        userParts[0] = newUsername;

        String newLine = String.join(",",userParts);

        LinesHandler.updateLinesFromDatabase(usersFilePath,newLine,filters);

        System.out.println("Username changed successfully.");
        this.username = newUsername;
    }

    public void handleProfileOptions(String option) {
        switch (option){
            case "Change username"->{
               changeUsername();
            }
            case "Change password"->{
                changePassword();
            }
        }
    }
    public void showProfile(){
        System.out.println("Username: " + username);
        System.out.println("Password: " +  password);
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
