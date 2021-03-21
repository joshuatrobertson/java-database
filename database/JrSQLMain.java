import java.util.*;

public class JrSQLMain {

    private String currentDatabase = null;
    private final HashMap<String, Database> databases = new HashMap<>();
    private String[] tokenizedText;
    FileIO file = new FileIO();

    public JrSQLMain() {

    }

    public String run(String incomingCommand) {
        tokenizedText = tokenizeText(incomingCommand);
        Parser parser = new Parser(tokenizedText);
        Command command = parser.parseText();
        return returnRelevantCommand(command);
    }

    // Splits the text up and removes any whitespace
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
                CreateTableCommand createTable = new CreateTableCommand(tokenizedText, databases, currentDatabase);
                return createTable.run();
            case CREATE_DATABASE:
                CreateDatabaseCommand createDatabase = new CreateDatabaseCommand(tokenizedText, databases);
                return createDatabase.run();
            case DROP:
                DropCommand dropCommand = new DropCommand(tokenizedText, databases, currentDatabase);
                return dropCommand.run();
            case ALTER:
                AlterCommand alterCommand = new AlterCommand(tokenizedText, databases, currentDatabase);
                return alterCommand.run();
            case INSERT:
                InsertCommand insertCommand = new InsertCommand(databases, tokenizedText, currentDatabase);
                return insertCommand.run();
            case SELECT:
                SelectCommand selectCommand = new SelectCommand(databases, tokenizedText, getDatabaseByName(currentDatabase));
                return selectCommand.run();
            case UPDATE:
                UpdateCommand updateCommand = new UpdateCommand(tokenizedText, databases, currentDatabase);
                return updateCommand.run();
            case DELETE:
                DeleteCommand deleteCommand = new DeleteCommand(tokenizedText, databases, currentDatabase);
                return deleteCommand.run();
            case JOIN:
                return joinCommand();
            case MISSING_SEMI_COLON:
                return "[ERROR]: Missing semi-colon";
            case NO_COMMAND:
                return "[ERROR]";
        }
        return "No command found!";
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
