package Vehicle;

import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PMVehicle {
    public static final String[] vehiclesCols = {"ID","Name","Carrying Capacity","Current Fuel","Fuel Capacity","Current Port ID"};
    public static final String vehiclesFilePath = "./src/database/vehiclesTri.txt";
    private String id;
    private String name;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;

    public static OptionsInterface createOptionsInterfaceForVehicles(String name, LineFilters lineFilters) {
        OptionsInterface containersInterface = new OptionsInterface("vehiclesInterface", name, 5);

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
            filters.addFilter(4,portId, FiltersType.INCLUDE);

            TableInterface table = createTableFromDatabase(filters);

            OptionsInterface vehiclesOption = new OptionsInterface("vehicles","Which vehicle to update?",2);

            ArrayList<String> lines = LinesHandler.getLinesFromDatabase(vehiclesFilePath,filters);

            int count = 1;

            for(String line: lines){
                String[] parts = line.split(",");
                String vehicleId = parts[0].trim();
                String currentPort = parts[5].trim();

                if(currentPort.equals(portId)){
                    vehiclesOption.addOption(count,vehicleId,null,null);
                    count++;
                }
            }

            vehiclesOption.addOption(count,"Return",null,null);

            System.out.println(table);
            HashMap<String, String> interfaceData = vehiclesOption.run(null);

            String option = interfaceData.get("option");

            if(!option.equals("Return")){
                String vehicleId = option;

                OptionsInterface vehicleUpdate = new OptionsInterface("vehicleUpdate","What do you want to update?",2);
                vehicleUpdate.addOption(1,"Current fuel",null,null);
                vehicleUpdate.addOption(2,"Return",null,null);

                interfaceData = vehicleUpdate.run(null);
                option = interfaceData.get("option");

                switch (option){
                    case "Current fuel": {
                    }
                    case "Return":{
                        keepRunning = false;
                        break;
                        //set keepRunning false
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
