import Parser.*;
import java.util.HashMap;
import java.util.Map;

public class DBRun {

    private String currentDatabase;
    private Map<String, Database> databases = new HashMap<>();
    private Command command;
    private String[] tokenizedText;
    private String stringToPrint;

    public DBRun() {

    }

    public String run(String incomingCommand) {
        String[] tokenizedText = tokenizeText(incomingCommand);
        CommandLogic commandLogic = new CommandLogic(tokenizedText);
        stringToPrint = commandLogic.returnRelevantCommand(tokenizedText);
        return stringToPrint;
    }

    private String[] tokenizeText(String originalText) {
        String lowerCase = originalText.toLowerCase();
        return lowerCase.split("\\s+");
    }
}
