package Resource;

import java.util.Scanner;

// collect input of the users (1,2,3 etc)
public class UserInput {
    public static String rawInput() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Please enter the number to select an option:  ");
            return sc.nextLine();
        }
    }
}
