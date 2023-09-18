package Menu;

import Resources.UserInput;
import Resources.TableInterface;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Authentication{

    // Main menu when user start our system
    public void mainMenu() throws IOException, InterruptedException, ParseException {
        System.out.println("COSC2081 GROUP ASSIGNMENT");
        System.out.println("CONTAINER PORT MANAGEMENT SYSTEM");
        System.out.println("Instructor: Mr. Minh Vu & Dr. Phong Ngo");
        System.out.println("Group: Group_Work");
        TableInterface.setShowVerticalLines(true);

        TableInterface.setHeaders("sID", "FULL NAME");
        TableInterface.addRow("S3891483", "Vu Loc");
        TableInterface.addRow("S3891483", "Vu Loc");
        TableInterface.addRow("S3891483", "Vu Loc");
        TableInterface.addRow("S3891483", "Vu Loc");
        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());

        System.out.println("\n================================================= WELCOME TO CONTAINER PORT MANAGEMENT SYSTEM =================================================");
        System.out.println("1. Use as a Administrator");
        System.out.println("2. Use as a Port Manager");
        System.out.println("3. Exit");

        AdminMenu adminMenu = new AdminMenu();
        PortManagerMenu portManagerMenu = new PortManagerMenu();

        String option = UserInput.rawInput();
        switch (option) {
            case "1":
                adminMenu.view();
            case "2":
                portManagerMenu.viewLogin();
            case "3":
                System.exit(1);
            default:
                System.out.println("THERE IS NO MATCHING RESULT, PLEASE TRY AGAIN!!!");
                TimeUnit.SECONDS.sleep(1);
                this.mainMenu();
        }
    }
}