package Vehicle;

import Menu.PortManagerMenu;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class PMVehicle {
    private static final String containersFilePath = "./src/database/vehicles.txt";
    private String id;
    private String name;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;
    public static void updateVehiclesFromDatabase(String portName){
        //Get all vehicles from database from a port
        //Create a menu with all options as vehicles
        //Menu ask what do they want to update? Current fuel / Current port
        //Switch case option
        //Update vehicle to database
        //PortManagerMenu.updateLinesWithId();

        /*
        OptionsInterface askInterface = new OptionsInterface("askQuestion","Which option you want to update for vehicle?", 2);
        askInterface.addOption(1,"Current fuel", null);
        askInterface.addOption(2,"Current port", null);

        //#ID, Name, Carrying Capacity, Current Fuel, Fuel Capacity, Current Port
        String[] cols = {"Id", "Name", "Carrying Capacity", "Current Fuel", "Fuel Capacity","Current Port"};
        TableInterface table = new TableInterface("a","Vehicles",cols,",");

        System.out.println(table);

        HashMap<String, String> results = askInterface.run(null);

        String id = results.get("id");
        String option = results.get("option");


        switch (option){
            case "Current fuel":{
                success = PortManagerMenu.updateLinesWithId(containersFilePath, ?, "a, b , c ,d ,e, f");

                PromptsInterface askQuestion = new PromptsInterface("a","Fuel input");
                askQuestion.addPrompt("Enter fuel");
                askQuestion.addPrompt("Enter a");
                askQuestion.addPrompt("Enter b");
                askQuestion.addPrompt("Enter c");

                PortManagerMenu.deleteLinesWithId;
                PortManagerMenu.updateLinesWithId();
                PortManagerMenu.addLineToDatabase();
                PortManagerMenu.lineHasId();
                if(success){
                    //print
                }else{

                }
                break;
            }
            case "Current port":{
                //
                break;
            }
        }
         */
    }
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
                        String portId = parts[5].trim();
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
