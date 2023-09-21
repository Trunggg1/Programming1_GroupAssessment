package Container;

import Menu.PortManagerMenu;
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

    public static void addContainerToDatabase(String portId) {
        PromptsInterface prompt = new PromptsInterface("containersPrompt","Add container inputs");
        prompt.addPrompt("Enter id");
        prompt.addPrompt("Enter weight");

        String id;
        String weight;
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
        mainInterface.addOption(1,"True",null);
        mainInterface.addOption(2,"False",null);

        HashMap<String, String> interfaceData = mainInterface.run(null);

        boolean addContainerToPort = interfaceData.get("option").equals("True");

        mainInterface = new OptionsInterface("askQuestion","What type of this container?", 2);
        mainInterface.addOption(1,"Dry Storage",null);
        mainInterface.addOption(2,"Refrigerated",null);
        mainInterface.addOption(3,"Open Top",null);
        mainInterface.addOption(4,"liquid",null);
        mainInterface.addOption(5,"Open Side",null);

        interfaceData = mainInterface.run(null);

        String type = interfaceData.get("option");

        String line;

        if(addContainerToPort){
            line = id + ", " + weight + ", " + type + "," + portId;
        }else{
            line = id + ", " + weight + ", " + type + "," + "null";
        }

        boolean success = PortManagerMenu.addLineToDatabase(containersFilePath, line);

        if(success){
            System.out.println("Successfully added container to database!");
        }else{
            System.out.println("Failed to add container to database!");
        }
    }
    public static void deleteContainerFromDatabase() {
        // Create a menu
        // Run while loop with Scanner to add container id as option
        // Run menu and get containerId
        // Use function to delete container
        //PortManagerMenu.deleteLinesWithId()
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
                boolean deleted = PortManagerMenu.deleteLinesWithId(containersFilePath, containerIdToDelete);

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
    public static TableInterface createTableFromDatabase(String portId){
        TableInterface table = new TableInterface("containersData", "Container",containersCols,",");

        ArrayList<String> lines = PortManagerMenu.getLinesFromDatabase(containersFilePath);

        for(String line: lines){
            String[] parts = line.split(",");

            if(portId!= null){
                String linePortId = parts[3].trim();
                String trimmedPortId = portId.trim();

                if(trimmedPortId.equals(linePortId)){
                    table.addRow(line);
                }
            }else{
                table.addRow(line);
            }
        }

        return table;
    }
    public static void updateContainerFromDatabase(String portId){
        boolean keepRunning = true;

        while (keepRunning){
            TableInterface table = createTableFromDatabase(portId);
            OptionsInterface containersInterface = new OptionsInterface("askQuestion","Which container you want to update?", 5);

           ArrayList<String> lines;

            if(portId!=null){
                lines = PortManagerMenu.getLinesFromDatabaseById(containersFilePath,portId,3);
            }else{
                lines = PortManagerMenu.getLinesFromDatabase(containersFilePath);
            }

            for(int i = 0; i < lines.size(); i++){
                String line = lines.get(i);
                String[] parts = line.split(",");

                containersInterface.addOption(i + 1,parts[0],null);

                if(i == lines.size() - 1){
                    containersInterface.addOption(i + 1,parts[0],null);
                    containersInterface.addOption(i + 2,"Return",null);
                }
            }

            System.out.println(table);

            HashMap<String, String> interfaceData = containersInterface.run(null);
            String option = interfaceData.get("option");

            String containerLine = "";

            for(String line: lines){
                String[] parts = line.split(",");

                if(parts[0].equals(option)){
                    containerLine = line;
                    break;
                }
            }

            if(!option.equals("Return")){

                OptionsInterface updateInterface = new OptionsInterface("update","What update for the container " + option + " ?",4);
                updateInterface.addOption(1,"ID",null);
                updateInterface.addOption(2,"Weight",null);
                updateInterface.addOption(3,"Type",null);
                updateInterface.addOption(4,"Port ID",null);
                updateInterface.addOption(5,"Return",null);

                Scanner input = new Scanner(System.in);
                interfaceData = updateInterface.run(null);

                switch (interfaceData.get("option")){
                    case "ID":{
                        while (true){
                            System.out.println("Enter ID(c-00): ");

                            String inputResult = input.nextLine();

                            if(inputResult.matches("^c-\\d+$")){
                                String[] parts = containerLine.split(",");
                                parts[0] = inputResult;
                                String newLine = String.join(",", parts);

                                boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                                if(success){
                                    System.out.println("Update container successfully!");
                                    break;
                                }else{
                                    System.out.println("Failed to update container!");
                                }
                            }else{
                                System.out.println("Id must follow format: c-00");
                            }
                        }
                        break;
                    }
                    case "Weight":{
                        while (true){
                            System.out.println("Enter weight(ex: 5.9)Kg: ");

                            String inputResult = input.nextLine();

                            if(inputResult.matches("\\d+.\\d+")){
                                String[] parts = containerLine.split(",");
                                parts[1] = inputResult;
                                String newLine = String.join(",", parts);

                                boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                                if(success){
                                    System.out.println("Update container successfully!");
                                    break;
                                }else{
                                    System.out.println("Failed to update container!");
                                }
                            }else{
                                System.out.println("Weight must follow format: 0.0");
                            }
                        }
                        break;
                    }
                    case "Type":{
                        OptionsInterface questionInterface = new OptionsInterface("askQuestion", "What type of this container?", 2);
                        questionInterface.addOption(1,"Dry Storage",null);
                        questionInterface.addOption(2,"Refrigerated",null);
                        questionInterface.addOption(3,"Open Top",null);
                        questionInterface.addOption(4,"liquid",null);
                        questionInterface.addOption(5,"Open Side",null);

                        interfaceData = questionInterface.run(null);

                        String type = interfaceData.get("option");

                        String[] parts = containerLine.split(",");

                        if(parts[0].equals(option)){
                            parts[2] = type;

                            String newLine = String.join(",", parts);

                            boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                            if(success){
                                System.out.println("Update container successfully!");
                                break;
                            }else{
                                System.out.println("Failed to update container!");
                            }
                        }
                        break;
                    }
                    case "Port ID":{
                        while (true){
                            System.out.println("Enter port id(p-00): ");

                            String inputResult = input.nextLine();

                            if(inputResult.matches("^p-\\d+$")){
                                String[] parts = containerLine.split(",");

                                if(parts[0].equals(option)){
                                    parts[3] = inputResult;

                                    String newLine = String.join(",",parts);

                                    boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                                    if(success){
                                        System.out.println("Update container successfully!");
                                        break;
                                    }else{
                                        System.out.println("Failed to update container!");
                                    }
                                }

                                break;
                            }else{
                                System.out.println("Port id must follow format: p-00");
                            }
                        }
                        break;
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
    public PMContainer(String cNumber, double weight, String containerType, String portId) {
        this.id = cNumber;
        this.weight = weight;
        this.containerType = containerType;
        this.portId = portId;
    }
}