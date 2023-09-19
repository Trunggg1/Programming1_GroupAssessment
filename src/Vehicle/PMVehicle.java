package Vehicle;

import interfaces.builders.TableInterface;

import java.io.File;
import java.util.Scanner;

public class PMVehicle {
    private static final String containersFilePath = "./src/database/vehicles.txt";
    private String id;
    private String name;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;

    public static TableInterface createTableFromDatabase(String portName){
        Scanner fileData;

        try{
            fileData = new Scanner(new File(containersFilePath));
        }catch (Exception e){
            fileData = null;
        }

        //#ID, Name, Carrying Capacity, Current Fuel, Fuel Capacity, Current Port
        String[] containersCols = {"Id","Name","Carrying Capacity","Current Fuel","Fuel Capacity","Current Port"};
        TableInterface table = new TableInterface("vehicles","Vehicles",containersCols,",");

        int count = 1;
        if(fileData!= null){
            while (fileData.hasNext()){
                String line = fileData.nextLine();
                String[] parts = line.split(",");

                if(count != 1){
                    if(portName!= null){
                        String portId = parts[6].trim();
                        String trimmedId = portName.trim();

                        if(trimmedId.equals(portId)){
                            table.addRow(line);
                        }
                    }else{
                        table.addRow(line);
                    }
                }

                count++;
            }
        }

        return table;
    }
}
