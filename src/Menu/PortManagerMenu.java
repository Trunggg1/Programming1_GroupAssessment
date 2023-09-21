package Menu;

import Port.PMPort;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import User.PortManager;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class PortManagerMenu {
    private PortManager user;
    private PMPort port;
    private OptionsInterface mainInterface;
    public PortManagerMenu() {}
    public static ArrayList<String> getLinesFromDatabase(String filePath){
        try{
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> lines = new ArrayList<>();

            String fileLine;
            while ((fileLine = reader.readLine()) != null){
               lines.add(fileLine);
            }

            return lines;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkLineHasId(String filePath, String id){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));

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

                if (!currentId.equals(id)) {
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

            promptInterface.addPrompt("Enter Username");
            promptInterface.addPrompt("Enter Password");

            while (true){
                //Begin to ask question
                HashMap<Number, String> value = promptInterface.startPrompts();

                //Get data from input
                String usernameInput = value.get(1).trim();
                String passwordInput = value.get(2).trim();

                ArrayList<String> lines = getLinesFromDatabase(PortManager.usersFilePath);

                boolean userFound = false;

                String portId = null;

                for(String line: lines){
                    String[] parts = line.split(",");

                    String trimmedUsername = parts[0].trim();
                    String trimmedPassword = parts[1].trim();

                    System.out.println(trimmedUsername + ": " + trimmedPassword);
                    if(trimmedUsername.equals(usernameInput) && trimmedPassword.equals(passwordInput)){
                        userFound = true;
                        portId = parts[2];
                        break;
                    }
                }

                if(userFound){
                    this.user = new PortManager(usernameInput,passwordInput);
                    this.port = new PMPort(portId);

                    break;
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
            } catch (IOException | ParseException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setup(){
        OptionsInterface profilePanel = new OptionsInterface("profilePanel","Profile Panel", 2);
        profilePanel.addOption(1,"Change username",null);
        profilePanel.addOption(2,"Change password", null);
        profilePanel.addOption(3,"Go back", null);

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
        mainInterface.addOption(7,"Log out", null);
    }
    public void run(){
        String interfaceId = "mainInterface";

        while (true){
            HashMap<String, String> interfaceData = mainInterface.run(interfaceId);

            String id = interfaceData.get("id");
            String option = interfaceData.get("option");

            if(option.equals("Log out")){
                viewLogin();
                break;
            }

            switch (id) {
                case "profilePanel" -> this.user.handleProfileOptions(option);
                case "vehiclesPanel" -> this.port.handleVehicleOptions(option);
                case "portPanel" -> this.port.handlePortOptions(option);
                case "containersPanel" -> this.port.handleContainerOptions(option);
                case "tripsPanel" -> this.port.handleTripsOptions(option);
                case "statisticPanel" -> this.port.handleStatisticOptions(option);
            }

            interfaceId = id;
        }
    }
}