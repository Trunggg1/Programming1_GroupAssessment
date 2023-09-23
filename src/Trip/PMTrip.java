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
    public static final int colVehicleId = 1;
    public static final int colDateDepart = 2;
    public static final int colDateArrived = 3;
    public static final int colDepartPort = 4;
    public static final int colArrivedPort = 5;
    public static final int colStatus = 6;
    public static final String[] tripCols = {"Vehicle ID","Date Depart","Date Arrived","Depart Port","Arrived Port","Status"};
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

        return fuelConsumption * 4.546092;
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
                double vehicleCurrentFuel = Tools.stringGallonToDouble(vehicleParts[PMVehicle.colCurrentFuel -1]);

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
                        double consumption = calculateFuelConsumption(vehicleParts,portsDistance,containersLine);

                        if(consumption <= vehicleCurrentFuel){
                            String date = getCurrentDate();
                            String line = vehicleId + "," + date + "," + "null" + "," + port.getId() + "," + arrivalPortId +"," + "Moving";
                            LinesHandler.addLineToDatabase(tripsFilePath, line);

                            vehicleParts[PMVehicle.colCurrentPortId - 1] = "null";
                            vehicleParts[PMVehicle.colCurrentFuel-1] = Tools.doubleGallonToString(vehicleCurrentFuel - consumption);

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
    }
    public static void completeTrip(PMPort port){
       LineFilters filters = new LineFilters();
       filters.addFilter(colArrivedPort,port.getId(),FiltersType.INCLUDE);
       filters.addFilter(colStatus,"Landed",FiltersType.EXCLUDE);

       System.out.println(createTableFromDatabase(filters));
       OptionsInterface tripsInterface = createOptionsInterfaceForTrips("Choose a vehicle", filters);

       HashMap<String, String> interfaceData = tripsInterface.run(null);

       String option = interfaceData.get("option");

        if(!option.equals("Return")){
            String tripLine = interfaceData.get("data");
            String[] tripParts = tripLine.split(",");

            String vehicleId = tripParts[colVehicleId-1];
            String dateDepart = tripParts[colDateDepart-1];

            filters = new LineFilters();
            filters.addFilter(colVehicleId,vehicleId, FiltersType.INCLUDE);

            String vehicleLine = LinesHandler.getLinesFromDatabase(PMVehicle.vehiclesFilePath, filters).get(0);
            String[] vehicleParts = vehicleLine.split(",");

            vehicleParts[PMVehicle.colCurrentPortId-1] = port.getId();
            tripParts[colStatus-1] = "Landed";
            tripParts[colDateArrived-1] = getCurrentDate();

            filters = new LineFilters();
            filters.addFilter(colVehicleId,vehicleId, FiltersType.INCLUDE);
            filters.addFilter(colDateDepart,dateDepart, FiltersType.INCLUDE);

            LinesHandler.updateLinesFromDatabase(tripsFilePath,String.join(",",tripParts),filters);

            filters = new LineFilters();
            filters.addFilter(PMVehicle.colId,vehicleId, FiltersType.INCLUDE);
            LinesHandler.updateLinesFromDatabase(PMVehicle.vehiclesFilePath,String.join(",",vehicleParts),filters);

            System.out.println("Completed trip for " + vehicleId + " arrived to " + port.getId());
        }
    }
    public static void displayTripsByGivenDay(){
        OptionsInterface dateMenu = new OptionsInterface("date","dateMenu",2);
    }
    public static void displayTripsByDaysRange(){

    }
}
