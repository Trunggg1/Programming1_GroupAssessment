package interfaces.builders;

import interfaces.abstracts.Interface;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class OptionsInterface extends Interface {
    private HashMap<Integer, OptionsInterface> childOptionsInterface = new HashMap<>();
    private String id;
    private String name;
    private int width = 2;
    private TreeMap<Integer, String> options = new TreeMap<>();
    private TreeMap<Integer, String> optionsData = new TreeMap<>();
    public OptionsInterface(String id, String name, int width) {
        this.id = id;
        this.name = name;
        this.width = width;
    }
    private HashMap<String, String> getInterfaceData(int number){
        HashMap<String, String> interfaceData = new HashMap<>();
        interfaceData.put("id", id);
        interfaceData.put("number", String.valueOf(number));
        interfaceData.put("option", options.get(number));
        interfaceData.put("data", optionsData.get(number));

        return interfaceData;
    }
    public void clearOptions(){
        this.options.clear();
        this.childOptionsInterface.clear();
        this.optionsData.clear();
    }
    public void addOption(int number, String optionName, String optionData,OptionsInterface childInterface){
        this.options.put(number, optionName);
        this.optionsData.put(number, optionData);
        this.childOptionsInterface.put(number, childInterface);
    }
    public void removeOption(int number){
        this.options.remove(number);
        this.optionsData.remove(number);
        this.childOptionsInterface.remove(number);
    }
    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        int gap = 5;
        int textLongestLength = 0;
        int index = 0;
        String[] texts = new String[options.size()];

        for (Map.Entry<Integer, String> entry : options.entrySet()) {
            Integer optionNumber = entry.getKey();
            String optionText = entry.getValue();

            String text = optionNumber + ". " + optionText;

            if (textLongestLength < text.length()) {
                textLongestLength = text.length();
            }

            texts[index] = text;
            index++;
        }

        for (int i = 0; i < texts.length; i++) {
            int textLength = texts[i].length();
            if (textLength < textLongestLength) {
                texts[i] = texts[i] + " ".repeat(textLongestLength - textLength);
            }
        }

        String line = "=".repeat(textLongestLength * width + (gap * width));

        StringBuilder gui = new StringBuilder();
        gui.append(line).append("\n");
        gui.append("|").append(" ".repeat(Math.abs((line.length() - name.length()) / 2))).append(name).append("\n");
        gui.append(line).append("\n");

        int count = 1;
        for (String text : texts) {
            if (count == width) {
                gui.append(" ".repeat(gap - 1)).append(text);
                gui.append("\n");
                count = 1;
            } else {
                if (count == 1) {
                    gui.append("|").append(" ".repeat(gap - 2)).append(text);
                } else {
                    gui.append(" ".repeat(gap - 1)).append(text);
                }
                count++;
            }
        }
        return gui.toString();
    }
    public HashMap<String, String> run(String interfaceId){
        boolean keepRunning = true;
        Scanner input = new Scanner(System.in);
        int count = 0;

        String interfaceTexts = toString();

        if(interfaceId != null){
            for (Map.Entry<Integer, OptionsInterface> entry : childOptionsInterface.entrySet()) {
                OptionsInterface childInterface = entry.getValue();

                if(childInterface!= null){
                    if(childInterface.getId().equals(interfaceId)){
                        HashMap<String, String> interfaceData = childInterface.run(null);

                        if(interfaceData != null){
                            return  interfaceData;
                        }
                        break;
                    }
                }
            }
        }

        while (keepRunning){

            if(count < 2){
                System.out.println(interfaceTexts);
            }else{
                count = 0;
            }
            System.out.print("Input: ");

            String inputResult = input.nextLine();

            if(inputResult.matches("\\d+")){
                Integer number = Integer.parseInt(inputResult);
                if(options.containsKey(number)){
                    String textOption = options.get(number);

                    switch (textOption){
                        case "Go back", "Exit":{
                            keepRunning = false;

                            break;
                        }
                        default:{
                            OptionsInterface childInterface = childOptionsInterface.get(number);
                            if(childInterface != null){
                                HashMap<String, String> data = childInterface.run(null);

                                if(data != null){
                                    return data;
                                }
                            }else{
                                return getInterfaceData(number);
                            }
                        }
                    }

                }else{
                    System.out.println("There is no option for input " + inputResult);
                    count++;
                }
            }else{
                System.out.println("There is no option for input " + inputResult);
            }
        }

        return null;
    }
}