package User;

import Port.AdminPort;
import Resource.UserInput;
import Resource.ReadDatabase;
import Resource.WriteFile;

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

    public void addPort() throws IOException, ParseException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        PrintWriter pw;
        AdminPort Port = new AdminPort();
        pw = new PrintWriter(new FileWriter("./src/database/ports.txt", true));
        Path path = Paths.get("./src/database/ports.txt");
        int id = (int) Files.lines(path).count(); // Define the id of this port
        System.out.println("Enter a name for this port (e.g: District 7): "); // Ask admin to input the port's name
        String name = scanner.nextLine();
        String ID = String.format("P-%02d", id, name); // Generate the Port ID in items' file
        System.out.println("Enter capacity (e.g: 1000kg): "); // Ask admin to input the port's capacity
        String capacity = scanner.nextLine();
        System.out.println("Enter landing ability (e.g: Truck Available / Truck Unavailable) : "); // Ask admin to input the port's landing availability
        String landingAbility = scanner.nextLine();
        pw.println(ID + "," + name + "," + capacity + "," + landingAbility);
//        // FileMethods.Write product's information to items' file
        pw.close();
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
                newDatabase.add(strings); // Add all items except the deleted product
            }
        }
        PrintWriter pw = new PrintWriter("./src/database/ports.txt");

        pw.write(""); // The file would erase all the data in items' file
        pw.close();


        for (String[] obj : newDatabase) {
            WriteFile.rewriteFile("./src/database/ports.txt", "#ID,Name,Capacity,LandingAbility", String.join(",", obj));
            // This method would allow system to write all data including new data into the items' file
        }
        System.out.println("Deletion successful");
    }


}
