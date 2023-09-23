package Trip;

import Container.PMContainer;
import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
import Port.PMPort;
import Tools.Tools;
import Vehicle.PMVehicle;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class PMTrip {
    String[] tripsCols = {"Vehicle ID","Date Depart","Date Arrived","Depart Port","Arrived Port","Status"};
    public static final int colVehicleId = 1;
    public static final int colDateDepart = 2;
    public static final int colDateArrived = 3;
    public static final int colDepartPort = 4;
    public static final int colArrivedPort = 5;
    public static final int status = 6;
    public static final String[] tripCols = {"Vehicle ID", "Date Depart", "Date Arrived", "Depart Port", "Arrived Port", "Status"};
    public static final String tripsFilePath = "./src/database/PMtrips.txt";
    public static OptionsInterface createOptionsInterfaceForTrips(String name, LineFilters lineFilters) {
        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(tripsFilePath,lineFilters);

        int width = Math.min(lines.size() + 1, 5);

        OptionsInterface tripsInterface = new OptionsInterface("tripsInterface", name, width);

        int i = 1;

        for(String line: lines){
            String[] parts = line.split(",");

            String optionName = parts[colVehicleId-1].trim();

            tripsInterface.addOption(i, optionName, line, null);
            i++;
        }

        tripsInterface.addOption(i, "Return", null, null);

        return tripsInterface ;
    }
    public static TableInterface createTableFromDatabase(LineFilters filters) {
        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(tripsFilePath, filters);
        return LinesHandler.createTableFromLines(lines,"trips","Trips",tripCols,",");
    }
    private static String getCurrentDate() {
        // Get the current date in the desired format (e.g., "yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.format(formatter);
    }
    private static boolean checkPortLandingCapacity(String portA, String portB) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/database/PMports.txt"))) {
            String line;
            int capacityPortA = 0;
            int capacityPortB = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equals(portA)) {
                    // Read and store the landing capacity of Port A
                    String capacityStr = parts[2].trim();
                    if (capacityStr.endsWith("Kg")) {
                        capacityPortA = Integer.parseInt(capacityStr.replaceAll("[^0-9]", ""));
                    }
                }
                if (parts.length >= 1 && parts[0].trim().equals(portB)) {
                    // Read and store the landing capacity of Port B
                    String capacityStr = parts[2].trim();
                    if (capacityStr.endsWith("Kg")) {
                        capacityPortB = Integer.parseInt(capacityStr.replaceAll("[^0-9]", ""));
                    }
                }
            }

            // Check if the combined capacity of both ports is sufficient
            int requiredCapacity = 1000; // Adjust this based on your requirements

            return (capacityPortA + capacityPortB) >= requiredCapacity;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Error occurred or not found
    }
    public static double calculateFuelConsumption(String[] vehicleParts, double distance, ArrayList<String> containersLines){
        String type;

        if(vehicleParts[0].matches(PMVehicle.regexTruckId)){
            type = "vehicle";
        }else{
            type = "ship";
        }

        double dryStorage;
        double openTop;
        double openSide;
        double refrigerated;
        double liquid;

        if(type.equals("ship")){
            dryStorage = 3.5;
            openTop = 2.8;
            openSide = 2.7;
            refrigerated = 4.5;
            liquid = 4.8;
        }else{
            dryStorage = 4.6;
            openTop = 3.2;
            openSide = 3.2;
            refrigerated = 5.4;
            liquid = 5.3;
        }

        double fuelConsumption = 0;

        double vehicleCurrentFuel = Tools.stringGallonToDouble(vehicleParts[PMVehicle.colCurrentFuel-1]);
        double vehicleFuelTankCapacity = Tools.stringGallonToDouble(vehicleParts[PMVehicle.colFuelCapacity-1]);

        for(String line : containersLines){
            double rate = 0;

            String[] containerParts = line.split(",");

            String containerType = containerParts[PMContainer.colType-1];
            double containerWeight = Tools.stringWeightToDouble(containerParts[PMContainer.colWeight-1]);

            switch (containerType){
                case "Open Top", "openTop":{
                    rate = openTop;
                    break;
                }
                case "Refrigerated, refrigerated":{
                    rate = refrigerated;
                    break;
                }
                case "Dry Storage, dryStorage":{
                    rate = dryStorage;
                    break;
                }
                case "Open side, openSide":{
                    rate = openSide;
                    break;
                }
                case "Liquid, liquid":{
                    rate = liquid;
                    break;
                }
            }

           fuelConsumption = fuelConsumption + (distance * (rate + containerWeight));
        }

        return fuelConsumption;
    }
    public static boolean hasEnoughFuel(String[] vehicleParts, double distance, ArrayList<String> containersLines){
        String type;

        if(vehicleParts[PMVehicle.colId-1].matches(PMVehicle.regexTruckId)){
            type = "vehicle";
        }else{
            type = "ship";
        }

        double dryStorage;
        double openTop;
        double openSide;
        double refrigerated;
        double liquid;

        if(type.equals("ship")){
            dryStorage = 3.5;
            openTop = 2.8;
            openSide = 2.7;
            refrigerated = 4.5;
            liquid = 4.8;
        }else{
            dryStorage = 4.6;
            openTop = 3.2;
            openSide = 3.2;
            refrigerated = 5.4;
            liquid = 5.3;
        }

        double fuelConsumption = 0.0;

        double vehicleCurrentFuel = Tools.stringGallonToDouble(vehicleParts[PMVehicle.colCurrentFuel-1]);

        for(String line : containersLines){
            double rate = 0;

            String[] containerParts = line.split(",");

            String containerType = containerParts[PMContainer.colType-1];
            double containerWeight = Tools.stringWeightToDouble(containerParts[PMContainer.colWeight-1]);

            switch (containerType){
                case "Open Top", "openTop":{
                    rate = openTop;
                    break;
                }
                case "Refrigerated, refrigerated":{
                    rate = refrigerated;
                    break;
                }
                case "Dry Storage, dryStorage":{
                    rate = dryStorage;
                    break;
                }
                case "Open side, openSide":{
                    rate = openSide;
                    break;
                }
                case "Liquid, liquid":{
                    rate = liquid;
                    break;
                }
            }

            fuelConsumption = fuelConsumption + (distance * (rate + containerWeight));
        }

        return fuelConsumption <= vehicleCurrentFuel;
    }
    public static double calculateContainersWeight(ArrayList<String> lines){
        double weight = 0;

        for(String line: lines){
            String[] containerParts = line.split(",");
            weight = weight +  Double.parseDouble(containerParts[1].replaceAll("(?i)kg",""));
        }

        return weight;
    }
    public static void createATrip(PMPort port){
        LineFilters filters = new LineFilters();
        filters.addFilter(1, port.getId(),FiltersType.EXCLUDE);

        TableInterface portsTable = PMPort.createTableFromDatabase(filters);

        System.out.println(portsTable);

        OptionsInterface portsInterface = PMPort.createOptionsInterfaceForPorts("Choose a arrival port for this trip", filters);

        HashMap<String, String> interfaceData = portsInterface.run(null);

        String portOption = interfaceData.get("option");

        if(!portOption.equals("Return")){
            String arrivalPortLine = interfaceData.get("data");
            String[] arrivalPortParts = arrivalPortLine.split(",");
            String arrivalPortId = arrivalPortParts[PMPort.colId-1];

            boolean canTruckLand;

            canTruckLand = arrivalPortParts[PMPort.colLandingAbility - 1].equals("Truck Available");

            filters = new LineFilters();
            filters.addFilter(PMVehicle.colCurrentPortId,port.getId(),FiltersType.INCLUDE);

            System.out.println(PMVehicle.createTableFromDatabase(filters));

            OptionsInterface vehiclesInterface = PMVehicle.createOptionsInterfaceForVehicles("Choose a vehicle for the trip",filters);

            interfaceData = vehiclesInterface.run(null);

            String vehicleOption = interfaceData.get("option");

            if(!vehicleOption.equals("Return")){
                String vehicleLine = interfaceData.get("data");
                String[] vehicleParts = vehicleLine.split(",");

                String vehicleId = vehicleParts[PMVehicle.colId-1];

                boolean canUseVehicle = false;
                if(vehicleId.matches(PMVehicle.regexTruckId)){
                    if(canTruckLand){
                        canUseVehicle = true;
                    }
                }else{
                    canUseVehicle = true;
                }

                if(canUseVehicle){
                    double portsDistance = PMPort.calculateDistanceBetweenPorts(port.getId(),arrivalPortId);

                    ArrayList<String> containersLine = PMContainer.getContainersFromVehicle(vehicleId);

                    if(containersLine.isEmpty()){
                        System.out.println("Please load at least one container on the vehicle before starting a trip!");
                    }else{
                        if(hasEnoughFuel(vehicleParts,portsDistance,containersLine)){
                            String date = getCurrentDate();
                            String line = vehicleId + "," + date + "," + "null" + "," + port.getId() + "," + arrivalPortId +"," + "Moving";
                            LinesHandler.addLineToDatabase(tripsFilePath, line);

                            vehicleParts[PMVehicle.colCurrentPortId - 1] = "null";

                            filters = new LineFilters();
                            filters.addFilter(PMVehicle.colId,vehicleId,FiltersType.INCLUDE);
                            LinesHandler.updateLinesFromDatabase(PMVehicle.vehiclesFilePath, String.join(",", vehicleParts),filters);

                            System.out.println("Created a trip for " + vehicleId + " going to " + arrivalPortId);
                        }else{
                            System.out.println("Can't go on trip because the vehicle doesn't have enough fuel!");
                        }
                    }
                }else{
                    System.out.println("Arrival port " + arrivalPortId + " can't be landed by trucks!");
                }
            }
        }
        /*
        PromptsInterface questionInterface = new PromptsInterface("askQuestion","Ports input");
        questionInterface.addPrompt("Enter departure port: ");
        questionInterface.addPrompt("Enter arrival port: ");

        TableInterface table = PMPort.createTableFromDatabase();

        System.out.println(table);

        HashMap<Number, String> results = questionInterface.startPrompts();
        questionInterface.clearPrompt();

        String departurePort = results.get(1);
        String arrivalPort = results.get(2);
        if(PortManagerMenu.checkLineHasId(PMPort.portsFilePath,departurePort, )
                && PortManagerMenu.checkLineHasId(PMPort.portsFilePath,arrivalPort)){

            OptionsInterface vehiclesMenu = new OptionsInterface("vehiclesMenu", "Choose a vehicle to add on a trip", 4);

            table = PMVehicle.createTableFromDatabase(port.getId());
            System.out.println(table);

        };

         */
        // Display menu need input 2 ports, vehicle
        // check if vehicle has atleast 1 container, fuel
        // check ports landing, capacity
        // add Trip to database
        //PortManagerMenu.addLineToDatabase(path,line);
    }
    public static void completeTrip(PMPort port){
       LineFilters filters = new LineFilters();
       filters.addFilter(colArrivedPort,port.getId(),FiltersType.INCLUDE);

       System.out.println(createTableFromDatabase(filters));
        OptionsInterface tripsInterface = createOptionsInterfaceForTrips("Choose a vehicle", filters);

        HashMap<String, String> interfaceData = tripsInterface.run(null);

        String option = interfaceData.get("option");

        if(!option.equals("Return")){
            String tripLine = interfaceData.get("data");
            String[] tripParts = tripLine.split(",");

            String vehicleId = tripParts[colVehicleId-1];
            filters = new LineFilters();
            filters.addFilter(colVehicleId,vehicleId, FiltersType.INCLUDE);
        }
    }
    public static void displayAllTripsFromDatabase(String portId){
        //Create a menu to ask if only display trips from the current port to another port or display all trips from any ports
        //Collect all trips from database while adding each of them to table.
        //println the table

        OptionsInterface questionInterface = new OptionsInterface("askQuestion","Do you want to display all trips or trips fromn port only",2);

        questionInterface.addOption(1,"Yes",null, null);
        questionInterface.addOption(2,"No",null, null);

        HashMap<String, String> interfaceData = questionInterface.run(null);

        String id = interfaceData.get("id");
        String option = interfaceData.get("option");

        //#id , vehicleId, portA, portB, status, date

        TableInterface table = new TableInterface("table","Trips",tripCols,",");

        PromptsInterface askQuestion = new PromptsInterface("ask","Ask a question");
        askQuestion.addPrompt("Enter username");
        askQuestion.addPrompt("Enter password");

        HashMap<Number, String> results = askQuestion.startPrompts();
        String username = results.get(0);
        String password = results.get(1);

        //table.addRow();
        //System.out.println(table);
        //"a,b,c,e,f,h"
        //PortManagerMenu.addLineToDatabase();
        //PortManagerMenu.updateLinesWithId();
        //PortManagerMenu.deleteLinesWithId();
        //PortManagerMenu.lineHasId();

        switch (option){
            case "Yes":{

                break;
            }
            case "No":{

                break;
            }
            case "Return":{

            }
        }
    }
}
