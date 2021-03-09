import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private int primaryKey;
    private List<String> elements = new ArrayList<String>();

    public Record(int key, String @NotNull [] records) {
        primaryKey = key;
        // Add in the entries and column names
        for (int i = 0; i < records.length; i++) {
            this.elements.add(records[i]);
        }
    }

    public void addNewNullElement() { elements.add("null"); }

    public int getKey() { return primaryKey; }

    public List<String> getElements() { return elements; }

    public String getSingleElement(Integer i) { return elements.get(i); }

    public void updateElement(int index, String newElement) {elements.set(index, newElement); }
}
