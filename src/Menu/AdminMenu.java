package Menu;

import Resource.ReadDatabase;
import Resource.UserInput;
import Port.AdminPort;
import Vehicle.AdminVehicle;
import User.Admin;


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
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    // if the username and password are not correct, the system will ask user to input again
                    if (!admin.verifyAdmin(username, password)) {
                        System.out.println("Wrong password, try again!");
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
                // System give user message and back to the viewpage
                System.out.println("THERE IS NO MATCHING RESULT, PLEASE TRY AGAIN!!!");
                TimeUnit.SECONDS.sleep(1);
                this.view();
        }
    }

    // Admin homepage when user log in successfully
    public void viewHomepage() throws IOException, InterruptedException, ParseException {
        System.out.println("\n================================================= HOMEPAGE =================================================");
        System.out.println("1. Ports' information");
        System.out.println("2. Vehicles' information");
        System.out.println("3. Containers information");
        System.out.println("4. Port managers' information");
        System.out.println("5. Log out");
        System.out.println("6. Exit");

        Scanner scanner = new Scanner(System.in);
        AdminPort Port = new AdminPort();
        Admin admin = new Admin();
        AdminVehicle Vehicle = new AdminVehicle();
        AdminMenu adminMenu = new AdminMenu();
        String choice = UserInput.rawInput();

        switch (choice) {
            case "1":
                // Allow user view and update ports
                System.out.println("\n================================================= PORT'S INFORMATION =================================================");
                System.out.println("1. List all ports' information");
                System.out.println("2. Add port");
                System.out.println("3. Remove port");
                System.out.println("4. Update ports' name");
                System.out.println("5. Update ports' capacity");
                System.out.println("6. Update ports' landing ability");
                System.out.println("7. Back to homepage");
                String option = UserInput.rawInput();

                switch (option) {
                    // Display the information of all Ports
                    case "1":
                        Port.getAllPortInfo();
                        TimeUnit.SECONDS.sleep(1);
                        adminMenu.viewHomepage();

                        //Create new port
                    case "2":
                        admin.addPort();
                        TimeUnit.SECONDS.sleep(1);
                        adminMenu.viewHomepage();

                        //Delete new port
                    case "3":
                        admin.deletePort();
                        TimeUnit.SECONDS.sleep(1);
                        adminMenu.viewHomepage();

                        //Update port's name
                    case "4":
                        Port.getAllPortInfo();
                        System.out.print("Enter port's ID to update: ");
                        String nId = scanner.nextLine();
                        System.out.println("Update port's name to:");
                        String name = scanner.nextLine();
                        Port.updatePortName("./src/database/ports.txt", name, nId);
                        adminMenu.viewHomepage();

                        //Update port's capacity
                    case "5":
                        Port.getAllPortInfo();
                        System.out.print("Enter port's ID to update: ");
                        String cId = scanner.nextLine();
                        System.out.println("Update port's capacity to:");
                        String capacity = scanner.nextLine();
                        Port.updatePortCapacity("./src/database/ports.txt", capacity, cId);
                        adminMenu.viewHomepage();

                        //Update port's landing ability
                    case "6":
                        Port.getAllPortInfo();
                        System.out.print("Enter port's ID to update: ");
                        String lId = scanner.nextLine();
                        System.out.println("Update port's landing ability to:");
                        String landingAbility = scanner.nextLine();
                        Port.updatePortLandingAbility("./src/database/ports.txt", landingAbility, lId);
                        adminMenu.viewHomepage();

                        //Back to homepage
                    case "7":
                        adminMenu.viewHomepage();
                }

            case "2":
                // Allow user view and update ports
                System.out.println("\n================================================= VEHICLE'S INFORMATION =================================================");
                System.out.println("1. List all vehicles' information");
                System.out.println("2. Add vehicle");
                System.out.println("3. Remove vehicle");
                System.out.println("4. Update vehicles' type");
                System.out.println("5. Update vehicles' carrying capacity");
                System.out.println("6. Back to homepage");
                String options = UserInput.rawInput();

                switch (options) {
                    // Display the information of all vehicle
                    case "1":
                        Vehicle.getAllVehicleInfo();
                        TimeUnit.SECONDS.sleep(1);
                        adminMenu.viewHomepage();

                        //Create new vehicle
                    case "2":
                        admin.addVehicle();
                        TimeUnit.SECONDS.sleep(1);
                        adminMenu.viewHomepage();

                        //Delete new vehicle
                    case "3":
                        admin.deleteVehicle();
                        TimeUnit.SECONDS.sleep(1);
                        adminMenu.viewHomepage();

                        //Update vehicle's type
                    case "4":
                        Vehicle.getAllVehicleInfo();
                        System.out.print("Enter vehicle's ID to update: ");
                        String vId = scanner.nextLine();
                        System.out.println("Update vehicle's name to:");
                        String type = scanner.nextLine();
                        Vehicle.updateVehicleType("./src/database/vehicles.txt", type, vId);
                        adminMenu.viewHomepage();

                        //Update vehicle's capacity
                    case "5":
                        Vehicle.getAllVehicleInfo();
                        System.out.print("Enter vehicle's ID to update: ");
                        String vcId = scanner.nextLine();
                        System.out.println("Update vehicle's capacity to:");
                        String capacity = scanner.nextLine();
                        Vehicle.updateVehicleCapacity("./src/database/vehicles.txt", capacity, vcId);
                        adminMenu.viewHomepage();

                        //Back to homepage
                    case "6":
                        adminMenu.viewHomepage();
                }

            /*case "3":
                // Display the information of all Port Managers
                admin.getAllPMInfo();
                TimeUnit.SECONDS.sleep(1);
                adminMenu.viewHomepage();

             */


        }
    }
}
