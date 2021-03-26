import java.util.*;

public class Table {

    private final String tableName;
    private final List<String> columns = new ArrayList<>();
    private final List<Entry> entries = new ArrayList<>();
    private int primaryKey;

    public Table(String tableName) {
        this.tableName = tableName;
        // Set the starting value for the primary key
        primaryKey = 0;
    }

    // Adds a column into the ArrayList
    void addColumn(String attribute) {
        columns.add(attribute);
    }

    public List<String> getColumns() { return columns; }

    public Integer getAttributePosition(String attributeName) {
        for (String attribute : columns) {
            if (attribute.trim().replace(";", "").equalsIgnoreCase(attributeName)) {
                return columns.indexOf(attribute);
            }
        }
        return null;
    }

    // Given a column name, return the column id
    Integer getTableId(String columnName) {
        for (int i = 0; i < columns.size(); i++)
            // Loop through checking the column name at position[i] against the given columnName
            // and return the index if found
            if (columns.get(i).equalsIgnoreCase(columnName)){
                return i;
            }
        // If not found return nul
        return null;
    }

    // Iterate through the entries and add each one contained within [columnName] to an integer array
    public String[] getForeignKeys(String columnName) {
        String[] items = new String[entries.size()];
        int column = getAttributePosition(columnName);
        for (int i = 0; i < entries.size(); i++) {
            items[i] = entries.get(i).getSingleElement(column).trim();
        }
        return items;
    }


    // When an attribute is added after the table has been initialised, fill
    // the row with null values
    void addNewAttribute(String attributeName) {
        // Add a new column
        columns.add(attributeName);
        // Fill the array with 'void' so that the list can be accessed
        // at that index
        for (Entry entry : entries) {
            entry.addNewNullElement();
        }
    }

    public String getTableName() { return tableName; }

    void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String[] getAttributes() { return columns.toArray(new String[0]) ; }

    public ValueType checkAttributeType(String attributeName) {
        int position = getAttributePosition(attributeName);
        // Check the first item in the list
        String itemToCheck = entries.get(0).getSingleElement(position).trim();

        if (itemToCheck.contains("'")) {
            return ValueType.STRING_LITERAL;
        }
        else if (itemToCheck.equals("true") || itemToCheck.equals("false")) {
            return ValueType.BOOLEAN_LITERAL;
        }
        else if (itemToCheck.matches("^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$")) {
            return ValueType.FLOAT_LITERAL;
        }
    return ValueType.ERROR;
    }

    public String[] getPrimaryKeys() {
        String[] primaryKeys = new String[entries.size()];

        for (int i = 0; i < primaryKeys.length; i++) {
            int entry;
            entry = entries.get(i).getKey();
            primaryKeys[i] = Integer.toString(entry);
        }
        return primaryKeys ;
    }

    List<String[]> getRecords() {
        List<String[]> records = new ArrayList<>();

        for (Entry entry : entries) {
            records.add(entry.getElementsAsString());
        }
        return records;
    }

    int getNumberOfColumns() { return columns.size(); }

    List<Entry> getEntries() { return entries; }

    // Takes in a comma separated string and adds it to the records ArrayList
    public void insertNewEntry(String entry) {
        // Increase the primary key by one with each record
        primaryKey++;

        // Split the incoming record by commas
        String[] splitRecord = entry.split(",");
        Entry newEntries = new Entry(primaryKey, splitRecord);
        entries.add(newEntries);
    }

    void insertEntry(Entry entry) { entries.add(entry); }

    // Delete a record from the arrayList
    void deleteRecord(int key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getKey() == key) {
                entries.remove(i);
                return;
            }
        }
    }

    public boolean checkAttributeExists(String attributeName) { return columns.contains(attributeName); }

    // Deletes a row with the associated data
    void deleteAttribute(String attributeName) {
        int column = getAttributePosition(attributeName);
        columns.remove(column);
        for (Entry entry : entries) {
            entry.removeElement(column);
        }
    }

    // Prints out the table line by line by using StringBuilder to create the string
    String printFullTable() {
        StringBuilder builder = new StringBuilder();
        builder.append("id" + "\t");
        for (String c : columns) {
            builder.append(c).append("\t");
        }
        builder.append("\n");
        // Loop through each entry
        for (int i = 0; i < entries.size(); i++) {
            // Add the keys
            builder.append(entries.get(i).getKey()).append("\t");
            // Loop through each sub entry to get individual entries, -1 means that it will print off every entry
            Database.printSingleElements(entries, builder, i);
            builder.append("\n");
        }
        return builder.toString();
    }

    // Prints out the table with all keys and only specified columns line by line by using StringBuilder
    String printPartialTableAllKeys(List<Integer> columnIds) {
        StringBuilder builder = new StringBuilder();
        // Prints out the column titles
        builder.append("id" + "\t");
        for (Integer column : columnIds) {
            builder.append(columns.get(column)).append("\t");
        }
        builder.append("\n");
        // Loop through each entry
        for (Entry entry : entries) {
            // Add the keys
            builder.append(entry.getKey()).append("\t");
            // Loop through each sub entry to get individual entries
            for (Integer column : columnIds) {
                if (!(entry.getSingleElement(column).replace("'", "")).trim().equals("null")) {
                    builder.append(entry.getSingleElement(column).replace("'", "").trim()).append("\t");
                } else {
                    builder.append("\t");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    // Prints out the table with all keys and only specified columns line by line by using StringBuilder
    String printPartialTableAllColumns(List<Integer> keys) {
        StringBuilder builder = new StringBuilder();
        // Prints out the column titles
        builder.append("id" + "\t");
        for (String c : columns) {
            builder.append(c).append("\t");
        }
        builder.append("\n");

        // Loop through keys and see if any match,
        for (Integer key : keys) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getKey() == key) {
                    // If a key matches print the key
                    builder.append(entries.get(i).getKey()).append("\t");
                    // Then iterate through the columns and print only the columns given in the list 'Columns'
                    // Loop through each sub entry to get individual entries
                    Database.printSingleElements(entries, builder, i);
                    builder.append("\n");
                }
            }
        }
        return builder.toString();
    }

    // Prints out partial table for searches
    String printPartialTable(List<Integer> keys, List<Integer> columnIds) {
        StringBuilder builder = new StringBuilder();
        // Prints out the column titles
        builder.append("id" + "\t");
        for (Integer column : columnIds) {
            builder.append(columns.get(column)).append("\t");
        }
        builder.append("\n");

        // Loop through keys and see if any match,
        for (Integer key : keys) {
            for (Entry entry : entries) {
                if (entry.getKey() == key) {
                    // If a key matches print the key
                    builder.append(entry.getKey()).append("\t");
                    // Then iterate through the columns and print only the columns given in the list 'Columns'
                    for (Integer column : columnIds) {
                        if (!(entry.getSingleElement(column).replace("'", "")).trim().equals("null")) {
                            builder.append(entry.getSingleElement(column).replace("'", "").trim()).append("\t");
                        } else {
                            builder.append("\t");
                        }
                    }
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }


}
