package Port;

import Container.PMContainer;
import Trip.PMTrip;
import Vehicle.PMVehicle;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import static Menu.PortManagerMenu.updateLinesWithId;

public class PMPort {
    public static final String portsFilePath = "./src/database/ports.txt";
    private String id;
    private String name;
    private String capacity;
    private String landingAbility;
    private void getPortData(){
        Scanner fileData;

        try{
            fileData = new Scanner(new File(portsFilePath));
        }catch (Exception e){
            fileData = null;
        }

        if(fileData!= null){
            while (fileData.hasNext()){
                //#ID, Name, Capacity, LandingAbility
                String line = fileData.nextLine();
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");

                String idField = stringTokenizer.nextToken();
                String nameField = stringTokenizer.nextToken();
                String capacityField = stringTokenizer.nextToken();
                String landingAbility = stringTokenizer.nextToken();

                if(idField.equals(this.id)){
                    this.id = idField;
                    this.landingAbility = landingAbility;
                    this.name = nameField;
                    this.capacity = capacityField;
                    break;
                }
            }
        }
    }
    public PMPort(String id) {
        this.id = id.trim();
        getPortData();
    }

    public String getId() {
        return id;
    }
    public static TableInterface createTableFromDatabase(){
        Scanner fileData;

        try{
            fileData = new Scanner(new File(portsFilePath));
        }catch (Exception e){
            fileData = null;
        }

        String[] containersCols = {"Id","Name","Capacity","Landing Ability"};
        TableInterface table = new TableInterface("ports","Ports",containersCols,",");

        int count = 1;
        if(fileData!= null){
            while (fileData.hasNext()){
                String line = fileData.nextLine();

                if(count != 1){
                    System.out.println(line);
                    table.addRow(line);
                }

                count++;
            }
        }

        return table;
    }
    public String toString(){
        return  id + ", " + name + ", " + capacity + ", " + landingAbility;
    }
    private void updatePort(){
        OptionsInterface updateInterface = new OptionsInterface("update","What do you want to update for the port?",2);
        updateInterface.addOption(1,"Id",null);
        updateInterface.addOption(2,"Name",null);
        updateInterface.addOption(3,"Capacity",null);
        updateInterface.addOption(4,"Landing Ability",null);
        updateInterface.addOption(5,"Return",null);

        Scanner input = new Scanner(System.in);

        boolean keepRunning = true;

        while (keepRunning){
            HashMap<String, String> interfaceData = updateInterface.run(null);

            String option = interfaceData.get("option");

            switch (option){
                case "Id":{
                    while (true){
                        System.out.print("Enter Id: ");
                        String inputResult = input.next();

                        if(inputResult.matches("^p-\\d++$")){
                            String oldId = this.id;
                            this.id = inputResult;

                            boolean success = updateLinesWithId(portsFilePath, oldId, toString());
                            if(success){
                                System.out.println("Updated port successfully!");
                            }else{
                                System.out.println("Failed to update port!");
                            }

                            break;
                        }else{
                            System.out.println("Id must follow this format: p-00");
                        }
                    }

                    break;
                }
                case "Name":{
                    while (true){
                        System.out.println("Enter Name: ");

                        String inputResult = input.next();

                        this.name = inputResult;

                        String line = toString();

                        boolean success = updateLinesWithId(portsFilePath, id, line);
                        if(success){
                            System.out.println("Updated port successfully!");
                        }else{
                            System.out.println("Failed to update port!");
                        }

                        break;
                    }
                    break;
                }
                case "Capacity":{
                    while (true){
                        System.out.println("Enter Capacity: ");

                        String inputResult = input.next();

                        this.capacity = inputResult;

                        String line = toString();

                        boolean success = updateLinesWithId(portsFilePath, id, line);
                        if(success){
                            System.out.println("Updated port successfully!");
                        }else{
                            System.out.println("Failed to update port!");
                        }

                        break;
                    }
                    break;
                }
                case "Landing Ability":{
                    while (true){
                        System.out.println("Enter landing ability: ");

                        String inputResult = input.next();

                        this.landingAbility = inputResult;

                        String line = toString();

                        boolean success = updateLinesWithId(portsFilePath, id, line);
                        if(success){
                            System.out.println("Updated port successfully!");
                        }else{
                            System.out.println("Failed to update port!");
                        }

                        break;
                    }
                    break;
                }
                case "Return":{
                    keepRunning = false;
                    break;
                }
            }
        }
    }
    public PMPort(String id, String name, String capacity, String landingAbility) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.landingAbility = landingAbility;
    }
    public void handlePortOptions(String option){
        switch (option){
            case "Update the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    updatePort();
                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
            case "Display all ports from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = createTableFromDatabase();
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
        }
    }
    public void handleVehicleOptions(String option) {
        switch (option){
            case "Update a vehicle from the port": {
                break;
            }
            case "Display all vehicles from the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMVehicle.createTableFromDatabase(this.name);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }case "Display all vehicles from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMVehicle.createTableFromDatabase(null);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
        }
    }
    public void handleContainerOptions(String option){
        switch (option){
            case "Add a container to database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    PMContainer.addContainerToDatabase(id);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
            case "Update a container from database": {
                PMContainer.updateContainerFromDatabase();
                break;
            }
            case "Delete a container from database": {
                PMContainer.deleteContainerFromDatabase();
                break;
            }
            case "Display all containers from the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMContainer.createTableFromDatabase(this.id);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
            case "Display all containers from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMContainer.createTableFromDatabase(null);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
        }
    }
    public void handleTripsOptions(String option) {
        switch (option){
            case "Create a trip": {
                Scanner input = new Scanner(System.in);
                break;
            }
            case "Update a trip from database": {
                break;
            }case "Delete a trip from database": {
                break;
            }
            case "Display all trips from the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMTrip.createTableFromDatabase(name);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
            case "Display all trips from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMTrip.createTableFromDatabase(null);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.next();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
        }
    }
    public void handleStatisticOptions(String option) {
        switch (option){
            case "Profile": {
                Scanner input = new Scanner(System.in);
                break;
            }
            case "Port": {
                break;
            }case "Containers": {
                break;
            }
            case "Vehicles": {
                break;
            }
            case "Trips": {
                break;
            }
            case "Summary": {
                break;
            }
        }
    }
}