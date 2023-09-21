package Vehicle;

import Menu.PortManagerMenu;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PMVehicle {
    public static final String[] containersCols = {"ID","Name","Carrying Capacity","Current Fuel","Fuel Capacity","Current Port"};
    public static final String containersFilePath = "./src/database/vehiclesTri.txt";
    private String id;
    private String name;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;
    public static TableInterface createTableFromDatabase(String portId){
        TableInterface table = new TableInterface("vehicles","Vehicles",containersCols,",");

        ArrayList<String> lines = PortManagerMenu.getLinesFromDatabase(containersFilePath);

        for (String line : lines) {
            String[] parts = line.split(",");

            if (portId != null) {
                String id = parts[5].trim();
                String trimmedPortID = portId.trim();

                if (trimmedPortID.equals(id)) {
                    table.addRow(line);
                }
            } else {
                table.addRow(line);
            }
        }

        return table;
    }
    public static void updateVehicleFromDatabase(String portId){
        boolean keepRunning = true;

        while (keepRunning){
            TableInterface table = createTableFromDatabase(portId);

            OptionsInterface vehiclesOption = new OptionsInterface("vehicles","Which vehicle to update?",2);

            ArrayList<String> lines = PortManagerMenu.getLinesFromDatabase(containersFilePath);

            int count = 1;

            for(String line: lines){
                String[] parts = line.split(",");
                String vehicleId = parts[0].trim();
                String currentPort = parts[5].trim();

                if(currentPort.equals(portId)){
                    vehiclesOption.addOption(count,vehicleId,null);
                    count++;
                }
            }

            vehiclesOption.addOption(count,"Return",null);

            System.out.println(table);
            HashMap<String, String> interfaceData = vehiclesOption.run(null);

            String option = interfaceData.get("option");

            if(!option.equals("Return")){
                String vehicleId = option;

                OptionsInterface vehicleUpdate = new OptionsInterface("vehicleUpdate","What do you want to update?",2);
                vehicleUpdate.addOption(1,"Current fuel",null);
                vehicleUpdate.addOption(2,"Return",null);

                interfaceData = vehicleUpdate.run(null);
                option = interfaceData.get("option");

                switch (option){
                    case "Current fuel":{
                        //Ask input
                        //Update
                        break;
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
