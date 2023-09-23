package Port;

import Container.PMContainer;
import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
import Tools.Tools;
import Trip.PMTrip;
import Vehicle.PMVehicle;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PMPort {
    public  static String regexCapacity = "(?i)[kK]g";
    public static final String[] portsCols = {"ID","Name","Capacity","Landing Ability","Latitude","Longitude"};
    public static final int colId = 1;
    public static final int colName = 2;
    public static final int colCapacity = 3;
    public static final int colLandingAbility = 4;
    public static final int colLatitude = 5;
    public static final int colLongitude = 6;
    public static final String portsFilePath = "./src/database/PMports.txt";
    private String id;
    private String name;
    private String capacity;
    private String landingAbility;
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in kilometers
        double earthRadius = 6371.0;

        // Convert latitude and longitude from degrees to radians
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }
    public static double calculateDistanceBetweenPorts(String portOneId, String portTwoId){
        LineFilters filters = new LineFilters();
        filters.addFilter(1,portOneId,FiltersType.INCLUDE);

        String portOneLine = LinesHandler.getLinesFromDatabase(portsFilePath, filters).get(0);

        filters = new LineFilters();
        filters.addFilter(1,portTwoId,FiltersType.INCLUDE);
        String portTwoLine = LinesHandler.getLinesFromDatabase(portsFilePath, filters).get(0);

        String[] portOneData = portOneLine.split(",");
        String[] portTwoData = portTwoLine.split(",");

        double portOneLatitude = Double.parseDouble(portOneData[4]);
        double portOneLongitude = Double.parseDouble(portOneData[5]);

        double portTwoLatitude = Double.parseDouble(portTwoData[4]);
        double portTwoLongitude = Double.parseDouble(portTwoData[5]);

        return haversine(portOneLatitude, portOneLongitude, portTwoLatitude, portTwoLongitude);
    }
    public static double getRemainingCapacity(String portId){
        LineFilters filters = new LineFilters();
        filters.addFilter(1,portId,FiltersType.INCLUDE);
        String portLine = LinesHandler.getLinesFromDatabase(portsFilePath, filters).get(0);
        String[] portData = portLine.split(",");

        filters = new LineFilters();
        filters.addFilter(4,portId,FiltersType.INCLUDE);
        ArrayList<String> containersLine = LinesHandler.getLinesFromDatabase(PMContainer.containersFilePath, filters);

        double containersWeight = 0;

        for(String line: containersLine){
            String[] parts = line.split(",");
            containersWeight = containersWeight + Tools.stringWeightToDouble(parts[1]);
        }

        double capacity = Double.parseDouble(portData[2].replaceAll(regexCapacity, ""));

        return capacity  - containersWeight;
    }
    public static boolean canStoreContainer(String portId, double containerWeight){
        LineFilters filters = new LineFilters();

        filters.addFilter(1,portId,FiltersType.INCLUDE);
        String portLine = LinesHandler.getLinesFromDatabase(portsFilePath, filters).get(0);
        String[] portData = portLine.split(",");

        filters = new LineFilters();
        filters.addFilter(4,portId,FiltersType.INCLUDE);
        ArrayList<String> containersLine = LinesHandler.getLinesFromDatabase(PMContainer.containersFilePath, filters);

        double containersWeight = containerWeight;

        for(String line: containersLine){
            String[] parts = line.split(",");
            containersWeight = containersWeight + Tools.stringWeightToDouble(parts[1]);
        }

        double capacity = Tools.stringWeightToDouble(portData[2]);

        return containersWeight < capacity;
    }
    public static OptionsInterface createOptionsInterfaceForPortsLandingTypes(String title){
        OptionsInterface landingTypesInterface = new OptionsInterface("askQuestion",title,2);
        landingTypesInterface.addOption(1,"Truck Availability",null,null);
        landingTypesInterface.addOption(2,"Unavailability",null, null);

       return landingTypesInterface;
    }
    public static OptionsInterface createOptionsInterfaceForPorts(String name, LineFilters lineFilters){
        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(portsFilePath,lineFilters);

        OptionsInterface portsInterface = new OptionsInterface("portsInterface",name, 3);

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
    public static TableInterface createTableFromDatabase(LineFilters lineFilters){
        TableInterface table = new TableInterface("ports","Ports",portsCols,",");

        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(portsFilePath, lineFilters);

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
        LineFilters lineFilters = new LineFilters();
        lineFilters.addFilter(1,  this.id,FiltersType.INCLUDE);

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
                LineFilters filters = new LineFilters();

                filters.addFilter(1,this.id,FiltersType.INCLUDE);
                boolean success = LinesHandler.updateLinesFromDatabase(portsFilePath, toString(), filters);

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
                    TableInterface table = createTableFromDatabase(null);
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
            case "Unload a container": {
                PMVehicle.unloadContainter(this.id);
                break;
            }
            case "Load a container": {
                PMVehicle.loadContainer(this.id);
                break;
            }
            case "Update a vehicle from the port": {
                PMVehicle.updateVehicleFromDatabase(this.id);
                break;
            }
            case "Display all vehicles from the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    LineFilters filters = new LineFilters();
                    filters.addFilter(PMVehicle.colCurrentPortId,this.id,FiltersType.INCLUDE);

                    TableInterface table = PMVehicle.createTableFromDatabase(filters);
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
                    LineFilters lineFilters = new LineFilters();
                    lineFilters.addFilter(4, this.id,FiltersType.INCLUDE);

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