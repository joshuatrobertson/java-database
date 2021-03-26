import javax.xml.crypto.Data;
import java.util.*;

public class JrSQLMain {

    private String currentDatabase = null;
    private final HashMap<String, Database> databases = new HashMap<>();
    private String[] tokenizedText;

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
        return originalText.trim().split("\\s+");
    }

    public HashMap<String, Database> getDatabases() { return databases; }

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
                JoinCommand joinCommand = new JoinCommand(tokenizedText, databases, currentDatabase);
                return joinCommand.run();
            case MISSING_SEMI_COLON:
                return "[ERROR]: Semi colon missing at end of line";
            case NO_COMMAND:
                return "[ERROR]: Invalid query";
        }
        return "No command found!";
    }

    public void setCurrentDatabase(String database) { currentDatabase = database; }

    private Database getDatabaseByName(String databaseName) { return databases.get(databaseName); }
}
