package Vehicle;

import Resource.ReadDatabase;
import Resource.TableInterface;
import Resource.WriteFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AdminVehicle {
    // Attributes
    private String ID;
    private String Type;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;

    public AdminVehicle(String ID, String Type, String carryingCapacity, String currentFuel, String fuelCapacity, String currentPort ) {
        this.ID = ID;
        this.Type = Type;
        this.carryingCapacity = carryingCapacity;
        this.currentFuel = currentFuel;
        this.fuelCapacity = fuelCapacity;
        this.currentPort = currentPort;
    }

    public AdminVehicle() {
    }

    public void getAllVehicleInfo() throws FileNotFoundException {
        ArrayList<String[]> user = new ArrayList<>();
        Scanner fileVehicles = new Scanner(new File("./src/database/vehicles.txt"));

        // The while loop is used to get info in the scanner.
        while (fileVehicles.hasNext()) {
            String[] vehicleData = new String[3];
            String line = fileVehicles.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            String ID = stringTokenizer.nextToken();
            String Type = stringTokenizer.nextToken();
            String carryingCapacity = stringTokenizer.nextToken();
            String currentFuel = stringTokenizer.nextToken();
            String fuelCapacity = stringTokenizer.nextToken();
            String currentPort = stringTokenizer.nextToken();
            vehicleData = new String[]{ID, Type, carryingCapacity, currentFuel, fuelCapacity, currentPort};
            user.add(vehicleData);

        }
        // Print out the table contain all the product information.
        TableInterface.setShowVerticalLines(true);
        TableInterface.setHeaders("ID", "Type", "Carrying Capacity", "Current Fuel", "Fuel Capacity", "Current Port");

        for (int i = 1; i < user.size(); i++) {
            TableInterface.addRow(user.get(i)[0], user.get(i)[1], user.get(i)[2], user.get(i)[3], user.get(i)[4], user.get(i)[5]);
        }

        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());
    }

    public void getVehicleId() throws FileNotFoundException { //Display vehicles information with options to choose
        ArrayList<String[]> user = new ArrayList<>();
        Scanner fileVehicles = new Scanner(new File("./src/database/vehicles.txt"));

        // Use tokenizer to pick data
        while (fileVehicles.hasNext()) {
            String[] vehicleData = new String[3];
            String line = fileVehicles.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            String ID = stringTokenizer.nextToken();
            String Type = stringTokenizer.nextToken();
            String carryingCapacity = stringTokenizer.nextToken();
            vehicleData = new String[]{ID, Type, carryingCapacity};
            user.add(vehicleData); // User input choice
        }

        TableInterface.setShowVerticalLines(true);
        TableInterface.setHeaders("OPTION", "ID", "Type", "Carrying Capacity");  // Option choices

        for (int i = 1; i < user.size(); i++) {
            TableInterface.addRow(String.valueOf(i), user.get(i)[0], user.get(i)[1], user.get(i)[2]);
        }

        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());
    }

    public void updateVehicleType(String filepath, String newData, String ID) throws IOException {

        ArrayList<String[]> database = ReadDatabase.readAllLines("./src/database/vehicles.txt");

        for (String[] strings : database) {
            if (strings[0].equals(ID)) { /* System check ID */
                strings[1] = newData; // Modify the vehicle's type
            }
        }
        File file = new File(filepath);
        PrintWriter printWriter = new PrintWriter(file);

        printWriter.write(""); // erase data
        printWriter.close();

        for (String[] strings : database) {
            WriteFile.rewriteFile(filepath, "ID, Type, Carrying Capacity", String.join(",", strings)); // Rewrite new data
        }
    }

    public void updateVehicleCapacity(String filepath, String newData, String ID) throws IOException {

        ArrayList<String[]> database = ReadDatabase.readAllLines("./src/database/vehicles.txt");

        for (String[] strings : database) {
            if (strings[0].equals(ID)) { // System check ID
                strings[2] = newData; // Modify the vehicle's carrying capacity
            }
        }
        File file = new File(filepath);
        PrintWriter printWriter = new PrintWriter(file);

        printWriter.write(""); // erase data
        printWriter.close();

        for (String[] strings : database) {
            WriteFile.rewriteFile(filepath, "ID, Type, Carrying Capacity", String.join(",", strings)); // Rewrite new data
        }
    }


}