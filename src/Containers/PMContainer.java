package Containers;

import interfaces.builders.PromptsInterface;
import interfaces.builders.TableInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class PMContainer {
    private static final String containersFilePath = "./src/database/containers.txt";
    private String id;
    private double weight;
    private String containerType;
    private String portId;

    public static void addContainerToDatabase() {
        //Yeu cau input
        while (true){
            PromptsInterface prompt = new PromptsInterface("containersPrompt","Add container inputs");
            prompt.addPrompt("Enter id");
            prompt.addPrompt("Enter weight");
            prompt.addPrompt("Enter type");

            HashMap<Number, String> results = prompt.startPrompts();

            String idPattern = "^c-\\d{2}$";

            String id = results.get(1);
            String weight = results.get(2);

            if(!id.matches(idPattern)){
                System.out.println("Id must follow this format: c-00");
            }

            if(!weight.matches("^-?\\d+(\\.\\d+)?$")){
                System.out.println("Weight has to be a double value");
            }

            //Kiem tra input

            //Input "Go back" to go back

            //Add len database
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
