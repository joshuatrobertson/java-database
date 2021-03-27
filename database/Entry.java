import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Entry {

    private final int primaryKey;
    private final List<String> elements = new ArrayList<>();

    public Entry(int key, String [] records) {
        primaryKey = key;
        // Add in the entries and column names
        this.elements.addAll(Arrays.asList(records));
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
