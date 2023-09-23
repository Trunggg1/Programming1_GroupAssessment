package LinesHandler;

import interfaces.builders.TableInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LinesHandler {
    public static boolean checkIfLineIsFiltered(ArrayList<HashMap<String, Object>> filters, String[] parts){
        boolean meetRequirement = false;

        for(HashMap<String, Object> filter: filters){
            int column = (int) filter.get("column") - 1;
            String value = (String) filter.get("value");
            FiltersType type = (FiltersType) filter.get("type");

            String trimmedValue = value.trim();
            String trimmedPartField = parts[column].trim();

            if(type == FiltersType.EXCLUDE){
                if(trimmedPartField.equals(trimmedValue)){
                    meetRequirement = false;
                    break;
                }else{
                    meetRequirement = true;
                }
            }else{
                if(trimmedPartField.equals(trimmedValue)){
                    meetRequirement = true;
                }else{
                    meetRequirement = false;
                    break;
                }
            }
        }

        return meetRequirement;
    }
    public static ArrayList<String> getLinesFromDatabase(String filePath, LineFilters lineFilters){
        try{
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> lines = new ArrayList<>();

            String fileLine;

            reader.readLine();

            ArrayList<HashMap<String, Object>> filters = null;
            if(lineFilters!= null){
                filters = lineFilters.getFilters();
            }

            while ((fileLine = reader.readLine()) != null){
                String[] parts = fileLine.split(",");

                boolean addLine;

                if(filters!= null){
                    addLine = checkIfLineIsFiltered(filters, parts);
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
    public static boolean updateLinesFromDatabase(String filePath, String line, LineFilters lineFilters){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();

            String fileLine;

            ArrayList<HashMap<String, Object>> filters = null;

            if(lineFilters != null){
                filters = lineFilters.getFilters();
            }

            while ((fileLine = reader.readLine()) != null){
                // Split the line into parts based on a delimiter (e.g., comma or tab)
                String[] parts = fileLine.split(",");

                boolean updateLine;

                if(filters!= null){
                    updateLine = checkIfLineIsFiltered(filters, parts);
                }else{
                    updateLine = true;
                }

                if(updateLine){
                    content.append(line).append("\n");
                }else{
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
    public static boolean deleteLinesWithId(String filePath, LineFilters lineFilters){
        try {
            File file = new File(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();

            String fileLine;

            ArrayList<HashMap<String, Object>> filters = null;

            if(lineFilters != null){
                filters = lineFilters.getFilters();
            }

            while ((fileLine = reader.readLine()) != null){
                // Split the line into parts based on a delimiter (e.g., comma or tab)
                String[] parts = fileLine.split(",");

                boolean deleteLine;

                if(filters!= null){
                    deleteLine  =  checkIfLineIsFiltered(filters,parts);
                }else{
                    deleteLine = false;
                }

                if(!deleteLine){
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
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));

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
