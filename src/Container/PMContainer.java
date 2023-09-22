package Container;

import LinesHandler.FiltersType;
import LinesHandler.LineFilters;
import LinesHandler.LinesHandler;
import Port.PMPort;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PMContainer {
    public static final String[] containersCols = {"ID","Weight","Type","Port ID", "Vehicle ID"};
    public static final String containersFilePath = "./src/database/containers.txt";
    private String id;
    private Double weight;
    private String containerType;
    private String portId;
    public static OptionsInterface createOptionInterfaceForContainerTypes(String name){
        OptionsInterface optionInterface = new OptionsInterface("containersType",name, 4);
        optionInterface.addOption(1,"Dry Storage",null,null);
        optionInterface.addOption(2,"Refrigerated",null,null);
        optionInterface.addOption(3,"Open Top",null,null);
        optionInterface.addOption(4,"liquid",null,null);
        optionInterface.addOption(5,"Open Side",null,null);

        return optionInterface;
    }
    public static ArrayList<String> getContainersFromVehicle(String vehicleId){
        LineFilters filers = new LineFilters();
        filers.addFilter(5,vehicleId,FiltersType.INCLUDE);
        return LinesHandler.getLinesFromDatabase(containersFilePath,filers);
    }
    public static TableInterface createTableFromDatabase(LineFilters lineFilters){
        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(containersFilePath, lineFilters);
        return LinesHandler.createTableFromLines(lines, "containersList", "Containers list", containersCols, ",");
    }
    public static OptionsInterface createOptionsInterfaceForContainers(String name, LineFilters lineFilters){
        OptionsInterface containersInterface = new OptionsInterface("containersInterface",name, 5);

        ArrayList<String> lines = LinesHandler.getLinesFromDatabase(containersFilePath, lineFilters);

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            String[] parts = line.split(",");

            String optionName = parts[0].trim() + "(" + parts[1].trim() + ")";

            containersInterface.addOption(i + 1,optionName,line,null);

            if(i == lines.size() - 1){
                containersInterface.addOption(i + 1,optionName,line,null);
                containersInterface.addOption(i + 2,"Return",null,null);
            }
        }

        return containersInterface;
    }
    public static void loadContainerOnVehicle(String portId){
        TableInterface containersTable = createTableFromDatabase(null);
        //TableInterface vehiclesTable = PMVehicle.createTableFromDatabase(portId);


    }
    public static void addContainerToDatabase(String portId) {
        TableInterface table = createTableFromDatabase(null);
        PromptsInterface prompt = new PromptsInterface("containersPrompt","Add container inputs");
        prompt.addPrompt("Enter ID: ");
        prompt.addPrompt("Enter Weight(Example: 5.9)Kg");

        String id;
        String weight;

        System.out.println(table);

        while (true){
            HashMap<Number, String> results = prompt.startPrompts();

            String idPattern = "^c-\\d+$";

            id = results.get(1);
            weight = results.get(2);

            if(!id.matches(idPattern)){
                System.out.println("Id must follow this format: c-00");
            }else if(!weight.matches("^-?\\d+(\\.\\d+)?$")){
                System.out.println("Weight has to be a double value");
            }else{
                break;
            }
        }

        OptionsInterface mainInterface = new OptionsInterface("askQuestion","Add container to this port?", 2);
        mainInterface.addOption(1,"True","true",null);
        mainInterface.addOption(2,"False","false",null);

        HashMap<String, String> interfaceData = mainInterface.run(null);

        boolean addContainerToPort = interfaceData.get("option").equals("True");

        OptionsInterface containerTypesInterface = createOptionInterfaceForContainerTypes("What type of this container?");

        interfaceData = containerTypesInterface.run(null);

        String type = interfaceData.get("option");

        String line;

        if(addContainerToPort){
            //Check port capacity
            line = id + ", " + weight + ", " + type + "," + portId + "," + "null";
        }else{
            line = id + ", " + weight + ", " + type + "," + "null" + "," + "null";
        }

        boolean success = LinesHandler.addLineToDatabase(containersFilePath, line);

        if(success){
            System.out.println("Successfully added container to database!");
            table = createTableFromDatabase(null);

            System.out.println(table);
        }else{
            System.out.println("Failed to add container to database!");
        }
    }
    public static void deleteContainerFromDatabase() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Delete Container Menu:");
            System.out.println("1. Add Container ID to Delete");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            int menuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (menuChoice == 1) {
                // Ask the user to enter the container ID to delete
                System.out.print("Enter the container ID to delete: ");
                String containerIdToDelete = scanner.nextLine();

                // Validate the container ID using a pattern
                Pattern idPattern = Pattern.compile("^c-\\d+$");
                if (!idPattern.matcher(containerIdToDelete).matches()) {
                    System.out.println("Invalid container ID format. Please use 'c-<digits>'.");
                    continue;
                }

                // Use function to delete the container
                LineFilters filters = new LineFilters();
                filters.addFilter(1,containerIdToDelete, FiltersType.INCLUDE);

                boolean deleted = LinesHandler.deleteLinesWithId(containersFilePath, filters);

                if (deleted) {
                    System.out.println("Container with ID " + containerIdToDelete + " deleted successfully.");
                } else {
                    System.out.println("Container with ID " + containerIdToDelete + " not found.");
                }
            } else if (menuChoice == 2) {
                // Exit the delete container menu
                break;
            } else {
                System.out.println("Invalid menu choice. Please choose again.");
            }
        }
    }
    public static void updateContainerFromDatabase(){
        boolean keepRunning = true;

        while (keepRunning){
            TableInterface table = createTableFromDatabase(null);
            OptionsInterface containersInterface = createOptionsInterfaceForContainers("Which container do you want to update?",null);

            System.out.println(table);

            HashMap<String, String> interfaceData = containersInterface.run(null);

            String optionName = interfaceData.get("option");

            if(!optionName.equals("Return")){
                String containerLine = interfaceData.get("data");

                String[] parts = containerLine.split(",");

                OptionsInterface updateInterface = new OptionsInterface("update","What update for the container " + parts[0] + " ?",4);
                updateInterface.addOption(1,"ID",null,null);
                updateInterface.addOption(2,"Weight",null,null);
                updateInterface.addOption(3,"Type",null,null);
                updateInterface.addOption(4,"Port ID",null,null);
                updateInterface.addOption(5,"Return",null,null);

                Scanner input = new Scanner(System.in);

                System.out.println(containerLine);
                interfaceData = updateInterface.run(null);

                switch (interfaceData.get("option")){
                    case "ID":{
                        while (true){
                            System.out.println("Enter ID(c-00): ");

                            String inputResult = input.nextLine().trim();

                            if(inputResult.matches("^c-\\d+$")){
                                parts[0] = inputResult;
                                break;
                            }else{
                                System.out.println("ID must follow format: c-00");

                                System.out.print("Return?(Y/N): ");
                                inputResult = input.nextLine().trim();;

                                if(inputResult.equals("Y") || inputResult.equals("y")){
                                    keepRunning = false;
                                }
                            }
                        }
                        break;
                    }
                    case "Weight":{
                        while (true){
                            System.out.println("Enter weight(Example: 5.9Kg): ");

                            String inputResult = input.nextLine().trim();;

                            if(inputResult.matches("\\d+.\\d+") ){
                                parts[1] = inputResult + "Kg";
                                break;
                            }else if (inputResult.matches("\\d+(\\.\\d+)?Kg")){
                                parts[1] = inputResult;
                                break;
                            }else
                            {
                                System.out.println("Weight must follow format: 0.0");

                                System.out.print("Return?(Y/N): ");
                                inputResult = input.nextLine().trim();;

                                if(inputResult.equals("Y") || inputResult.equals("y")){
                                    keepRunning = false;
                                }
                            }
                        }
                        break;
                    }
                    case "Type":{
                        OptionsInterface containerTypesInterface = createOptionInterfaceForContainerTypes("What type of this container?");

                        interfaceData = containerTypesInterface.run(null);

                        String type = interfaceData.get("option");

                        if(type.equals("Return")){
                            keepRunning = false;
                        }else{
                            parts[2] = type;
                        }

                        break;
                    }
                    case "Port ID":{
                            while (true){
                                LineFilters filters = new LineFilters();
                                filters.addFilter(1,parts[3],FiltersType.EXCLUDE);

                                TableInterface portsTable = PMPort.createTableFromDatabase(filters);

                                System.out.println(portsTable);

                                OptionsInterface portsInterface = PMPort.createOptionsInterfaceForPorts("Which port do you want to set?", filters);

                                interfaceData = portsInterface.run(null);

                                String portLine = interfaceData.get("data");
                                String[] portParts = portLine.split(",");

                                double remainingCapacity = PMPort.getRemainingCapacity(portParts[0]);

                                if(remainingCapacity > 0){
                                    parts[3] = portParts[0];
                                    break;
                                }else{
                                    System.out.println("Failed to update container! port reached maximum capacity");

                                    System.out.print("Return?(Y/N): ");
                                    String inputResult = input.nextLine().trim();;

                                    if(inputResult.equals("Y") || inputResult.equals("y")){
                                        keepRunning = false;
                                    }
                                }
                            }
                        break;
                    }
                    case "Return":{
                        keepRunning = false;
                        break;
                    }
                }

                if(keepRunning){
                    String line = String.join(",", parts);

                    LineFilters lineFilters = new LineFilters();
                    lineFilters.addFilter(1,parts[0],FiltersType.INCLUDE);

                    boolean success = LinesHandler.updateLinesFromDatabase(containersFilePath, line,lineFilters);
                    if(success){
                        System.out.println("Update container " + parts[0] +  " successfully!");

                        table.clearRows();
                        table.addRow(line);
                        System.out.println(table);

                        keepRunning = false;
                    }else{
                        System.out.println("Failed to update container!");
                    }
                }
            }else{
                keepRunning = false;
            }
        }
    }
    public PMContainer(String cNumber, double weight, String containerType, String portId) {
        this.id = cNumber;
        this.weight = weight;
        this.containerType = containerType;
        this.portId = portId;
    }
}