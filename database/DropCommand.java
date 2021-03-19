import java.io.File;
import java.util.HashMap;

public class DropCommand extends MainCommand {

    String command;

    public DropCommand(String[] incomingCommand, HashMap databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
        command = tokenizedText[1];
    }

    public String run() {
        if (command.equals("table")) {
            return dropTable();
        }
        return dropDatabase();
    }

    private String dropTable() {
        currentTable = tokenizedText[2].replace(";", "");
        if (checkTableExists()) {
            databases.get(currentDatabase).removeTable(currentTable);
            file.dropTable(currentTable, currentDatabase);
            return printOk();
        }

        return printError("Table does not exist");    }

    private String dropDatabase() {
        currentDatabase = tokenizedText[2].replace(";", "");
        if (databases.containsKey(currentDatabase)) {
            // Remove from memory
            databases.remove(currentDatabase);
            // Remove from persistent storage
            File database = new File("files" + File.separator + currentDatabase);
            file.dropDatabase(database);
            return printOk();
        }

        { return printError("Database does not exist"); }
    }

}
