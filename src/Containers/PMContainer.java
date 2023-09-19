package Containers;

import Menu.PortManagerMenu;
import interfaces.builders.OptionsInterface;
import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

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
    public static void updateContainerFromDatabase(){
        // Create a menu
        // Run while loop with Scanner to add container id as option
        // Run menu and get container id
        // Create a menu with options to choose which part to edit(id, name, portId, etc.)
        // Run while loop
        // Run menu and get the edit option
        // Edit the container with new line data
        //PortManagerMenu.updateLinesWithId();
    }
    public static void deleteContainerFromDatabase(){
        // Create a menu
        // Run while loop with Scanner to add container id as option
        // Run menu and get containerId
        // Use function to delete container
        //PortManagerMenu.deleteLinesWithId()
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
