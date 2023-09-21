package Menu;

import Port.PMPort;
import Resource.ReadDatabase;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import User.PortManager;

import java.io.*;
import java.util.*;

public class PortManagerMenu {
    private PortManager user;
    private PMPort port;
    private OptionsInterface mainInterface;

    public PortManagerMenu() {}
    public static boolean lineHasId(String filePath, String id){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();

            String fileLine;

            while ((fileLine = reader.readLine()) != null){
                String[] parts = fileLine.split(",");

                String currentId = parts[0];
                if (currentId.equals(id)) {
                    return  true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static boolean updateLinesWithId(String filePath, String id, String line){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();

            String fileLine;
            while ((fileLine = reader.readLine()) != null){
                // Split the line into parts based on a delimiter (e.g., comma or tab)
                String[] parts = fileLine.split(",");

                // Check if the id in the current line matches the target id
                String currentId = parts[0].trim();
                String targetId = id.trim();

                if (currentId.equals(targetId)) {
                    content.append(line).append("\n");
                } else {
                    content.append(fileLine).append("\n");
                }
            }

            reader.close();

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();

            return true;
        }catch (IOException e){
            return  false;
        }
    }
    public static boolean deleteLinesWithId(String filePath, String id){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();

            String fileLine;
            while ((fileLine = reader.readLine()) != null){
                // Split the line into parts based on a delimiter (e.g., comma or tab)
                String[] parts = fileLine.split(",");

                // Check if the id in the current line matches the target id
                String currentId = parts[0];
                if (currentId.equals(id)) {
                    //Do nothing
                } else {
                    content.append(fileLine).append("\n");
                }
            }

            reader.close();

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();

            return true;
        }catch (IOException e){
            return  false;
        }
    }
    public static boolean addLineToDatabase(String filePath, String line){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true)); // Use 'true' to append

            writer.newLine();
            writer.write(line);
            writer.close();

            return true;
        }catch (IOException e){
            return  false;
        }
    }
    public void viewLogin(){
        OptionsInterface menu = new OptionsInterface("portManagerLogin","Port Manager login",2);
        menu.addOption(1,"Log in", null);
        menu.addOption(2,"Return", null);

        HashMap<String, String> results = menu.run(null);

        String option = results.get("option");

        if(option.equals("Log in")){
            PromptsInterface promptInterface = new PromptsInterface("portManagerLogin","Login for port manager");

            promptInterface.addPrompt("Enter username");
            promptInterface.addPrompt("Enter password");

            boolean keepRunning = true;

            while (keepRunning){
                //Begin to ask question
                HashMap<Number, String> value = promptInterface.startPrompts();

                //Get data from input
                String username = value.get(1);
                String password = value.get(2);

                Scanner fileData;
                try{
                    fileData = new Scanner(new File("./src/database/portManagers.txt"));
                }catch (Exception e){
                    fileData = null;
                }

                boolean userFound = false;

                String usernameField;
                String passwordField;
                String portIdField = null;

                if(fileData!= null){
                    while (fileData.hasNext()){
                        String line = fileData.nextLine();
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");

                        usernameField = stringTokenizer.nextToken().trim();
                        passwordField = stringTokenizer.nextToken().trim();
                        portIdField = stringTokenizer.nextToken().trim();

                        if(usernameField.equals(username) && passwordField.equals(password)){
                            userFound = true;
                            break;
                        }
                    }
                }

                if(userFound){
                    this.user = new PortManager(username,password);
                    this.port = new PMPort(portIdField);
                    keepRunning = false;
                }else{
                    System.out.println("Username or Password is incorrect!");
                }
            }

            setup();
            run();
        }else{
            try{
                Authentication authentication = new Authentication();
                authentication.mainMenu();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void setup(){
        OptionsInterface profilePanel = new OptionsInterface("profilePanel","Profile Panel", 2);
        profilePanel.addOption(1,"Change username",null);
        profilePanel.addOption(2,"Change password", null);
        profilePanel.addOption(3,"Log out", null);
        profilePanel.addOption(4,"Go back", null);

        OptionsInterface vehiclesPanel = new OptionsInterface("vehiclesPanel","Vehicles Panel",2);
        vehiclesPanel.addOption(1,"Update a vehicle from the port", null);
        vehiclesPanel.addOption(2,"Display all vehicles from the port", null);
        vehiclesPanel.addOption(3,"Display all vehicles from database", null);
        vehiclesPanel.addOption(4,"Go back", null);

        OptionsInterface containersPanel = new OptionsInterface("containersPanel","Containers Panel",3);
        containersPanel.addOption(1,"Add a container to database", null);
        containersPanel.addOption(2,"Update a container from database", null);
        containersPanel.addOption(3,"Delete a container from database", null);
        containersPanel.addOption(4,"Display all containers from the port", null);
        containersPanel.addOption(5,"Display all containers from database", null);
        containersPanel.addOption(6,"Go back", null);

        //Port Panel
        OptionsInterface portPanel = new OptionsInterface("portPanel","Port Panel",3);
        portPanel.addOption(1,"Update the port", null);
        portPanel.addOption(2,"Display all ports from database", null);
        portPanel.addOption(3,"Go back", null);

        OptionsInterface tripsPanel = new OptionsInterface("tripsPanel","Trips Panel",3);
        tripsPanel.addOption(1,"Create a trip", null);
        tripsPanel.addOption(2,"Update a trip from database", null);
        tripsPanel.addOption(3,"Delete a trip from database", null);
        tripsPanel.addOption(4,"Display all trips from the port", null);
        tripsPanel.addOption(5,"Display all trips from database", null);
        tripsPanel.addOption(6,"Go back", null);

        OptionsInterface statisticPanel = new OptionsInterface("statisticPanel","Statistic Panel",4);
        statisticPanel.addOption(1,"Profile", null);
        statisticPanel.addOption(2,"Port", null);
        statisticPanel.addOption(3,"Containers", null);
        statisticPanel.addOption(4,"Vehicles", null);
        statisticPanel.addOption(5,"Trips", null);
        statisticPanel.addOption(6,"Summary", null);
        statisticPanel.addOption(7,"Go back", null);

        mainInterface = new OptionsInterface("controlPanel", "Control Panel", 4);
        mainInterface.addOption(1,"Profile panel", profilePanel);
        mainInterface.addOption(2,"Vehicles panel", vehiclesPanel);
        mainInterface.addOption(3,"Containers panel", containersPanel);
        mainInterface.addOption(4,"Port panel", portPanel);
        mainInterface.addOption(5,"Trips panel", tripsPanel);
        mainInterface.addOption(6,"Statistic", statisticPanel);
    }
    public void run(){
        String interfaceId = "mainInterface";

        while (true){
            HashMap<String, String> interfaceData = mainInterface.run(interfaceId);

            interfaceId = "mainInterface";

            String id = interfaceData.get("id");
            String option = interfaceData.get("option");

            switch (id) {
                case "profilePanel" -> {
                    this.user.handleProfileOptions(option);
                }
                case "vehiclesPanel" ->{
                    this.port.handleVehicleOptions(option);
                    interfaceId = "vehiclesPanel";
                }
                case "portPanel" ->{
                    this.port.handlePortOptions(option);
                    interfaceId = "portPanel";
                }
                case "containersPanel" ->{
                    this.port.handleContainerOptions(option);
                    interfaceId = "containersPanel";
                }
                case "tripsPanel" ->{
                    this.port.handleTripsOptions(option);
                    interfaceId = "tripsPanel";
                }
                case "statisticPanel" ->{
                    this.port.handleStatisticOptions(option);
                    interfaceId = "statisticPanel";
                }
            }

        }
    }
}