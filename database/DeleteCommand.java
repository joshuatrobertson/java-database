import java.util.HashMap;

public class DeleteCommand extends MainCommand {

    public DeleteCommand(String[] incomingCommand, HashMap databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
        super.currentTable = tokenizedText[2];
    }

    public String run() {
        if (!checkTableExists()) { return printError("Table does not exist"); }

        return printOk();
    }
}
