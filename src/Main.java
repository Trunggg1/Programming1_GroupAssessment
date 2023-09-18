/*
COSC2081 GROUP ASSIGNMENT
CONTAINER PORT MANAGEMENT SYSTEM
Instructor: Mr. Minh Vu & Dr. Phong Ngo
Group: Group_Work
S3891483, Vu Loc

 */

import Menu.Authentication;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        Authentication authentication = new Authentication();
        authentication.mainMenu();
    }
}