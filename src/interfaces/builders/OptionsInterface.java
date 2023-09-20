package interfaces.builders;

import interfaces.abstracts.Interface;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OptionsInterface extends Interface {
    private HashMap<String, OptionsInterface> childOptionsInterface = new HashMap<>();
    private String id;
    private String name;
    private int width = 2;
    private HashMap<String, String> options = new HashMap<>();
    public OptionsInterface(String id, String name, int width) {
        this.id = id;
        this.name = name;
        this.width = width;
    }
   public void addOption(int number, String description, OptionsInterface childInterface){
        options.put(String.valueOf(number), description);
        childOptionsInterface.put(String.valueOf(number), childInterface);
    }
    public void removeOption(int number){
        options.remove(String.valueOf(number));
        childOptionsInterface.remove(String.valueOf(number));
    }
    public String getId() {
        return id;
    }
    public HashMap<String, String> getOptions() {
        return options;
    }
    public String toString() {
        int gap = 5;
        int textLongestLength = 0;
        int index = 0;
        String[] texts = new String[options.size()];

        for (Map.Entry<String, String> entry : options.entrySet()) {
            String optionNumber = entry.getKey();
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
            for (Map.Entry<String, OptionsInterface> entry : childOptionsInterface.entrySet()) {
                OptionsInterface childInterface = entry.getValue();

                if(childInterface.getId().equals(interfaceId)){
                    HashMap<String, String> result = childInterface.run(null);

                    if(result != null){
                        return  result;
                    }

                    break;
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

            if(options.containsKey(inputResult)){
                String textOption = options.get(inputResult);

                switch (textOption){
                    case "Go back", "Exit":{
                        keepRunning = false;

                        break;
                    }
                    default:{
                        OptionsInterface childInterface = childOptionsInterface.get(inputResult);
                        if(childInterface != null){
                            HashMap<String, String> data = childInterface.run(null);

                            if(data != null){
                                return data;
                            }
                        }else{
                            HashMap<String,String> data = new HashMap<>();

                            data.put("id",id);
                            data.put("option",textOption);
                            return data;
                        }
                    }
                }

            }else{
                System.out.println("There is no option for input " + inputResult);
                count++;
            }
        }

        return null;
    }
}