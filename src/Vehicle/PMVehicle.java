package Vehicle;

import Menu.PortManagerMenu;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class PMVehicle {
    public static final String[] vehiclesCols = {"ID","Name","Carrying Capacity","Current Fuel","Fuel Capacity","Current Port ID"};
    public static final String vehiclesFilePath = "./src/database/vehiclesTri.txt";
    private String id;
    private String name;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;

    public static OptionsInterface createOptionsInterfaceForVehicles(String name, String portId, String excludedId) {
        OptionsInterface containersInterface = new OptionsInterface("vehiclesInterface", name, 5);

        ArrayList<String> lines;

        if (portId != null) {
            lines = PortManagerMenu.getLinesFromDatabaseById(vehiclesFilePath, portId, 5);
        } else {
            lines = PortManagerMenu.getLinesFromDatabase(vehiclesFilePath);
        }

        String trimmedExcludedId = "";

        if (excludedId != null) {
            trimmedExcludedId = excludedId.trim();
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            if (!parts[0].trim().equals(trimmedExcludedId)) {
                String vehicleName = parts[1].trim();
                String vehicleId = parts[0].trim();

                String optionName = vehicleName + "(" + vehicleId + ")";

                containersInterface.addOption(i + 1, optionName, line, null);

                if (i == lines.size() - 1) {
                    containersInterface.addOption(i + 1, optionName, line, null);
                    containersInterface.addOption(i + 2, "Return", null, null);
                }
            }
        }

        return containersInterface;
    }
    public static TableInterface createTableFromDatabase(String portId){
        TableInterface table = new TableInterface("vehicles","Vehicles",vehiclesCols,",");

        ArrayList<String> lines = PortManagerMenu.getLinesFromDatabase(vehiclesFilePath);

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

            ArrayList<String> lines = PortManagerMenu.getLinesFromDatabase(vehiclesFilePath);

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
