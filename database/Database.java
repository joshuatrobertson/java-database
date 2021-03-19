import java.util.HashMap;
import java.util.Locale;

public class Database {

    // Each database stores a list of tables contained within it
    private HashMap<String, Table> tableList = new HashMap<>();
    FileIO file = new FileIO();
    private String databaseName;

    public Database(String databaseName) {
        this.databaseName = databaseName;
        file.createDatabaseFolder(databaseName);
    }

    public void removeTable(String tableName) { tableList.remove(tableName); }

    // Adds a table to the Table ArrayList
    void addTable(Table table) {
        tableList.put(table.getTableName().toLowerCase(), table);
    }

    // Getter function to return the list of tables within the database
    private HashMap<String, Table> getTableList() {
        return tableList;
    }

    public Table getTable(String tableName) {
        tableName = tableName.replace(";", "");
        return tableList.get(tableName); }

    public String getDatabaseName() { return databaseName; }

    public boolean checkTableExists(String Tablename) { return tableList.containsKey(Tablename); }

}
