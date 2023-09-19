package Menu;

import Port.PMPort;
import Resources.ReadDatabase;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import users.PortManager;

import java.io.File;
import java.util.*;

public class PortManagerMenu {
    private PortManager user;
    private PMPort port;
    private OptionsInterface mainInterface;

    public PortManagerMenu() {}

    public void viewLogin(){
        OptionsInterface menu = new OptionsInterface("portManagerLogin","Port Manager login",2);
        menu.addOption(1,"Log in", null);
        menu.addOption(2,"Return", null);

        HashMap<String, String> results = menu.run();

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

                ArrayList<String[]> data = ReadDatabase.readAllLines("./src/database/portManagers.txt");

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

                        usernameField = stringTokenizer.nextToken();
                        passwordField = stringTokenizer.nextToken();
                        portIdField = stringTokenizer.nextToken();

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
        vehiclesPanel.addOption(1,"Update a vehicle", null);
        vehiclesPanel.addOption(2,"Go back", null);

        OptionsInterface containersPanel = new OptionsInterface("containersPanel","Containers Panel",2);
        containersPanel.addOption(1,"Add a container", null);
        containersPanel.addOption(2,"Update a container", null);
        containersPanel.addOption(3,"Delete a container", null);
        containersPanel.addOption(4,"Display all containers", null);
        containersPanel.addOption(5,"Go back", null);

        //Port Panel
        OptionsInterface portPanel = new OptionsInterface("portPanel","Port Panel",2);
        portPanel.addOption(1,"Update a port", null);
        portPanel.addOption(2,"Go back", null);

        OptionsInterface tripsPanel = new OptionsInterface("tripPanel","Trip Panel",2);
        tripsPanel.addOption(1,"Add a trip", null);
        tripsPanel.addOption(2,"Update a trip", null);
        tripsPanel.addOption(3,"Delete a trip", null);
        tripsPanel.addOption(4,"Go back", null);

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
        while (true){
            HashMap<String, String> interfaceData = mainInterface.run();

            String id = interfaceData.get("id");
            String option = interfaceData.get("option");

            switch (id) {
                case "profilePanel" -> {
                }
                case "vehiclesPanel" ->{
                }
                case "portPanel" ->{
                    this.port.handlePortOptions(option);
                }
                case "containersPanel" ->{
                    this.port.handleContainerOptions(option);
                }
            }

        }
    }
}