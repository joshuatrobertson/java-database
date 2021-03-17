import java.util.*;

public class Table {

    private String tableName;
    private List<String> columns = new ArrayList<String>();
    private List<Entry> entries = new ArrayList<Entry>();
    private HashMap<String, Table> tableList = new HashMap<>();
    private int primaryKey;

    public Table(String tableName) {
        this.tableName = tableName;
        // Set the starting value for the primary key
        primaryKey = 0;
    }

    // Adds a column into the ArrayList
    void addColumn(String columnName) {
        columns.add(columnName);
    }

    // Given a column name, return the column id
    Integer getTableId(String columnName) {
        for (int i = 0; i < columns.size(); i++)
            // Loop through checking the column name at position[i] against the given columnName
            // and return the index if found
            if (columns.get(i).toLowerCase().equals(columnName)){
                return i;
            }
        // If not found return nul
        return null;
    }


    // When a column is added after the table has been initialised, fill
    // the row with null values
    void addNewColumn(String columnName) {
        // Add a new column
        columns.add(columnName);
        // Fill the array with 'void' so that the list can be accessed
        // at that index
        for (int i = 0; i < entries.size(); i++){
            entries.get(i).addNewNullElement();
        }
    }

    public String getTableName() { return tableName; }

    void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    int getNumberOfColumns() { return columns.size(); }

    int getNumberOfEntries() { return entries.size(); }

    List<Entry> getEntries() { return entries; }

    // Takes in a comma separated string and adds it to the records ArrayList
    void insertNewEntry(String entry) {
        // Increase the primary key by one with each record
        primaryKey++;

        // Split the incoming record by commas
        String[] splitRecord = entry.split(",");
        Entry newEntries = new Entry(primaryKey, splitRecord);;
        entries.add(newEntries);
    }

    void insertEntry(Entry entry) { entries.add(entry); }

    // Delete a record from the arrayList
    void deleteRecord(int key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getKey() == key) {
                entries.remove(i);
            }
        }
    }

    // Deletes a row with the associated data
    void deleteRow(int rowNumber) {
        columns.remove(rowNumber);
        for (int i = 0; i < entries.size(); i++) {
            entries.remove(rowNumber);
        }
    }

    void updateRecord(int key, int itemLocation, String newRecord) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getKey() == key) {
                entries.get(i).updateElement(itemLocation, newRecord);
            }
        }
    }


    // Prints out the table line by line by using StringBuilder to create the string
    String printFullTable() {
        StringBuilder builder = new StringBuilder();
        builder.append("Key" + "\t");
        for (String c : columns) {
            builder.append(c + "\t");
        }
        builder.append("\n");
        // Loop through each entry
        for (int i = 0; i < entries.size(); i++) {
            // Add the keys
            builder.append(entries.get(i).getKey() + "\t");
            // Loop through each sub entry to get individual entries
            for (int j = 0; j < entries.get(i).getElements().size(); j++) {
                builder.append(entries.get(i).getSingleElement(j) + "\t");
            }
            builder.append("\n");
        }
        String finalText = builder.toString();
        return finalText;
    }

    // Prints out the table with all keys and only specified columns line by line by using StringBuilder
    String printPartialTableAllKeys(List<Integer> columnIds) {
        StringBuilder builder = new StringBuilder();
        // Prints out the column titles
        builder.append("Key" + "\t");
        for (Integer column : columnIds) {
            builder.append(columns.get(column) + "\t");
        }
        builder.append("\n");
        // Loop through each entry
        for (int i = 0; i < entries.size(); i++) {
            // Add the keys
            builder.append(entries.get(i).getKey() + "\t");
            // Loop through each sub entry to get individual entries
            for (Integer column : columnIds) {
                builder.append(entries.get(i).getSingleElement(column)+ "\t");
            }
            builder.append("\n");
        }
        String finalText = builder.toString();
        return finalText;
    }

    // Prints out partial table for searches
    String printPartialTable(List<Integer> keys, List<Integer> columnIds) {
        StringBuilder builder = new StringBuilder();
        // Prints out the column titles
        builder.append("Key" + "\t");
        for (Integer column : columnIds) {
            builder.append(columns.get(column - 1) + "\t");
        }
        builder.append("\n");

        // Loop through keys and see if any match,
        for (Integer key : keys) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getKey() == key) {
                    // If a key matches print the key
                    builder.append(entries.get(i).getKey() + "\t");
                    // Then iterate through the columns and print only the columns given in the list 'Columns'
                    for (Integer column : columnIds) {
                        builder.append(entries.get(i).getSingleElement(column - 1)+ "\t");
                    }
                }
            } builder.append("\n");
        }
        String finalText = builder.toString();
        return finalText;
    }
}
