package Menu;

import Resources.ReadDatabase;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import users.PortManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PortManagerMenu {
    private PortManager user;
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

            while (true){
                HashMap<Number, String> value = promptInterface.startPrompts();

                String username = value.get(0);
                String password = value.get(1);

                ArrayList<String[]> data = ReadDatabase.readAllLines("./src/database/portManagers.txt");

                for (String[] line : data) {
                    this.user = new PortManager(line[0],line[1]);
                    break;
                }
            }
        }else{
            try{
                Authentication authentication = new Authentication();
                authentication.mainMenu();
            }catch (Exception e){

            }
        }
    }

    public void setup(){
        OptionsInterface profilePanel = new OptionsInterface("profilePanel","Profiel Panel", 2);
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
        containersPanel.addOption(4,"Go back", null);

        OptionsInterface portsPanel = new OptionsInterface("portPanel","Port Panel",2);
        portsPanel.addOption(1,"Update a port", null);
        portsPanel.addOption(2,"Go back", null);

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
        mainInterface.addOption(1,"profile panel", profilePanel);
        mainInterface.addOption(2,"Vehicles panel", vehiclesPanel);
        mainInterface.addOption(3,"Containers panel", containersPanel);
        mainInterface.addOption(4,"Port panel", portsPanel);
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
                }}
        }
    }
}