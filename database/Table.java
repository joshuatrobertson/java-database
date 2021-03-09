import java.util.*;

public class Table {

    private String tableName;
    private List<String> columns = new ArrayList<String>();
    private List<Record> records = new ArrayList<Record>();
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

    // When a column is added after the table has been initialised, fill
    // the row with null values
    void addNewColumn(String columnName) {
        // Add a new column
        columns.add(columnName);
        // Fill the array with 'void' so that the list can be accessed
        // at that index
        for (int i = 0; i < records.size(); i++){
            records.get(i).addNewNullElement();
        }
    }

    void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    int getNumberOfColumns() { return columns.size(); }

    int getNumberOfRecords() { return records.size(); }

    List<Record> getRecords() { return records; }

    // Takes in a comma separated string and adds it to the records ArrayList
    void insertRecord(String record) {
        // Increase the primary key by one with each record
        primaryKey++;

        // Split the incoming record by commas
        String[] splitRecord = record.split(",");
        Record newRecord = new Record(primaryKey, splitRecord, columns, false);;
        records.add(newRecord);

    }

    // Delete a record from the arrayList
    void deleteRecord(int key) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getKey() == key) {
                records.remove(i);
            }
        }
    }

    // Deletes a row with the associated data
    void deleteRow(int rowNumber) {
        columns.remove(rowNumber);
        for (int i = 0; i < records.size(); i++) {
            records.remove(rowNumber);
        }
    }

    void updateRecord(int key, int itemLocation, String newRecord) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getKey() == key) {
                records.get(i).setRecord(itemLocation, newRecord);
            }
        }
    }


    // Prints out the table line by line
    void printFullTable() {
        System.out.print("Key" + "\t");
        System.out.print(columns + "\n");

        for (int i = 0; i < records.size(); i++) {
            System.out.print(records.get(i).getKey() + "\t");
            System.out.print(records.get(i).getElements() + "\n");
        }
    }

    // Prints out partial table for searches
    void printPartialTable(List<Integer> keys, List<Integer> Columns) {
        // Prints out the column titles
        System.out.print("Key" + "\t");
        for (Integer column : Columns) {
            System.out.print(columns.get(column - 1) + "\t");
        }
        System.out.print("\n");

        // Loop through keys and see if any match,
        for (Integer key : keys) {
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i).getKey() == key) {
                    // If a key matches print the key
                    System.out.print(records.get(i).getKey() + "\t");
                    // Then iterate through the columns and print only the columns given in the list 'Columns'
                    for (Integer column : Columns) {
                        System.out.print(records.get(i).getSingleElement(column - 1)+ "\t");
                    }
                }
            } System.out.println("\n");
        }
    }
}
