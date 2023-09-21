package interfaces.builders;

import interfaces.abstracts.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PromptsInterface extends Interface
{
    private String id;
    private String name;
    private ArrayList<String> prompts = new ArrayList<>();
    public PromptsInterface(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void addPrompt(String prompt){
        prompts.add(prompt);
    }
    public void clearPrompt(){
        prompts.clear();
    }
    public HashMap<Number, String> startPrompts(){
        Scanner input = new Scanner(System.in);

        HashMap<Number, String> data = new HashMap<>();

        System.out.println("=====================");
        System.out.println(name);

        int i = 1;

        for (String prompt : prompts) {
            System.out.println(prompt);
            System.out.print("Input: ");

            String inputResult = input.nextLine();

            data.put(i, inputResult);
            i++;
        }

        return data;
    }
}
