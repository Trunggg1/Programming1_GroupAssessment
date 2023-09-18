package Menu;

import Resources.TableInterface;
import Resources.UserInput;
import Port.AdminPort;
import Vehicle.AdminVehicle;
import users.Admin;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class AdminMenu {

    // Display a menu for user before login
    public void view() throws IOException, ParseException, InterruptedException {
        System.out.println("\n================================================= WELCOME TO TOM'S PRODIGIES STORE =================================================");
        System.out.println("1. Login by admin account");
        System.out.println("2. Back to authentication system");
        System.out.println("3. Exit");

        boolean validUser = false;
        Admin admin = new Admin();
        Authentication authentication = new Authentication();
        String option = UserInput.rawInput();
        switch (option) {
            case "1":
                System.out.println("\n================================================= LOGIN FORM =================================================");
                do {
                    // Ask user to input username and password
                    // if the username and password are not correct, the system will ask user to input again
                    // otherwise, the system will change to the homepage after login
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    if (!admin.verifyAdmin(username, password)) {
                        System.out.println("Wrong password, try again !!!!!");

                    } else {
                        this.viewHomepage();
                        validUser = true;
                    }
                } while (!validUser);

            case "2":
                authentication.mainMenu();

            case "3":
                System.exit(1);

            default:
                // If customer input another option that don't have in the menu
                // then the system will give he/she message and back to the viewpage
                System.out.println("THERE IS NO MATCHING RESULT, PLEASE TRY AGAIN!!!");
                TimeUnit.SECONDS.sleep(1);
                this.view();
        }
    }

    // Display the homepage for admin when he/she log in successfully
    public void viewHomepage() throws IOException, InterruptedException, ParseException {
        System.out.println("\n================================================= HOMEPAGE =================================================");
        System.out.println("\n1. List all ports' information");
        System.out.println("2. List all vehicles' information");
        System.out.println("3. List all port managers' information");
        System.out.println("4. List all containers information");
        System.out.println("5. List all statistics");
        System.out.println("7. Search port managers by ID");
        System.out.println("8. Update ports");
        System.out.println("9. Update vehicles");
        System.out.println("10. Remove customer by customer ID");
        System.out.println("11. Log out");
        System.out.println("12. Exit");

        Scanner scanner = new Scanner(System.in);
        AdminPort Port = new AdminPort();
        Admin admin = new Admin();
        AdminVehicle Vehicle = new AdminVehicle();
        AdminMenu adminMenu = new AdminMenu();

        String choice = UserInput.rawInput();
        switch (choice) {
            case "1":
                // Display the information of all Ports
                Port.getAllPortInfo();
                TimeUnit.SECONDS.sleep(1);
                adminMenu.viewHomepage();

            case "2":
                // Display the information of all Vehicle
                Vehicle.getAllVehicleInfo();
                TimeUnit.SECONDS.sleep(1);
                adminMenu.viewHomepage();

            /*case "3":
                // Display the information of all Port Managers
                admin.getAllPMInfo();
                TimeUnit.SECONDS.sleep(1);
                adminMenu.viewHomepage();

             */
        }
    }
}
