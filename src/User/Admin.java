package User;

import Container.AdminContainer;
import Port.AdminPort;
import Resource.UserInput;
import Resource.ReadDatabase;
import Resource.WriteFile;
import Vehicle.AdminVehicle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends Account {

    public Admin() {
        super();
    }

    public boolean verifyAdmin(String username, String password) {
        String hashPassword = this.hashing(password); // Hash the input password
        // If the username and password after hashing are correct
        return username.equals("admin") && hashPassword.equals("751cb3f4aa17c36186f4856c8982bf27");
    }

// ================================================= PORT SYSTEM =================================================
    public void addPort() throws IOException, ParseException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        PrintWriter printWriter;
        AdminPort Port = new AdminPort();

        printWriter = new PrintWriter(new FileWriter("./src/database/ports.txt", true));

        Path path = Paths.get("./src/database/ports.txt");
        int id = (int) Files.lines(path).count(); // Define the id of this port

        System.out.println("Enter port's name (e.g: District 7): "); // Admin input the port's name
        String name = scanner.nextLine();

        String ID = String.format("p-%02d", id, name); // Generate the Port ID in dataqbase

        String capacity = "0";
        boolean validInput = false;
        while(!validInput) {
            System.out.println("Enter capacity (e.g: 1000kg): "); // Ask admin to input the port's capacity
            String capacityInput  = scanner.nextLine();
            if (!capacityInput .contains("kg")) { //Check if unit measured was input
                System.out.println("Error: Please include capacity units in kg"); //Display error if measured unit was not input
            } else {
                validInput = true;
                capacity = capacityInput;
            }
        }

        System.out.println("Enter landing ability (e.g: Truck Available / Truck Unavailable) : "); // Ask admin to input the port's landing availability
        String landingAbility = scanner.nextLine();

        printWriter.println(ID + "," + name + "," + capacity + "," + landingAbility); // Write port's information to database
        printWriter.close();
        System.out.println("Addition successful");
    }

    public void deletePort() throws IOException {

        AdminPort port = new AdminPort();
        port.getPortId();
        String choiceOrder = UserInput.rawInput();

        ArrayList<String[]> portList = ReadDatabase.readAllLines("./src/database/ports.txt");

        String[] portInfo = new String[3];
        for (int i = 0; i < portList.size(); i++) {
            if (i == Integer.parseInt(choiceOrder)) {
                portInfo = ReadDatabase.readSpecificLine(portList.get(i)[1], 1, "./src/database/ports.txt", ",");
            }
        }

        ArrayList<String[]> database = ReadDatabase.readAllLines("./src/database/ports.txt");

        ArrayList<String[]> newDatabase = new ArrayList<>();
        for (String[] strings : database) {
            if (!strings[0].equals(portInfo[0])) {
                newDatabase.add(strings); // replace all port except the deleted one
            }
        }

        PrintWriter printWriter = new PrintWriter("./src/database/ports.txt");
        printWriter.write(""); // erase data
        printWriter.close();

        // write new data into database
        for (String[] obj : newDatabase) {
            WriteFile.rewriteFile("./src/database/ports.txt", "#ID,Name,Capacity,LandingAbility", String.join(",", obj));
        }
        System.out.println("Deletion successful");
    }

// ================================================= VEHICLE SYSTEM =================================================
    public void addVehicle() throws IOException, ParseException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        PrintWriter printWriter;
        AdminVehicle Vehicle = new AdminVehicle();

        printWriter = new PrintWriter(new FileWriter("./src/database/vehicles.txt", true));

        Path path = Paths.get("./src/database/vehicles.txt");
        int id = (int) Files.lines(path).count(); // Define the id of this vehicle

        // Ask user to enter type of truck or ship
        System.out.println("Enter vehicle's type \nex. : Basic Truck \n      Reefer Truck \n      Tanker Truck \n      Ship"); // Admin input the vehicle's type
        String vehicleType  = scanner.nextLine();

//      Define vehicle type and assign proper id
        String ID;
        if(vehicleType.equalsIgnoreCase("Basic Truck")) {
            ID = "btr-";
        } else if(vehicleType.equalsIgnoreCase("Reefer Truck")) {
            ID = "rtr-";
        } else if(vehicleType.equalsIgnoreCase("Tanker Truck")) {
            ID = "ttr-";
        } else if(vehicleType.equalsIgnoreCase("Ship")) {
            ID = "sh-";
        } else {
            System.out.println("Invalid vehicle type"); // if user enter incorrect vehicle type, display error message
            return;
        }
        String idString = ID + String.format("%02d", id);

        // Ask admin to input the vehicle's carrying capacity
        String capacity = "0";
        boolean validInput = false;
        while(!validInput) {
            System.out.println("Enter carrying capacity (e.g: 1000kg): "); // Ask admin to input the vehicle's carrying capacity
            String capacityInput  = scanner.nextLine();
            if (!capacityInput .contains("kg")) { //Check if unit measured was input
                System.out.println("Error: Please include capacity units in kg"); //Display error if measured unit was not input
            } else {
                validInput = true;
                capacity = capacityInput;
            }
        }
        // Ask admin to input the vehicle's Current Fuel
        String currentFuel = "0";
        boolean validCurrentFuel = false;
        while(!validCurrentFuel) {
            System.out.println("Enter current fuel (e.g: 50%): "); // Ask admin to input the vehicle's current fuel
            String currentFuelInput  = scanner.nextLine();
            if (!currentFuelInput .contains("%")) { //Check if unit measured was input
                System.out.println("Error: Please include current fuel units in % (percentage)"); //Display error if measured unit was not input
            } else {
                validCurrentFuel = true;
                currentFuel = currentFuelInput;
            }
        }

        // Ask admin to input the vehicle's Fuel capacity
        String fuelCapacity = "0";
        boolean validFuelCapacity = false;
        while(!validFuelCapacity) {
            System.out.println("Enter fuel capacity (e.g: 50L): "); // Ask admin to input the vehicle's Fuel capacity
            String fuelCapacityInput  = scanner.nextLine();
            if (!fuelCapacityInput .contains("L")) { //Check if unit measured was input
                System.out.println("Error: Please include capacity units in L (litters)"); //Display error if measured unit was not input
            } else {
                validFuelCapacity = true;
                fuelCapacity = fuelCapacityInput;
            }
        }

        System.out.println("Enter current port (e.g: Long Khanh) : "); // Ask admin to input the current port of that vehicle
        String currentPort = scanner.nextLine();

        printWriter.println(idString + "," + vehicleType + "," + capacity + "," + currentFuel + "," + fuelCapacity + "," + currentPort); // Write vehicle's information to database
        printWriter.close();
        System.out.println("Addition successful");
    }

    public void deleteVehicle() throws IOException {

        AdminVehicle vehicle = new AdminVehicle();
        vehicle.getVehicleId();
        String choiceOrder = UserInput.rawInput();

        ArrayList<String[]> vehicleList = ReadDatabase.readAllLines("./src/database/vehicles.txt");

        String[] vehicleInfo = new String[3];
        for (int i = 0; i < vehicleList.size(); i++) {
            if (i == Integer.parseInt(choiceOrder)) {
                vehicleInfo = ReadDatabase.readSpecificLine(vehicleList.get(i)[1], 1, "./src/database/vehicles.txt", ",");
            }
        }

        ArrayList<String[]> database = ReadDatabase.readAllLines("./src/database/vehicles.txt");

        ArrayList<String[]> newDatabase = new ArrayList<>();
        for (String[] strings : database) {
            if (!strings[0].equals(vehicleInfo[0])) {
                newDatabase.add(strings); // replace all port except the deleted one
            }
        }

        PrintWriter printWriter = new PrintWriter("./src/database/vehicles.txt");
        printWriter.write(""); // erase data
        printWriter.close();

        // write new data into database
        for (String[] obj : newDatabase) {
            WriteFile.rewriteFile("./src/database/vehicles.txt", "#ID, Type, Carrying Capacity, Current Fuel, Fuel Capacity, Current Port", String.join(",", obj));
        }
        System.out.println("Deletion successful");
    }

    // ================================================= CONTAINER SYSTEM =================================================

    public void addContainer() throws IOException, ParseException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        PrintWriter printWriter;
        AdminContainer Container = new AdminContainer();

        printWriter = new PrintWriter(new FileWriter("./src/database/containers.txt", true));

        Path path = Paths.get("./src/database/containers.txt");
        int id = (int) Files.lines(path).count(); // Define the id of this port

        System.out.println("Enter container's type \nex. : Dry Storage \n      Open top \n      Open side \n      Refrigerated \n      Liquid): "); // Admin input the port's name
        String type = scanner.nextLine();

        String ID = String.format("c-%02d", id, type); // Generate the container ID in dataqbase

        String weight = "0";
        boolean validWeightInput = false;
        while(!validWeightInput) {
            System.out.println("Enter container's weight (e.g: 100kg): "); // Ask admin to input the container's weight
            String weightInput  = scanner.nextLine();
            if (!weightInput .contains("kg")) { //Check if unit measured was input
                System.out.println("Error: Please include capacity units in kg"); //Display error if measured unit was not input
            } else {
                validWeightInput = true;
                weight = weightInput;
            }
        }

        String portId = "0";
        boolean validInput = false;
        while(!validInput) {
            System.out.println("Enter which port the container locate (e.g: p-01): "); // Ask admin to input the container's weight
            String portIdInput  = scanner.nextLine();
            if (!portIdInput .contains("p-")) { //Check if unit measured was input
                System.out.println("Error: Please include relevant port ID"); //Display error if measured unit was not input
            } else {
                validInput = true;
                portId = portIdInput;
            }
        }

        printWriter.println(ID + "," + weight + "," + type + "," + portId); // Write port's information to database
        printWriter.close();
        System.out.println("Addition successful");
    }

    public void deleteContainer() throws IOException {

        AdminContainer Container = new AdminContainer();
        Container.getContainerId();
        String choiceOrder = UserInput.rawInput();

        ArrayList<String[]> containerList = ReadDatabase.readAllLines("./src/database/containers.txt");

        String[] containerInfo = new String[3];
        for (int i = 0; i < containerList.size(); i++) {
            if (i == Integer.parseInt(choiceOrder)) {
                containerInfo = ReadDatabase.readSpecificLine(containerList.get(i)[1], 1, "./src/database/containers.txt", ",");
            }
        }

        ArrayList<String[]> database = ReadDatabase.readAllLines("./src/database/containers.txt");

        ArrayList<String[]> newDatabase = new ArrayList<>();
        for (String[] strings : database) {
            if (!strings[0].equals(containerInfo[0])) {
                newDatabase.add(strings); // replace all port except the deleted one
            }
        }

        PrintWriter printWriter = new PrintWriter("./src/database/containers.txt");
        printWriter.write(""); // erase data
        printWriter.close();

        // write new data into database
        for (String[] obj : newDatabase) {
            WriteFile.rewriteFile("./src/database/containers.txt", "#id, weight, type, portId", String.join(",", obj));
        }
        System.out.println("Deletion successful");
    }

}
