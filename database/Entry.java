import java.util.ArrayList;
import java.util.List;

public class Entry {

    private int primaryKey;
    private List<String> elements = new ArrayList<String>();

    public Entry(int key, String [] records) {
        primaryKey = key;
        // Add in the entries and column names
        for (int i = 0; i < records.length; i++) {
            this.elements.add(records[i]);
        }
    }


    public void addNewNullElement() { elements.add("null"); }

    public int getKey() { return primaryKey; }

    public List<String> getElements() { return elements; }

    public String[] getElementsAsString() {

        String[] NewElements = new String[elements.size()];
        for (int i = 0; i < NewElements.length; i++) {
            NewElements[i] = elements.get(i).trim();
        }
        return NewElements;
    }

    public String getSingleElement(Integer i) { return elements.get(i); }

    public void updateElement(int index, String newElement) {elements.set(index, newElement); }

    public void removeElement(int index) { elements.remove(index); }
}
