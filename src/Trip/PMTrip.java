package Trip;

import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import Port.PMPort;
import Vehicle.PMVehicle;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
public class PMTrip {
    public static final String[] cols = {"ID", "Date Depart", "Date Arrived", "Depart Port", "Arrived Port", "Status"};
    public static final String tripsFilePath = "./src/database/tripsTri.txt";
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

            filters = new LineFilters();
            filters.addFilter(4,port.getId(),FiltersType.INCLUDE);

            TableInterface vehiclesTable = PMVehicle.createTableFromDatabase(filters);
            System.out.println(vehiclesTable);

            OptionsInterface vehiclesInterface = PMVehicle.createOptionsInterfaceForVehicles("Choose a vehicle for a trip",filters);

            interfaceData = vehiclesInterface.run(null);

            String vehicleOption = interfaceData.get("option");

            if(!vehicleOption.equals("Return")){
                String vehicleLine = interfaceData.get("data");
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
