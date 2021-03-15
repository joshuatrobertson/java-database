import java.util.*;

public class DBRun {

    private Database currentDatabase;
    private HashMap<String, Database> databases = new HashMap<>();
    private Command command;
    private String[] tokenizedText;
    private String stringToPrint;
    FileIO file = new FileIO();

    public DBRun() {

    }

    public String run(String incomingCommand) {
        tokenizedText = tokenizeText(incomingCommand);
        Parser parser = new Parser(tokenizedText);
        command = parser.parseText();
        stringToPrint = returnRelevantCommand(command);
        return stringToPrint;
    }

    // Splits the text up an removes any whitespace
    private String[] tokenizeText(String originalText) {
        String lowerCase = originalText.toLowerCase().trim();
        return lowerCase.split("\\s+");
    }


    public void addDatabase(String databaseName, Database database) {
        databases.put(databaseName, database);
    }

    public String returnRelevantCommand(Command command) {
        switch (command) {
            case USE:
                UseCommand useCommand = new UseCommand(tokenizedText, databases, currentDatabase);
                return useCommand.run();
            case CREATE_TABLE:
                return createTableCommand();
            case CREATE_DATABASE:
                return createDatabaseCommand();
            case DROP:
                return dropCommand();
            case ALTER:
                return alterCommand();
            case INSERT:
                return insertCommand();
            case SELECT:
                return "[OK]" + "\n" + selectCommand();
            case UPDATE:
                return updateCommand();
            case DELETE:
                return deleteCommand();
            case JOIN:
                return joinCommand();
            case MISSING_SEMI_COLON:
                return "[ERROR]: Missing semi-colon";
            case NO_COMMAND:
                return "[ERROR]";
        }
        return "No command found!";
    }

    private String createTableCommand() {
       return "Create table";
    }


    // Creates a new database and folder within /files/
    private String createDatabaseCommand() {
        String databaseName = tokenizedText[2];
        if (file.checkFolderExists(databaseName)) {
            return "[ERROR]: Database already exists";
        }
        else {
            Database newDatabase = new Database(databaseName);
            databases.put(databaseName, newDatabase);
            return "[OK]";
        }
    }

    private String dropCommand() {
        return "[OK]";
    }

    private String alterCommand() {
        return "[OK]";
    }

    private String insertCommand() {
        return "[OK]";
    }

    private String selectCommand() {
        String stringToReturn = "";
        return stringToReturn;
    }

    private String updateCommand() {
        return "[OK]";
    }

    private String deleteCommand() {
        return "[OK]";
    }

    private String joinCommand() {
        return "[OK]";
    }
}
