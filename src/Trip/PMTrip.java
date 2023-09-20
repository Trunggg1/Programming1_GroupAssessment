package Trip;

import Menu.PortManagerMenu;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.util.HashMap;

public class PMTrip {
    private static final String portsFilePath = "./src/database/trips.txt";
    public static void addTripToDatabase(){
        // Display menu need input 2 ports, vehicle
        // check if vehicle has atleast 1 container, fuel
        // check ports landing, capacity
        // add Trip to database
        //PortManagerMenu.addLineToDatabase(path,line);
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
    public static void updateTripToDatabase(){
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

        questionInterface.addOption(1,"Yes", null);
        questionInterface.addOption(2,"No", null);

        HashMap<String, String> interfaceData = questionInterface.run(null);

        String id = interfaceData.get("id");
        String option = interfaceData.get("option");

        //#id , vehicleId, portA, portB, status, date
        String[] cols = {"Id", "Vehicle Id", "Port A", "Port B", "Status", "Date"};
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
