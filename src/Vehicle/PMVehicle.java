package Vehicle;

import Container.PMContainer;
import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PMVehicle {
    public static final String[] vehiclesCols = {"ID","Name","Carrying Capacity","Current Fuel","Fuel Capacity","Current Port ID"};
    public static final String vehiclesFilePath = "./src/database/PMvehicles.txt";
    private String id;
    private String name;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;
    public static double getVehicleCurrentCapacity(String vehicleId){
        ArrayList<String> lines = PMContainer.getContainersFromVehicle(vehicleId);

        double weight = 0;
        for(String line: lines){
            String[] parts = line.split(",");

            double containerWeight = Double.parseDouble(parts[1].replaceAll("(?i)[kK]g",""));
            weight = weight + containerWeight;
        }

        return weight;
    }
    public static void loadContainer(String portId){
        boolean keepRunning = true;

        while (keepRunning){
            LineFilters filters = new LineFilters();
            filters.addFilter(6,portId, FiltersType.INCLUDE);

            TableInterface vehiclesTable = PMVehicle.createTableFromDatabase(filters);
            OptionsInterface vehiclesInterface = PMVehicle.createOptionsInterfaceForVehicles("Which vehicle you want to load on?", filters);

            System.out.println(vehiclesTable);
            HashMap<String, String> vehicleInterfaceData = vehiclesInterface.run(null);

            String vehicleOption = vehicleInterfaceData.get("option");

            if(!vehicleOption.equals("Return")){
                String vehicleLine = vehicleInterfaceData.get("data");
                String[] vehicleParts = vehicleLine.split(",");

                String vehicleId = vehicleParts[0];

                filters = new LineFilters();
                filters.addFilter(4,portId, FiltersType.INCLUDE);
                filters.addFilter(5,"null", FiltersType.INCLUDE);

                TableInterface containersTable = PMContainer.createTableFromDatabase(filters);
                OptionsInterface containersInterface = PMContainer.createOptionsInterfaceForContainers("Which container you want to load?", filters);

                System.out.println(containersTable);
                HashMap<String, String> interfaceData = containersInterface.run(null);

                String option = interfaceData.get("option");

                if(!option.equals("Return")){
                    String containerLine = interfaceData.get("data");
                    String[] containersPart = containerLine.split(",");

                    String containerId = containersPart[0];

                    double vehicleMaxCapacity = Double.parseDouble(vehicleParts[2].replaceAll("(?i)[kK]g",""));
                    double containerWeight = Double.parseDouble(containersPart[1].replaceAll("(?i)[kK]g",""));
                    double currentCapacity = getVehicleCurrentCapacity(vehicleId);

                    System.out.println(vehicleMaxCapacity + "  " + containerWeight + "  " + currentCapacity);
                    if(currentCapacity + containerWeight > vehicleMaxCapacity){
                        System.out.println("Failed to load the container on vehicle because it reached max capacity!");
                    }else{
                        containersPart[4] = vehicleId;
                        String line = String.join(",",containersPart);
                        filters = new LineFilters();
                        filters.addFilter(1,containerId, FiltersType.INCLUDE);
                        boolean success = LinesHandler.updateLinesFromDatabase(PMContainer.containersFilePath, line, filters);

                        if(success){
                            System.out.println("Successfully load container " + containerId +  "on vehicle " + vehicleId + "!");
                            keepRunning = false;
                        }
                    }
                }
            }else{
                keepRunning = false;
            }
        }
        /*
        while (keepRunning){
            LineFilters filters = new LineFilters();
            filters.addFilter(4,portId, FiltersType.INCLUDE);
            filters.addFilter(5,vehicleId, FiltersType.INCLUDE);

            TableInterface containersTable = PMContainer.createTableFromDatabase(filters);
            OptionsInterface containersInterface = PMContainer.createOptionsInterfaceForContainers("Which container you want to load?", filters);

            System.out.println(containersTable);
            HashMap<String, String> interfaceData = containersInterface.run(null);

            String option = interfaceData.get("option");

            if(!option.equals("Return")){
                String containerLine = interfaceData.get("data");
                String[] containersPart = containerLine.split(",");

                String containerId = containersPart[0];

                filters = new LineFilters();
                filters.addFilter(6,portId, FiltersType.INCLUDE);
                OptionsInterface vehiclesInterface = PMVehicle.createOptionsInterfaceForVehicles("Which vehicle you want to load on?", filters);

                HashMap<String, String> vehicleInterfaceData = vehiclesInterface.run(null);

                String vehicleOption = vehicleInterfaceData.get("option");

                if(!vehicleOption.equals("Return")){
                    String vehicleLine = vehicleInterfaceData.get("data");

                    String[] vehicleParts = vehicleLine.split(",");

                    String vehicleId = vehicleParts[0];

                    double vehicleMaxCapacity = Double.parseDouble(vehicleParts[2].replaceAll("(?i)[kK]g",""));
                    double containerWeight = Double.parseDouble(containersPart[1].replaceAll("(?i)[kK]g",""));
                    double currentCapacity = getVehicleCurrentCapacity(vehicleId);

                    if(currentCapacity + containerWeight > vehicleMaxCapacity){
                        System.out.println("Failed to load the container on vehicle because it reached max capacity!");
                    }else{
                        containersPart[4] = vehicleId;
                        String line = String.join(",",containersPart);
                        filters = new LineFilters();
                        filters.addFilter(1,containerId, FiltersType.INCLUDE);
                        LinesHandler.updateLinesFromDatabase(PMContainer.containersFilePath,line,filters);
                    }
                }
            }else{
                keepRunning = false;
            }


        }
*/
    }
    public static void unloadContainter(){
    }
    public static OptionsInterface createOptionsInterfaceForVehicles(String name, LineFilters lineFilters) {
        OptionsInterface containersInterface = new OptionsInterface("vehiclesInterface", name, 3);

        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(vehiclesFilePath,lineFilters);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            String vehicleName = parts[1].trim();
            String vehicleId = parts[0].trim();

            String optionName = vehicleName + "(" + vehicleId + ")";

            containersInterface.addOption(i + 1, optionName, line, null);

            if (i == lines.size() - 1) {
                containersInterface.addOption(i + 1, optionName, line, null);
                containersInterface.addOption(i + 2, "Return", null, null);
            }
        }

        return containersInterface;
    }
    public static TableInterface createTableFromDatabase(LineFilters lineFilters){
        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(vehiclesFilePath,lineFilters);
        return LinesHandler.createTableFromLines(lines,"vehicles","Vehicles",vehiclesCols,",");
    }
    public static void updateVehicleFromDatabase(String portId){
        boolean keepRunning = true;

        while (keepRunning){
            LineFilters filters = new LineFilters();
            filters.addFilter(6,portId, FiltersType.INCLUDE);

            TableInterface table = createTableFromDatabase(filters);

            OptionsInterface vehiclesInterface = PMVehicle.createOptionsInterfaceForVehicles("Which vehicle to update?", filters);

            System.out.println(table);

            HashMap<String, String> interfaceData = vehiclesInterface.run(null);

            String option = interfaceData.get("option");

            if(!option.equals("Return")){
                String vehicleLine = interfaceData.get("data");
                String[] vehicleParts = vehicleLine.split(",");

                OptionsInterface vehicleUpdate = new OptionsInterface("vehicleUpdate","What do you want to update?",2);
                vehicleUpdate.addOption(1,"Current fuel",null,null);
                vehicleUpdate.addOption(2,"Return",null,null);

                interfaceData = vehicleUpdate.run(null);
                option = interfaceData.get("option");

                switch (option){
                    case "Current fuel": {
                        while (true){
                            Scanner input = new Scanner(System.in);
                            System.out.println("Enter fuel(Example 50L): ");
                            String inputResult = input.nextLine();

                            System.out.println(inputResult + " " + inputResult.matches("\\d+[lL]"));
                            if(inputResult.matches("\\d+[lL]")){
                                filters = new LineFilters();
                                filters.addFilter(1, vehicleParts[0], FiltersType.INCLUDE);

                                double currentFuel = Double.parseDouble(inputResult.replaceAll("[lL]",""));
                                double maxFuel = Double.parseDouble(vehicleParts[4].replaceAll("[lL]",""));

                                if(currentFuel <= maxFuel){
                                    vehicleParts[3] = inputResult;

                                    String newLine = String.join(",",vehicleParts);
                                    boolean success = LinesHandler.updateLinesFromDatabase(PMVehicle.vehiclesFilePath, newLine, filters);

                                    if(success){
                                        System.out.println("Updated vehicle successfully!");
                                        break;
                                    }else{
                                        System.out.println("Failed to update vehicle!");
                                    }
                                }
                            }else{
                                System.out.println("Please enter correct format: 50L");
                            }
                        }
                    }
                    case "Return":{
                        keepRunning = false;
                        break;
                    }
                }
            }else{
                keepRunning = false;
            }
        }
    }

    public PMVehicle(String id, String name, String carryingCapacity, String currentFuel, String fuelCapacity, String currentPort) {
        this.id = id;
        this.name = name;
        this.carryingCapacity = carryingCapacity;
        this.currentFuel = currentFuel;
        this.fuelCapacity = fuelCapacity;
        this.currentPort = currentPort;
    }
}
