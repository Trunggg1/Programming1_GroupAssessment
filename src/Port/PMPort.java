package Port;

import Containers.PMContainer;
import Trip.PMTrip;
import Vehicle.PMVehicle;
import interfaces.builders.OptionsInterface;
import interfaces.builders.TableInterface;
import users.PortManager;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import static Menu.PortManagerMenu.updateLinesWithId;

public class PMPort {
    private static final String portsFilePath = "./src/database/ports.txt";
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
        updateInterface.addOption(1,"Name",null);
        updateInterface.addOption(2,"Capacity",null);
        updateInterface.addOption(3,"Landing Ability",null);
        updateInterface.addOption(4,"Return",null);

        Scanner input = new Scanner(System.in);

        boolean keepRunning = true;

        while (keepRunning){
            System.out.println("Current port: " + this);
            HashMap<String, String> interfaceData = updateInterface.run(null);

            String option = interfaceData.get("option");

            switch (option){
                case "Name":{
                    while (true){
                        System.out.println("Enter name: ");

                        this.name = input.nextLine();

                        boolean success = updateLinesWithId(portsFilePath, id, toString());
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
                        System.out.println("Enter Capacity(ex: 1000Kg): ");

                        this.capacity = input.nextLine();

                        boolean success = updateLinesWithId(portsFilePath, id, toString());

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
                        OptionsInterface questionInterface = new OptionsInterface("askQuestion","Which landing ability does this port have?",4);
                        questionInterface.addOption(1,"Truck Availability",null);
                        questionInterface.addOption(2,"Unavailability", null);

                        interfaceData = questionInterface.run(null);

                        this.landingAbility = interfaceData.get("option");

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
                    String inputResult = input.nextLine();

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
                    String inputResult = input.nextLine();

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
                    String inputResult = input.nextLine();

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
                    String inputResult = input.nextLine();

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
                    String inputResult = input.nextLine();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }

                break;
            }
            case "Update a container from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    OptionsInterface questionInterface = new OptionsInterface("askQuestion", "Display containers inside port only?", 2);
                    questionInterface.addOption(1,"Yes", null);
                    questionInterface.addOption(2,"No", null);

                    HashMap<String, String> interfaceData = questionInterface.run(null);

                    if(interfaceData.get("option").equals("Yes")){
                        PMContainer.updateContainerFromDatabase(id);
                    }else{
                        PMContainer.updateContainerFromDatabase(null);
                    }

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.nextLine();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
            case "Delete a container from database": {
                Scanner input = new Scanner(System.in);

                while (true){
                    PMContainer.deleteContainerFromDatabase();

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.nextLine();

                    if(inputResult.equals("Y") || inputResult.equals("y")){
                        break;
                    }
                }
                break;
            }
            case "Display all containers from the port": {
                Scanner input = new Scanner(System.in);

                while (true){
                    TableInterface table = PMContainer.createTableFromDatabase(this.id);
                    System.out.println(table);

                    System.out.println("Go back?(Y/N)");
                    String inputResult = input.nextLine();

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
                    String inputResult = input.nextLine();

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
            case "Add a trip to database": {
                PMTrip.addTripToDatabase();
                break;
            }
            case "Update a trip from database": {
                PMTrip.updateTripToDatabase();
                break;
            }case "Delete a trip from database": {
                PMTrip.deleteTripToDatabase();
                break;
            }
            case "Display all trips from the port": {
                PMTrip.displayAllTripsFromDatabase(id);
                break;
            }
            case "Display trips from database": {
                PMTrip.displayAllTripsFromDatabase(null);
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
