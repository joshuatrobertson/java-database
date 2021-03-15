import java.util.HashMap;
import java.util.List;

public class UseCommand extends MainCommand {

    Database currentDatabase;

    public UseCommand(String[] incomingCommand, HashMap databases, Database currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        this.currentDatabase = currentDatabase;
    }

    public String run() {
        String databaseName = tokenizedText[1].replace(";", "");
        if (file.checkFolderExists(databaseName)) {
            Database database = new Database(databaseName);
            databases.put(databaseName, database);
            // Add the tables to the database
            List<Table> listOfTables = file.readTablesFromDatabase(databaseName);
            for (Table tables : listOfTables) {
                database.addTable(tables);
            }
            currentDatabase = database;
            return printOk();
        }
        else {
            return printError("Unknown database");
        }
    }

    public Database getCurrentDatabase() { return (Database) databases.get(currentDatabase); }


}
