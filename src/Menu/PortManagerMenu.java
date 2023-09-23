package Menu;

import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
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
    public void viewLogin(){
        OptionsInterface menu = new OptionsInterface("portManagerLogin","Port Manager login",2);
        menu.addOption(1,"Log in",null, null);
        menu.addOption(2,"Return",null, null);

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

                LineFilters lineFilters = new LineFilters();
                lineFilters.addFilter(1,usernameInput, FiltersType.INCLUDE);
                ArrayList<String> lines = LinesHandler.getLinesFromDatabase(PortManager.usersFilePath, lineFilters);

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
        profilePanel.addOption(1,"Change username",null,null);
        profilePanel.addOption(2,"Change password",null, null);
        profilePanel.addOption(3,"Go back",null, null);

        OptionsInterface vehiclesPanel = new OptionsInterface("vehiclesPanel","Vehicles Panel",3);
        vehiclesPanel.addOption(1,"Load a container",null, null);
        vehiclesPanel.addOption(2,"Unload a container",null, null);
        vehiclesPanel.addOption(3,"Update a vehicle from the port",null, null);
        vehiclesPanel.addOption(4,"Display all vehicles from the port",null, null);
        vehiclesPanel.addOption(5,"Display all vehicles from database",null, null);
        vehiclesPanel.addOption(6,"Go back",null, null);

        OptionsInterface containersPanel = new OptionsInterface("containersPanel","Containers Panel",3);
        containersPanel.addOption(1,"Add a container to database",null, null);
        containersPanel.addOption(2,"Update a container from database",null, null);
        containersPanel.addOption(3,"Delete a container from database",null, null);
        containersPanel.addOption(4,"Display all containers from the port",null, null);
        containersPanel.addOption(5,"Display all containers from database",null, null);
        containersPanel.addOption(6,"Go back",null,null);

        OptionsInterface portPanel = new OptionsInterface("portPanel","Port Panel",3);
        portPanel.addOption(1,"Update the port",null,null);
        portPanel.addOption(2,"Display all ports from database",null,null);
        portPanel.addOption(3,"Go back",null,null);

        OptionsInterface tripsPanel = new OptionsInterface("tripsPanel","Trips Panel",3);
        tripsPanel.addOption(1,"Create a trip",null,null);
        tripsPanel.addOption(2,"Complete a trip",null,null);
        tripsPanel.addOption(4,"Display all trips from the port",null,null);
        tripsPanel.addOption(5,"Display all trips from database",null,null);
        tripsPanel.addOption(6,"Go back",null,null);

        OptionsInterface statisticPanel = new OptionsInterface("statisticPanel","Statistic Panel",4);
        statisticPanel.addOption(1,"Profile",null, null);
        statisticPanel.addOption(2,"Port",null, null);
        statisticPanel.addOption(3,"Containers",null, null);
        statisticPanel.addOption(4,"Vehicles",null, null);
        statisticPanel.addOption(5,"Trips",null, null);
        statisticPanel.addOption(6,"Summary",null, null);
        statisticPanel.addOption(7,"Go back",null, null);

        mainInterface = new OptionsInterface("controlPanel", "Control Panel", 4);
        mainInterface.addOption(1,"Profile panel",null, profilePanel);
        mainInterface.addOption(2,"Vehicles panel",null, vehiclesPanel);
        mainInterface.addOption(3,"Containers panel",null, containersPanel);
        mainInterface.addOption(4,"Port panel",null, portPanel);
        mainInterface.addOption(5,"Trips panel",null, tripsPanel);
        mainInterface.addOption(6,"Statistic",null, statisticPanel);
        mainInterface.addOption(7,"Log out",null, null);
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