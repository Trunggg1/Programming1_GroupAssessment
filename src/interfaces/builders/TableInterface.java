package interfaces.builders;
import interfaces.abstracts.Interface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TableInterface extends Interface {
    private String id;
    private String name;
    private String[] cols;
    private String delimiter;
    private HashMap<Number, String[]> rows = new HashMap<>();
    private HashMap<String, String> options;
    public TableInterface(String id, String name, String[] cols, String delimiter) {
        this.id = id;
        this.name = name;
        this.cols = cols;
        this.delimiter = delimiter;
    }
    private String[] getPartsFromLine(String line){
        //Break a line data into many String[] parts
        return line.split(delimiter);
    }
    private boolean hasSameColsLength(String[] parts){
        //Check if the parts has same length with columns defined above
        return parts.length == cols.length;
    }
    public boolean addRow(String line){
        String[] parts = getPartsFromLine(line);

        if(!hasSameColsLength(parts)) return false;

        int count = rows.size() + 1;
        //Store index and parts data to rows
        rows.put(count,parts);

        return  true;
    }
    public boolean setRow(int index, String line){
        String[] parts = getPartsFromLine(line);

        if(!hasSameColsLength(parts)) return  false;

        //Set a row with that index with new data
        rows.put(index, parts);

        return  true;
    }
    public boolean removeRow(int index){
        //Find and remove the row with that index
        if (rows.containsKey(index)) {
            rows.remove(index);
            return true;
        } else {
           return  false;
        }
    }
    public String toString(){
        //predefine decoration string
        int gap = 2;
        String horizontalLine = "-";
        String intersection = "+";
        String verticalLine= "|";

        int[] longestTextLength = new int[cols.length];

        int count = 0;

        //Set default the highest length is the column string length
        for (String col : cols) {
            longestTextLength[count] = col.length();
            count++;
        }

        count = 0;

        //Find and return the highest string length for each column's value
        for(Map.Entry<Number, String[]> entry : rows.entrySet()){
            Number index = entry.getKey();
            String[] row = entry.getValue();

            for(int i = 0; i < row.length; i ++){
                String value = row[i];
                if(value.matches("^\\s+.*")){
                    row[i] = value.replaceFirst("^\\s+", "");
                }

                if (longestTextLength[count] < row[i].length()) {
                    longestTextLength[count] = row[i].length();
                }
                count++;
            }

            count = 0;
        }

        StringBuilder table = new StringBuilder();

        //Print out table name
        table.append(name);
        table.append("\n");

        //Generate a line for table
        StringBuilder line = new StringBuilder();
        for (int j : longestTextLength) {
            line.append(intersection).append(horizontalLine.repeat(j + gap));
        }
        line.append(intersection);

        String tableHorizontalLine = line.toString();

        //Print out the first table line
        table.append(tableHorizontalLine).append("\n").append(verticalLine);

        //Print out columns in the middle
        for(int i = 0; i < cols.length; i++){
            String col = cols[i];

            String leftSpacing = " ";
            String rightSpacing = " ";

            int spacing = longestTextLength[i] + gap - col.length();

            if(spacing %  2 != 0){
                leftSpacing = leftSpacing.repeat(spacing - 2);
                rightSpacing = rightSpacing.repeat(spacing - 1);
            }else{
                leftSpacing = leftSpacing.repeat(spacing/ 2);
                rightSpacing = rightSpacing.repeat(spacing/ 2);

            }

            table.append(leftSpacing);
            table.append(col);
            table.append(rightSpacing);

            table.append(verticalLine);
        }

        //Print to close the border for column text
        table.append("\n").append(tableHorizontalLine);

        //Start a loop to generate each row in table as a string
        for(Map.Entry<Number, String[]> entry : rows.entrySet()){
            String[] row = entry.getValue();

            table.append("\n");
            table.append(verticalLine);

            for(int i = 0; i < row.length; i++){
                String value = row[i];

                if(value.length() < longestTextLength[i]){
                    value = value + " ".repeat(longestTextLength[i] - value.length() + gap);
                }else{
                    value = value + " ".repeat(gap);
                }

                table.append(value);
                table.append(verticalLine);
            }

            //After row finished, print out table line to close the border
            table.append("\n");
            table.append(tableHorizontalLine);
        }

        return table.toString();
    }
}
