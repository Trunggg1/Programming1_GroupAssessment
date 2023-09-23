package Trip;

import Container.PMContainer;
import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import Port.PMPort;
import Vehicle.PMVehicle;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class PMTrip {
    private static String getCurrentDate() {
        // Get the current date in the desired format (e.g., "yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(formatter);
    }
    public static final String[] cols = {"ID", "Date Depart", "Date Arrived", "Depart Port", "Arrived Port", "Status"};
    public static final String tripsFilePath = "./src/database/PMtrips.txt";
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

        if(vehicleParts[0].matches("btr-\\d+")){
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

        double value = 0;

        for(String line : containersLines){
            String[] containerParts = line.split(",");

            String containerType = containerParts[2];
            double containerWeight = Double.parseDouble(containerParts[1].replaceAll("(?i)kg",""));

            double rate = 0;

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

            System.out.println(distance + "   " + containerWeight + "    " + rate);
            System.out.println(distance * containerWeight * rate);

            value = value + (distance * containerWeight * rate);
        }

        System.out.println(value);
        return value;
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

            filters = new LineFilters();
            filters.addFilter(PMVehicle.colCurrentPortId,port.getId(),FiltersType.INCLUDE);

            System.out.println(PMVehicle.createTableFromDatabase(filters));

            OptionsInterface vehiclesInterface = PMVehicle.createOptionsInterfaceForVehicles("Choose a vehicle for the trip",filters);

            interfaceData = vehiclesInterface.run(null);

            String vehicleOption = interfaceData.get("option");

            if(!vehicleOption.equals("Return")){
                String vehicleLine = interfaceData.get("data");
                String[] vehicleParts = vehicleLine.split(",");

                double arrivalPortRemainingCapacity = PMPort.getRemainingCapacity(arrivalPortParts[PMPort.colId-1]);
                double portsDistance = PMPort.calculateDistanceBetweenPorts(port.getId(),arrivalPortParts[PMPort.colId-1]);

                ArrayList<String> containersLine = PMContainer.getContainersFromVehicle(vehicleParts[0]);

                if(containersLine.isEmpty()){
                    System.out.println("Please load at least one container on the vehicle before starting a trip!");
                }else{
                    double containersWeights = calculateContainersWeight(containersLine);
                    double consumption = calculateFuelConsumption(vehicleParts,portsDistance,containersLine);
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
    public static TableInterface createTableFromDatabase(String portName) {
        Scanner fileData;

        try{
            fileData = new Scanner(new File(tripsFilePath));
        }catch (Exception e){
            fileData = null;
        }

        String[] containersCols = {"Id","Date Depart","Date Arrived","Depart Port","Arrived Port","Status"};
        TableInterface table = new TableInterface("trips","Trips",containersCols,",");

        int count = 1;
        if(fileData!= null){
            while (fileData.hasNext()){
                String line = fileData.nextLine();
                String[] parts = line.split(",");

                if(count != 1){
                    if(portName!= null){
                        String name = parts[3].trim();
                        String trimmedId = name.trim();

                        if(trimmedId.equals(portName)){
                            table.addRow(line);
                        }
                    }else{
                        System.out.println(line);
                        table.addRow(line);
                    }
                }

                count++;
            }
        }

        return table;
    }
    public static void completeTrip(){

        //Collect all trips from database that has status on going and load as a table
        //Display menu and ask to choose a option
        //vehicleId, portsId
        //Get vehicle from Vehicle Id, get 2 ports from ports ID
        //Fuel - consumption
        //Vehicle port B
        //Containers update portID to PortID B
        //Trip status set to success
        //7d
    }
    public static void updateTripFromPort(){

    }
    public static void deleteTripToDatabase(){
        //Collect all trips from database that has status on going and load as a table
        //Display menu and ask to choose an option
        //Find line that has id same with the option
        //Delete trip
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

        TableInterface table = new TableInterface("table","Trips",cols,",");

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
