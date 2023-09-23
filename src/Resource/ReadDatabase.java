package Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadDatabase {

    // Read all of a file's content within a specific column.
    public static String[] readColString(int col, String filepath, String delimiter) throws IOException {
        String[] data;
        String currentLine;
        ArrayList<String> colData = new ArrayList<>();

        try {
            // read a specific text file
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Break and skip data when encounter an assigned symbol (in this most of the cases, it will be the comma ","
            while ((currentLine = bufferedReader.readLine()) != null) {
                data = currentLine.split(delimiter);
                colData.add(data[col]);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return colData.toArray(new String[0]);
    }

    // Read all data
    public static ArrayList<String[]> readAllLines(String filepath) {
        String[] data;
        String currentLine;
        ArrayList<String[]> allFileData = new ArrayList<>();

        try {
            // read a specific text file
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Break when encounter ","
            while ((currentLine = bufferedReader.readLine()) != null) {
                data = currentLine.split(",");
                allFileData.add(data);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return allFileData;
    }

    // Read all of a file's content within a specific line.
    public static String[] readSpecificLine(String search, int col, String filepath, String delimiter) {
        String[] lineData;
        String currentLine;

        try {
            // read a specific text file
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

           // break and skip lines except assigned symbols were set
            while ((currentLine = bufferedReader.readLine()) != null) {
                lineData = currentLine.split(delimiter);
                if (lineData[col].equals(search)) {
                    return lineData;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return new String[0];
    }
}
