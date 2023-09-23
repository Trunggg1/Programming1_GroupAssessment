package Vehicle;

import Container.PMContainer;
import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
import Port.PMPort;
import Tools.Tools;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PMVehicle {
    public static final String[] vehiclesCols = {"ID","Name","Carrying Capacity","Current Fuel","Fuel Capacity","Current Port ID"};
    public static final int colId = 1;
    public static final int colName = 2;
    public static final int colCarryingCapacity = 3;
    public static final int colCurrentFuel = 4;
    public static final int colFuelCapacity = 5;
    public static final int colCurrentPortId = 6;
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
            filters.addFilter(colCurrentPortId,portId, FiltersType.INCLUDE);

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
                filters.addFilter(PMContainer.colPortId,portId, FiltersType.INCLUDE);
                filters.addFilter(PMContainer.colVehicleId,"null", FiltersType.INCLUDE);

                TableInterface containersTable = PMContainer.createTableFromDatabase(filters);
                OptionsInterface containersInterface = PMContainer.createOptionsInterfaceForContainers("Which container you want to load?", filters);

                System.out.println(containersTable);
                HashMap<String, String> interfaceData = containersInterface.run(null);

                String option = interfaceData.get("option");

                if(!option.equals("Return")){
                    String containerLine = interfaceData.get("data");
                    String[] containersPart = containerLine.split(",");

                    String containerId = containersPart[0];

                    double vehicleMaxCapacity = Tools.stringWeightToDouble(vehicleParts[colCarryingCapacity-1]);
                    double containerWeight = Tools.stringWeightToDouble(containersPart[PMContainer.colWeight-1]);
                    double currentCapacity = getVehicleCurrentCapacity(vehicleId);

                   if(currentCapacity + containerWeight > vehicleMaxCapacity){
                        System.out.println("Failed to load the container on vehicle because it reached max capacity!");
                    }else{
                        containersPart[PMContainer.colVehicleId - 1] = vehicleId;
                        containersPart[PMContainer.colPortId - 1] = "null";

                        String line = String.join(",",containersPart);
                        filters = new LineFilters();
                        filters.addFilter(PMContainer.colId,containerId, FiltersType.INCLUDE);
                        boolean success = LinesHandler.updateLinesFromDatabase(PMContainer.containersFilePath, line, filters);

                        if(success){
                            filters = new LineFilters();
                            filters.addFilter(PMContainer.colVehicleId,vehicleId, FiltersType.INCLUDE);

                            containersTable = PMContainer.createTableFromDatabase(filters);

                            System.out.println(containersTable);
                            System.out.println("Successfully loaded container " + containerId +  " on vehicle " + vehicleId + "!");
                        }
                    }
                }
            }else{
                keepRunning = false;
            }
        }
    }
    public static void unloadContainer(String portId){
        boolean keepRunning = true;

        while (keepRunning){
            LineFilters filters = new LineFilters();
            filters.addFilter(colCurrentPortId,portId, FiltersType.INCLUDE);

            TableInterface vehiclesTable = PMVehicle.createTableFromDatabase(filters);
            OptionsInterface vehiclesInterface = PMVehicle.createOptionsInterfaceForVehicles("Which vehicle you want to unload?", filters);

            System.out.println(vehiclesTable);
            HashMap<String, String> vehicleInterfaceData = vehiclesInterface.run(null);

            String vehicleOption = vehicleInterfaceData.get("option");

            if(!vehicleOption.equals("Return")){
                String vehicleLine = vehicleInterfaceData.get("data");
                String[] vehicleParts = vehicleLine.split(",");

                String vehicleId = vehicleParts[0];

                filters = new LineFilters();
                filters.addFilter(PMContainer.colPortId,"null", FiltersType.INCLUDE);
                filters.addFilter(PMContainer.colVehicleId,vehicleId, FiltersType.INCLUDE);

                TableInterface containersTable = PMContainer.createTableFromDatabase(filters);
                OptionsInterface containersInterface = PMContainer.createOptionsInterfaceForContainers("Which container you want to unload?", filters);

                System.out.println(containersTable);
                HashMap<String, String> interfaceData = containersInterface.run(null);

                String option = interfaceData.get("option");

                if(!option.equals("Return")){
                    String containerLine = interfaceData.get("data");
                    String[] containersPart = containerLine.split(",");

                    String containerId = containersPart[PMContainer.colId-1];
                    double containerWeight =  Tools.stringWeightToDouble(containersPart[PMContainer.colWeight-1]);

                    if(PMPort.canStoreContainer(portId,containerWeight)){
                        containersPart[PMContainer.colPortId-1] = portId;
                        containersPart[PMContainer.colVehicleId-1] = null;

                        String newLine = String.join(",",containersPart);

                        filters = new LineFilters();
                        filters.addFilter(PMContainer.colId,containerId, FiltersType.INCLUDE);

                        LinesHandler.updateLinesFromDatabase(PMContainer.containersFilePath,newLine,filters);

                        System.out.println("Successfully unload container " + containerId + " from " + vehicleId + " to port" + portId + "!");

                        System.out.println(PMContainer.createTableFromDatabase(filters));
                    }else{
                        System.out.println("Failed to unload because port reached weight limit!");
                    }
                }
            }else{
                keepRunning = false;
            }
        }
    }
    public static OptionsInterface createOptionsInterfaceForVehicles(String name, LineFilters lineFilters) {
        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(vehiclesFilePath,lineFilters);

        int width = Math.min(lines.size() + 1, 5);

        OptionsInterface vehiclesInterface = new OptionsInterface("vehiclesInterface", name, width);

        int i = 1;

        for(String line: lines){
            String[] parts = line.split(",");

            String vehicleName = parts[1].trim();
            String vehicleId = parts[0].trim();

            String optionName = vehicleName + "(" + vehicleId + ")";

            vehiclesInterface.addOption(i, optionName, line, null);
            i++;
        }

        vehiclesInterface.addOption(i, "Return", null, null);

        return vehiclesInterface;
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
                            System.out.println("Enter fuel(Example: 50L)");
                            String inputResult = input.nextLine().replaceAll(Tools.regexGallon,"");

                            if(inputResult.matches(Tools.regexDouble)){
                                filters = new LineFilters();
                                filters.addFilter(1, vehicleParts[0], FiltersType.INCLUDE);

                                double currentFuel = Tools.stringGallonToDouble(inputResult);
                                double maxFuel = Tools.stringGallonToDouble(vehicleParts[colFuelCapacity-1]);

                                if(currentFuel <= maxFuel){
                                    vehicleParts[colCurrentFuel-1] = Tools.doubleGallonToString(currentFuel);

                                    String newLine = String.join(",",vehicleParts);
                                    boolean success = LinesHandler.updateLinesFromDatabase(PMVehicle.vehiclesFilePath, newLine, filters);

                                    if(success){
                                        System.out.println("Updated vehicle successfully!");
                                        break;
                                    }else{
                                        System.out.println("Failed to update vehicle!");
                                    }
                                }else{
                                    System.out.println("Fuel has to be lower than " + maxFuel + "L");
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
