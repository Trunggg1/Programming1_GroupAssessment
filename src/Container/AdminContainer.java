package Container;

import Resource.ReadDatabase;
import Resource.TableInterface;
import Resource.WriteFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AdminContainer {
    // Attributes
    private String ID;
    private String Weight;
    private String Type;
    private String portId;

    public AdminContainer(String ID, String Weight, String Type, String portId) {
        this.ID = ID;
        this.Weight = Weight;
        this.Type = Type;
        this.portId = portId;
    }

    public AdminContainer() {
    }

    public void getAllContainerInfo() throws FileNotFoundException {
        ArrayList<String[]> user = new ArrayList<>();
        Scanner fileContainers = new Scanner(new File("./src/database/containers.txt"));

        // The while loop is used to get info in the scanner.
        while (fileContainers.hasNext()) {
            String[] containerData = new String[3];
            String line = fileContainers.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            String ID = stringTokenizer.nextToken();
            String Weight = stringTokenizer.nextToken();
            String Type = stringTokenizer.nextToken();
            String portId = stringTokenizer.nextToken();
            containerData = new String[]{ID, Weight, Type, portId};
            user.add(containerData);

        }
        // Print out the table contain all the container information.
        TableInterface.setShowVerticalLines(true);
        TableInterface.setHeaders("ID", "Weight", "Type", "portId");

        for (int i = 1; i < user.size(); i++) {
            TableInterface.addRow(user.get(i)[0], user.get(i)[1], user.get(i)[2], user.get(i)[3]);
        }

        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());
    }

    public void getContainerId() throws FileNotFoundException { //Display container information with options to choose
        ArrayList<String[]> user = new ArrayList<>();
        Scanner fileContainers = new Scanner(new File("./src/database/containers.txt"));

        // Use tokenizer to pick data
        while (fileContainers.hasNext()) {
            String[] containerData = new String[3];
            String line = fileContainers.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            String ID = stringTokenizer.nextToken();
            String Type = stringTokenizer.nextToken();
            String Weight = stringTokenizer.nextToken();
            containerData = new String[]{ID, Type, Weight};
            user.add(containerData); // User input choice
        }

        TableInterface.setShowVerticalLines(true);
        TableInterface.setHeaders("OPTION", "ID", "Type", "Weight");  // Option choices

        for (int i = 1; i < user.size(); i++) {
            TableInterface.addRow(String.valueOf(i), user.get(i)[0], user.get(i)[1], user.get(i)[2]);
        }

        TableInterface.print();
        TableInterface.setHeaders(new String[0]);
        TableInterface.setRows(new ArrayList<String[]>());
    }

    public void updateContainerWeight(String filepath, String newData, String ID) throws IOException {

        ArrayList<String[]> database = ReadDatabase.readAllLines("./src/database/containers.txt");

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
            WriteFile.rewriteFile(filepath, "ID, Weight, Type, portId", String.join(",", strings)); // Rewrite new data
        }
    }

    public void updateContainerType(String filepath, String newData, String ID) throws IOException {

        ArrayList<String[]> database = ReadDatabase.readAllLines("./src/database/containers.txt");

        for (String[] strings : database) {
            if (strings[0].equals(ID)) { // System check ID
                strings[2] = newData; // Modify the container type
            }
        }
        File file = new File(filepath);
        PrintWriter printWriter = new PrintWriter(file);

        printWriter.write(""); // erase data
        printWriter.close();

        for (String[] strings : database) {
            WriteFile.rewriteFile(filepath, "ID, Weight, Type", String.join(",", strings)); // Rewrite new data
        }
    }


}