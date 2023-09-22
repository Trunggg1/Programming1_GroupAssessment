package DatabaseLinesHandler;

import interfaces.builders.TableInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class LinesHandler {
    public static ArrayList<String> getLinesFromDatabase(String filePath, LineFilters lineFilters){
        try{
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> lines = new ArrayList<>();

            String fileLine;

            ArrayList<HashMap<Integer, String>> filters = null;
            FiltersType type = null;

            if(lineFilters != null){
                filters = lineFilters.getFilters();
                type = lineFilters.getType();
            }

            reader.readLine();

            while ((fileLine = reader.readLine()) != null){
                String[] parts = fileLine.split(",");

                boolean addLine = false;

                if(filters!= null){
                    for(HashMap<Integer, String> filter: filters){
                        Map.Entry<Integer, String> entry = filter.entrySet().iterator().next();
                        int col = entry.getKey() - 1;
                        String fieldName = entry.getValue().trim();

                        String trimmedPartField = parts[col].trim();

                        if(type == FiltersType.EXCLUDE){
                            if(trimmedPartField.equals(fieldName)){
                                addLine = false;
                                break;
                            }else{
                                addLine = true;
                            }
                        }else{
                            if(trimmedPartField.equals(fieldName)){
                                addLine = true;
                            }else{
                                addLine = false;
                                break;
                            }
                        }
                    }
                }else{
                    addLine = true;
                }

                if(addLine){
                    lines.add(fileLine);
                }
            }

            return lines;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String> getLinesFromDatabaseById(String filePath, String id, int colIndex){
        try{
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> lines = new ArrayList<>();

            String fileLine;
            while ((fileLine = reader.readLine()) != null){
                String[] parts = fileLine.split(",");
                String trimmedId = parts[colIndex].trim();

                if(trimmedId.equals(id)){
                    lines.add(fileLine);
                }
            }

            return lines;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String> getLinesFromDatabase(String filePath){
        try{
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> lines = new ArrayList<>();

            String fileLine;

            String header = reader.readLine();

            while ((fileLine = reader.readLine()) != null){
                lines.add(fileLine);
            }

            return lines;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkLineHasId(String filePath, String id, int colIndex){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String fileLine;

            while ((fileLine = reader.readLine()) != null){
                String[] parts = fileLine.split(",");

                String currentId = parts[colIndex];
                if (currentId.equals(id)) {
                    return  true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static boolean updateLinesWithId(String filePath, String id, int colIndex, String line){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();

            String fileLine;
            while ((fileLine = reader.readLine()) != null){
                // Split the line into parts based on a delimiter (e.g., comma or tab)
                String[] parts = fileLine.split(",");

                // Check if the id in the current line matches the target id
                String currentId = parts[colIndex].trim();
                String targetId = id.trim();

                if (currentId.equals(targetId)) {
                    content.append(line).append("\n");
                } else {
                    content.append(fileLine).append("\n");
                }
            }

            reader.close();

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();

            return true;
        }catch (IOException e){
            return  false;
        }
    }
    public static boolean deleteLinesWithId(String filePath, String id, int colIndex){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();

            String fileLine;
            while ((fileLine = reader.readLine()) != null){
                // Split the line into parts based on a delimiter (e.g., comma or tab)
                String[] parts = fileLine.split(",");

                // Check if the id in the current line matches the target id
                String currentId = parts[colIndex];

                if (!currentId.equals(id)) {
                    content.append(fileLine).append("\n");
                }
            }

            reader.close();

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();

            return true;
        }catch (IOException e){
            return  false;
        }
    }
    public static boolean addLineToDatabase(String filePath, String line){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true)); // Use 'true' to append

            writer.newLine();
            writer.write(line);
            writer.close();

            return true;
        }catch (IOException e){
            return  false;
        }
    }
    public static TableInterface createTableFromLines(ArrayList<String> lines, String id, String name, String[] cols, String delimiter){
        TableInterface table = new TableInterface(id, name,cols,delimiter);

        for(String line: lines){
            table.addRow(line);
        }

        return table;
    }
}
