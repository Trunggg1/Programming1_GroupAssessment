package Containers;

import Menu.PortManagerMenu;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class PMContainer {
    private static final String containersFilePath = "./src/database/containers.txt";
    private String id;
    private double weight;
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
            line = id + ", " + weight + ", " + type + portId;
        }else{
            line = id + ", " + weight + ", " + type + "null";
        }

        boolean success = PortManagerMenu.addLineToDatabase(containersFilePath, line);

        if(success){
            System.out.println("Successfully added container to database!");
        }else{
            System.out.println("Failed to add container to database!");
        }
    }
    public static void updateContainerFromDatabase(String portId){
        boolean keepRunning = true;

        while (keepRunning){
            Scanner fileData;
            OptionsInterface mainInterface = new OptionsInterface("askQuestion","Which container you want to update?", 5);

            int count = 1;

            try{
                fileData = new Scanner(new File(containersFilePath));
            }catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            ArrayList<String> lines = new ArrayList<String>();

            if(fileData!= null){
                while (fileData.hasNext()){
                    String line = fileData.nextLine();
                    String[] parts = line.split(",");

                    if(parts[0].matches("^c-\\d+$")){
                        if(portId != null){
                            if(parts[3].trim().equals(portId)){
                                mainInterface.addOption(count,parts[0],null);
                                lines.add(line);
                                count++;
                            }
                        }
                        else{
                            mainInterface.addOption(count,parts[0],null);
                            lines.add(line);
                            count++;
                        }
                    }
                }
            }

            mainInterface.addOption(count,"Return",null);
            TableInterface table = createTableFromDatabase(portId);
            System.out.println(table);

            HashMap<String, String> interfaceData = mainInterface.run(null);
            String option = interfaceData.get("option");

            if(option.equals("Return")){
                break;
            }

            OptionsInterface updateInterface = new OptionsInterface("update","What update for the container " + option + " ?",2);
            updateInterface.addOption(1,"Id",null);
            updateInterface.addOption(2,"Weight",null);
            updateInterface.addOption(3,"Type",null);
            updateInterface.addOption(4,"Port Id",null);
            updateInterface.addOption(5,"Return",null);

            Scanner input = new Scanner(System.in);

            interfaceData = updateInterface.run(null);

            switch (interfaceData.get("option")){
                case "Id":{
                    while (true){
                        System.out.println("Enter Id(c-00): ");

                        String inputResult = input.nextLine();

                        if(inputResult.matches("^c-\\d+$")){
                            for (String line : lines) {
                                String[] parts = line.split(",");

                                if(parts[0].equals(option)){
                                    parts[0] = inputResult;

                                    String newLine = String.join(",", parts);

                                    boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                                    if(success){
                                        System.out.println("Update container successfully!");
                                        break;
                                    }else{
                                        System.out.println("Failed to update container!");
                                        break;
                                    }
                                }
                            }

                            break;
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
                            for (String line : lines) {
                                String[] parts = line.split(",");

                                if(parts[0].equals(option)){
                                    parts[1] = inputResult;

                                    String newLine = String.join(",", parts);

                                    boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                                    if(success){
                                        System.out.println("Update container successfully!");
                                        break;
                                    }else{
                                        System.out.println("Failed to update container!");
                                        break;
                                    }
                                }
                            }

                            break;
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

                    for (String line : lines) {
                        String[] parts = line.split(",");

                        if(parts[0].equals(option)){
                            parts[2] = type;

                            String newLine = String.join(",", parts);

                            boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                            if(success){
                                System.out.println("Update container successfully!");
                                break;
                            }else{
                                System.out.println("Failed to update container!");
                                break;
                            }
                        }
                    }

                    break;
                }
                case "Port Id":{
                    while (true){
                        System.out.println("Enter port id(p-00): ");

                        String inputResult = input.nextLine();

                        if(inputResult.matches("^p-\\d+$")){
                            for (String line : lines) {
                                String[] parts = line.split(",");

                                if(parts[0].equals(option)){
                                    parts[3] = inputResult;

                                    String newLine = String.join(",",parts);

                                    boolean success = PortManagerMenu.updateLinesWithId(containersFilePath, option, newLine);

                                    if(success){
                                        System.out.println("Update container successfully!");
                                        break;
                                    }else{
                                        System.out.println("Failed to update container!");
                                        break;
                                    }
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
        }

        // Create a menu
        // Run while loop with Scanner to add container id as option
        // Run menu and get container id
        // Create a menu with options to choose which part to edit(id, name, portId, etc.)
        // Run while loop
        // Run menu and get the edit option
        // Edit the container with new line data
        //PortManagerMenu.updateLinesWithId();
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
    public static TableInterface createTableFromDatabase(String id){
        Scanner fileData;

        try{
            fileData = new Scanner(new File(containersFilePath));
        }catch (Exception e){
            fileData = null;
        }

        String[] containersCols = {"id","weight","type","portId"};
        TableInterface table = new TableInterface("containersData","Containers",containersCols,",");

        int count = 1;
        if(fileData!= null){
            while (fileData.hasNext()){
                String line = fileData.nextLine();
                String[] parts = line.split(",");

                if(count != 1){
                    if(id!= null){
                        String portId = parts[3].trim();
                        String trimmedId = id.trim();

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
    public static void loadContainerOnVehicle(){
        //
    }
    //get Data for this container
    private void getContainerData(){
        Scanner fileData;

        try{
            fileData = new Scanner(new File(containersFilePath));
        }catch (Exception e){
            fileData = null;
        }

        if(fileData!= null){
            while (fileData.hasNext()){
                String line = fileData.nextLine();
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");

                String idField = stringTokenizer.nextToken();
                String weightField = stringTokenizer.nextToken();
                String typeField = stringTokenizer.nextToken();
                String portIdField = stringTokenizer.nextToken();

                if(idField.equals(this.id)){
                    this.id = idField;
                    this.weight = Double.parseDouble(weightField);
                    this.containerType = typeField;
                    this.portId = portIdField;
                    break;
                }
            }
        }
    }
    public PMContainer(String cNumber) {
        this.id = cNumber;
        this.getContainerData();
    }
    public PMContainer(String cNumber, double weight, String containerType, String portId) {
        this.id = cNumber;
        this.weight = weight;
        this.containerType = containerType;
        this.portId = portId;
    }

    // Getters and setters for container attributes
    public String getId() {
        return id;
    }
    public void setId(String cNumber) {
        this.id = cNumber;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public String getContainerType() {
        return containerType;
    }
    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }
}
