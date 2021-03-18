import java.util.HashMap;

public class CreateDatabase extends MainCommand {

    public CreateDatabase(String[] incomingCommand, HashMap databases) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
    }

    // Creates a new database and folder within /files/
    public String run() {
        String databaseName = tokenizedText[2].replace(";", "");
        if (file.checkFolderExists(databaseName)) {
            return "[ERROR]: Database already exists";
        }
        else {
            Database newDatabase = new Database(databaseName);
            databases.put(databaseName, newDatabase);
            return "[OK]";
        }
    }
}
