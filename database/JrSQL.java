import java.util.*;

public class JrSQL {

    private String currentDatabase;
    private final HashMap<String, Database> databases = new HashMap<>();
    private String[] tokenizedText;
    FileIO file = new FileIO();

    public JrSQL() {

    }

    public String run(String incomingCommand) {
        tokenizedText = tokenizeText(incomingCommand);
        Parser parser = new Parser(tokenizedText);
        Command command = parser.parseText();
        return returnRelevantCommand(command);
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
                UseCommand useCommand = new UseCommand(tokenizedText, databases);
                setCurrentDatabase(useCommand.getCurrentDatabase());
                return useCommand.run();
            case CREATE_TABLE:
                CreateDatabase createTable = new CreateDatabase(tokenizedText, databases);
                return createTable.run();
            case CREATE_DATABASE:
                CreateDatabase createDatabase = new CreateDatabase(tokenizedText, databases);
                return createDatabase.run();
            case DROP:
                return dropCommand();
            case ALTER:
                return alterCommand();
            case INSERT:
                return insertCommand();
            case SELECT:
                SelectCommand selectCommand = new SelectCommand(tokenizedText, getDatabaseByName(currentDatabase));
                return selectCommand.run();
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



    private String dropCommand() {
        return "[OK]";
    }

    private String alterCommand() {
        return "[OK]";
    }

    private String insertCommand() {
        return "[OK]";
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

    public void setCurrentDatabase(String database) { currentDatabase = database; }

    private Database getDatabaseByName(String databaseName) { return databases.get(databaseName); }
}
