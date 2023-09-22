package Port;

import Container.PMContainer;
import DatabaseLinesHandler.FiltersType;
import DatabaseLinesHandler.LineFilters;
import DatabaseLinesHandler.LinesHandler;
import Menu.PortManagerMenu;
import Trip.PMTrip;
import Vehicle.PMVehicle;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static Menu.PortManagerMenu.updateLinesWithId;

public class PMPort {
    public static final String[] containersCols = {"ID","Name","Capacity","Landing Ability"};
    public static final String portsFilePath = "./src/database/ports.txt";
    private String id;
    private String name;
    private String capacity;
    private String landingAbility;

    public static OptionsInterface createOptionsInterfaceForPortsLandingTypes(String title){
        OptionsInterface landingTypesInterface = new OptionsInterface("askQuestion",title,2);
        landingTypesInterface.addOption(1,"Truck Availability",null,null);
        landingTypesInterface.addOption(2,"Unavailability",null, null);

       return landingTypesInterface;
    }
    public static OptionsInterface createOptionsInterfaceForPorts(String name, LineFilters lineFilters){
        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(portsFilePath,lineFilters);

        OptionsInterface portsInterface = new OptionsInterface("portsInterface",name, 5);

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            String[] parts = line.split(",");

            String optionName = parts[1] + "(" + parts[0] + ")";

            portsInterface.addOption(i + 1,optionName,line,null);

            if(i == lines.size() - 1){
                portsInterface.addOption(i + 1,optionName,line,null);
                portsInterface.addOption(i + 2,"Return",null,null);
            }
        }

        return portsInterface;
    }
    public static TableInterface createTableFromDatabase(){
        TableInterface table = new TableInterface("ports","Ports",containersCols,",");

        ArrayList<String> lines = PortManagerMenu.getLinesFromDatabase(portsFilePath);

        for(String line: lines){
            table.addRow(line);
        }

        return table;
    }

    public PMPort(String id) {
        this.id = id.trim();
        getPortData();
    }
    public PMPort(String id, String name, String capacity, String landingAbility) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.landingAbility = landingAbility;
    }

    private void getPortData(){
        LineFilters lineFilters = new LineFilters(FiltersType.INCLUDE);
        lineFilters.addFilter(1,  this.id);

        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(portsFilePath, lineFilters);

         String line = lines.get(0);

        if(line != null){
            String[] parts = line.split(",");

            this.id = parts[0];
            this.name = parts[1];
            this.capacity = parts[2];
            this.landingAbility = parts[3];
        }
    }

    public String getLandingAbility() {
        return landingAbility;
    }

    public String getId() {
        return id;
    }

    public String toString(){
        return  id + ", " + name + ", " + capacity + ", " + landingAbility;
    }
    private void updatePort(){
        OptionsInterface updateInterface = new OptionsInterface("update","What update for the port?",4);
        updateInterface.addOption(1,"Name",null,null);
        updateInterface.addOption(2,"Capacity",null,null);
        updateInterface.addOption(3,"Landing Ability",null,null);
        updateInterface.addOption(4,"Return",null,null);

        Scanner input = new Scanner(System.in);

        boolean keepRunning = true;
        while (keepRunning){
            System.out.println("Current port: " + this);
            HashMap<String, String> interfaceData = updateInterface.run(null);

            String option = interfaceData.get("option");

            switch (option){
                case "Name":{
                    System.out.println("Enter name: ");

                    this.name = input.nextLine().trim();

                    break;
                }
                case "Capacity":{
                    System.out.println("Enter Capacity(Example: 1000Kg): ");

                    String inputResult = input.nextLine().trim();

                    if(inputResult.matches("\\d+Kg")){
                        this.capacity = inputResult;
                    }else{
                        this.capacity = inputResult + "Kg";
                    }

                    break;
                }
                case "Landing Ability":{
                    OptionsInterface landingTypesInterface = createOptionsInterfaceForPortsLandingTypes("Which landing ability does this port have?");

                    interfaceData = landingTypesInterface.run(null);

                    this.landingAbility = interfaceData.get("option");

                    break;
                }
                case "Return":{
                    keepRunning = false;
                    break;
                }

            }

            if(keepRunning){
                boolean success = updateLinesWithId(portsFilePath, id,0, toString());

                if(success){
                    System.out.println("Updated port successfully!");
                    break;
                }else{
                    System.out.println("Failed to update port!");
                }
            }
        }
    }
    public void handlePortOptions(String option){
        switch (option){
            case "Update the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    updatePort();
                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
            case "Display all ports from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = createTableFromDatabase();
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
        }
    }
    public void handleVehicleOptions(String option) {
        switch (option){
            case "Update a vehicle from the port": {
                PMVehicle.updateVehicleFromDatabase(this.id);
                break;
            }
            case "Display all vehicles from the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMVehicle.createTableFromDatabase(this.id);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }case "Display all vehicles from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMVehicle.createTableFromDatabase(null);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
        }
    }
    public void handleContainerOptions(String option){
        switch (option){
            case "Add a container to database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    PMContainer.addContainerToDatabase(id);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
            case "Update a container from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    PMContainer.updateContainerFromDatabase();

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.nextLine();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
            case "Delete a container from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    PMContainer.deleteContainerFromDatabase();

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.nextLine();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
            case "Display all containers from the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    LineFilters lineFilters = new LineFilters(FiltersType.INCLUDE);
                    lineFilters.addFilter(4, this.id);

                    TableInterface table = PMContainer.createTableFromDatabase(lineFilters);

                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
            case "Display all containers from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMContainer.createTableFromDatabase(null);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
        }
    }
    public void handleTripsOptions(String option) {
        switch (option){
            case "Create a trip": {
                PMTrip.createATrip(this);
                break;
            }
            case "Update a trip from database": {
                PMTrip.updateTripFromPort();
                break;
            }case "Complete a trip": {
                PMTrip.completeTrip();
                break;
            }
            case "Display all trips from the port": {
                PMTrip.displayAllTripsFromDatabase(id);
                break;
            }
            case "Display trips from database": {
                PMTrip.displayAllTripsFromDatabase(null);
                break;
            }
        }
    }
    public void handleStatisticOptions(String option) {
        switch (option){
            case "Profile": {
                Scanner input = new Scanner(System.in);
                break;
            }
            case "Port": {
                break;
            }case "Containers": {
                break;
            }
            case "Vehicles": {
                break;
            }
            case "Trips": {
                break;
            }
            case "Summary": {
                break;
            }
        }
    }
}