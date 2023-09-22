package DatabaseLinesHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class LineFilters {
    private FiltersType type;
    private ArrayList<HashMap<Integer, String>> filters = new ArrayList<>();
    public LineFilters(FiltersType type) {
        this.type = type;
    }
    public FiltersType getType() {
        return type;
    }
    public void addFilter(int col, String fieldName) {
        HashMap<Integer, String> filter = new HashMap<>();

        filter.put(col, fieldName);

        filters.add(filter);
    }
    public void clearFilters() {
        filters.clear();
    }
    public ArrayList<HashMap<Integer, String>> getFilters() {
        return filters;
    }
}
