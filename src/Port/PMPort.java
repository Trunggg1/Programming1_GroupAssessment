package Port;

import Containers.PMContainer;
import interfaces.builders.TableInterface;

import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;

public class PMPort {
    private final String portsFilePath = "./src/database/ports.txt";
    private String id;
    private String name;
    private String capacity;
    private String landingAbility;
    private void getPortData(){
        Scanner fileData;

        try{
            fileData = new Scanner(new File(this.portsFilePath));
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
        this.id = id;
        getPortData();
    }
    public PMPort(String id, String name, String capacity, String landingAbility) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.landingAbility = landingAbility;
    }
    public void handlePortOptions(String option){
        switch (option){
            case "Update a port": {
                //Display port infomration as a table and user choose options that they want to update
            }
        }
    }
    public void handleContainerOptions(String option){
        switch (option){
            case "Display all containers": {
                TableInterface table = PMContainer.createTableFromContainersData();
                System.out.println(table);
            }
            case "Add a container": {
                //Ask to give input to add a container
            }
            case "Update a container": {
                //Send out prompts to collect inputs
            }
            case "Delete a container": {
                //Display port infomration as a table and user choose options that they want to update
            }
        }
    }
}
