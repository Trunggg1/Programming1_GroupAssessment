package LinesHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class LineFilters {
    private ArrayList<HashMap<String, Object>> filters = new ArrayList<>();
    public void addFilter(int lineColumn, String fieldValue, FiltersType type) {
        HashMap<String, Object> filter = new HashMap<>();

        filter.put("column", lineColumn);
        filter.put("value", fieldValue);
        filter.put("type", type);

        filters.add(filter);
    }
    public void clearFilters() {
        filters.clear();
    }
    public ArrayList<HashMap<String, Object>> getFilters() {
        return filters;
    }
}
