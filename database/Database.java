import java.util.ArrayList;
import java.util.List;

public class Database {

    // Each database stores a list of tables contained within it
    private List<Table> tableList = new ArrayList<Table>();
    private String databaseName;

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    // Adds a table to the Table ArrayList
    void addTable(Table tableName) {
        tableList.add(tableName);
    }

    // Getter function to return the list of tables within the database
    private List<Table> getTableList() {
        return tableList;
    }

}
