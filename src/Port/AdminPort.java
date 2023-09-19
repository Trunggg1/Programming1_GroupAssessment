package Port;

import Resource.TableInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AdminPort {
    // Attributes
    private String ID;
    private String Name;
    private String Capacity;
    private String landingAbility;

    public AdminPort(String ID, String title, Long price, String category) {
        this.ID = ID;
        this.Name = Name;
        this.Capacity = Capacity;
        this.landingAbility = landingAbility;
    }

    public AdminPort() {
    }

    public void getAllPortInfo() throws FileNotFoundException {
        ArrayList<String[]> user = new ArrayList<>();
        Scanner fileProducts = new Scanner(new File("./src/database/ports.txt"));

        // The while loop is used to get info of each product in the scanner.
        while (fileProducts.hasNext()) {
            String[] portData = new String[3];
            String line = fileProducts.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            String ID = stringTokenizer.nextToken();
            String Name = stringTokenizer.nextToken();
            String Capacity = stringTokenizer.nextToken();
            String landingAbility = stringTokenizer.nextToken();
            portData = new String[]{ID, Name, Capacity, landingAbility};
            user.add(portData);

        }
        // Print out the table contain all the product information.
        TableInterface.setShowVerticalLines(true);
        TableInterface.setHeaders("ID", "Name", "Capacity", "landing Ability");

        for (int i = 1; i < user.size(); i++) {
            TableInterface.addRow(user.get(i)[0], user.get(i)[1], user.get(i)[2], user.get(i)[3]);
        }

        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());
    }

    public void getPortId() throws FileNotFoundException {
        ArrayList<String[]> user = new ArrayList<>();
        Scanner fileProducts = new Scanner(new File("./src/database/ports.txt"));

        while (fileProducts.hasNext()) {
            String[] portData = new String[3];
            String line = fileProducts.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            String ID = stringTokenizer.nextToken();
            String Name = stringTokenizer.nextToken();
            String Capacity = stringTokenizer.nextToken();
            String landingAbility = stringTokenizer.nextToken();
            portData = new String[]{ID, Name, Capacity, landingAbility};
            user.add(portData);
        }

        TableInterface.setShowVerticalLines(true);
        TableInterface.setHeaders("OPTION", "ID", "Name", "Capacity", "Landing Ability");

        for (int i = 1; i < user.size(); i++) {
            TableInterface.addRow(String.valueOf(i), user.get(i)[0], user.get(i)[1], user.get(i)[2], user.get(i)[3]);
        }

        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());
    }




}