package Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteFile {

    // rewrite all content in a specific file
    public static void rewriteFile(String filepath, String header, String data) throws IOException {
        FileWriter newFile = new FileWriter(filepath, true);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));

        if (filepath.length() == 0) { // check if data is empty
            newFile.append(header); // write header
            newFile.append("\n"); // append for next command
        }
        bufferedReader.close();

        try {
            // empty arraylist await to assigned
            ArrayList<String> newDatabase = new ArrayList<>();

            newDatabase.add(data);

            // write data into new arraylist
            for (String s : newDatabase) {
                newFile.append(s);
                newFile.append("\n");
            }
            newFile.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}

