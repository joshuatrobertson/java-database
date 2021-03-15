import java.util.HashMap;
import java.util.List;

public class UseCommand extends MainCommand {

    Database currentDatabase;
    String databaseName;
    public UseCommand(String[] incomingCommand, HashMap databases) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        databaseName = tokenizedText[1].replace(";", "");
    }

    public String run() { return readInTablesFromFile(); }

    public String getCurrentDatabase() { return databaseName; }

    private String readInTablesFromFile() {
        if (file.checkFolderExists(databaseName)) {
            Database database = new Database(databaseName);
            databases.put(databaseName, database);
            // Add the tables to the database
            List<Table> listOfTables = file.readTablesFromDatabase(databaseName);
            for (Table tables : listOfTables) {
                database.addTable(tables);
            }
            return printOk();
        }
        else {
            return printError("Unknown database");
        }
    }
}
