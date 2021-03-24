import java.util.HashMap;
import java.util.List;

public class Database {

    // Each database stores a list of tables contained within it
    private final HashMap<String, Table> tableList = new HashMap<>();
    FileIO file = new FileIO();

    public Database(String databaseName) {
        file.createDatabaseFolder(databaseName);
    }

    public void removeTable(String tableName) {
        tableList.remove(tableName);
    }

    // Adds a table to the Table ArrayList
    void addTable(Table table) {
        tableList.put(table.getTableName().toLowerCase(), table);
    }


    public Table getTable(String tableName) {
        tableName = tableName.replace(";", "");
        return tableList.get(tableName);
    }


    public boolean checkTableExists(String TableName) {
        return tableList.containsKey(TableName);
    }

    public String printJoinCommand(Table firstTable, Table secondTable, String[] firstKeys, String[] secondKeys, String attribute1, String attribute2) {
        List<Entry> entries1 = firstTable.getEntries();
        List<Entry> entries2 = secondTable.getEntries();
        List<String> columns1 = firstTable.getColumns();
        List<String> columns2 = secondTable.getColumns();

        // Counter for the id column
        int id = 1;


        // Prints out the table with all keys and only specified columns line by line by using StringBuilder
        StringBuilder builder = new StringBuilder();
        // Prints out the column titles
        builder.append("id" + "\t");
        for (String c : columns1) {
            if (!c.equals(attribute1)) {
                builder.append(firstTable.getTableName()).append(".").append(c).append("\t");

            }
        }
        for (String c : columns2) {
            if (!c.equals(attribute2)) {
                // Appends the table + attribute name
                builder.append(secondTable.getTableName()).append(".").append(c).append("\t");
            }
        }
        builder.append("\n");

        // Loop through the first keys
        for (int i = 0; i < firstKeys.length; i++) {
            // Loop through the second keys
            for (int j = 0; j < secondKeys.length; j++) {
                // if they match then find the entries for each table
                if (firstKeys[i].equals(secondKeys[j])) {
                    builder.append(id).append("\t");
                    // if the attribute is not the id, then print the elements
                    if (!attribute1.equals("id")) {
                        printSingleElements(entries1, builder, i);
                    } else {
                        printSingleElementsId(entries1, builder, i);

                    }
                    if (!attribute2.equals("id")) {
                        printSingleElements(entries2, builder, j);
                    } else {
                        printSingleElementsId(entries2, builder, j);
                    }
                    builder.append("\n");
                    id++;
                }

            }
        }
        return builder.toString();
    }

    static void printSingleElementsId(List<Entry> entries, StringBuilder builder, int location) {
        // Loop through each entry
        for (int x = 0; x < entries.get(location).getElements().size(); x++) {
            if (!(entries.get(location).getKeyAsString().replace("'", "")).trim().equals("null")) {
                builder.append(entries.get(location).getSingleElement(x).replace("'", "").trim()).append("\t");
            }
            builder.append("\t");
        }
    }

    static void printSingleElements(List<Entry> entries, StringBuilder builder, int location) {
        for (int x = 0; x < entries.get(location).getElements().size(); x++)
            if (!(entries.get(location).getKeyAsString().replace("'", "")).trim().equals("null")) {
                    builder.append(entries.get(location).getSingleElement(x).replace("'", "").trim()).append("\t");
            }
    }
}
