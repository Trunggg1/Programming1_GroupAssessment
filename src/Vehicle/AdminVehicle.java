package Vehicle;

import Resources.TableInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AdminVehicle {
    // Attributes
    private String ID;
    private String Name;
    private String carryingCapacity;
    private String currentFuel;
    private String fuelCapacity;
    private String currentPort;

    public AdminVehicle(String ID, String Name, String carryingCapacity, String currentFuel, String fuelCapacity, String currentPort ) {
        this.ID = ID;
        this.Name = Name;
        this.carryingCapacity = carryingCapacity;
        this.currentFuel = currentFuel;
        this.fuelCapacity = fuelCapacity;
        this.currentPort = currentPort;
    }

    public AdminVehicle() {
    }

    public void getAllVehicleInfo() throws FileNotFoundException {
        ArrayList<String[]> user = new ArrayList<>();
        Scanner fileProducts = new Scanner(new File("./src/database/vehicles.txt"));

        // The while loop is used to get info of each product in the scanner.
        while (fileProducts.hasNext()) {
            String[] vehicleData = new String[3];
            String line = fileProducts.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            String ID = stringTokenizer.nextToken();
            String Name = stringTokenizer.nextToken();
            String carryingCapacity = stringTokenizer.nextToken();
            String currentFuel = stringTokenizer.nextToken();
            String fuelCapacity = stringTokenizer.nextToken();
            String currentPort = stringTokenizer.nextToken();
            vehicleData = new String[]{ID, Name, carryingCapacity, currentFuel, fuelCapacity, currentPort};
            user.add(vehicleData);

        }
        // Print out the table contain all the product information.
        TableInterface.setShowVerticalLines(true);
        TableInterface.setHeaders("ID", "Name", "Carrying Capacity", "Current Fuel", "Fuel Capacity", "Current Port");

        for (int i = 1; i < user.size(); i++) {
            TableInterface.addRow(user.get(i)[0], user.get(i)[1], user.get(i)[2], user.get(i)[3], user.get(i)[4], user.get(i)[5]);
        }

        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());
    }
}