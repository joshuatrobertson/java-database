import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UpdateCommand extends MainCommand {

    String[] tokens;
    String[] incomingCommand;
    Table tableToUpdate;
    String[] setValues;


    public UpdateCommand(String[] incomingCommand, HashMap databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        this.incomingCommand = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
        super.currentTable = tokenizedText[1];
    }

    public String run() {
        tokens = createTokens();
        if (!getTableToPrint()) {
            return printError("Table does not exist");
        }

        getValues();
        return getKeysAndRun();
    }

    private void getValues() { setValues = tokens[1].split(" "); }

    private String[] createTokens () {
        // Splits the text up by brackets
        String startingString = Arrays.toString(tokenizedText);

        startingString = startingString.replace(",", "");
        startingString = startingString.replace("[", "");
        startingString = startingString.replace(";]", "");
        String[] finalArray = startingString.split("[\\(||\\)|]|where|set");
        return finalArray;
    }

    // Assign the table to the Table tableToPrint and return true, return false if it does not exist
    private boolean getTableToPrint() {
        // The table will always be the first item
        String tableName = tokens[0];
        tableName = tableName.split("update")[1].trim();
        if ((tableToUpdate = databases.get(currentDatabase).getTable(tableName)) != null){
            return true;
        }
        return false;
    }

    private String getKeysAndRun() {
        WhereCommand whereCommand = new WhereCommand(tokens, tableToUpdate);
        List<Integer> keysToReturn = new ArrayList<>();
        if (!whereCommand.checkAttributesExist()) {
            return printError("Attributes do not exist");
        }
        keysToReturn = whereCommand.getRowIds();

        whereCommand.run();

        return printOk();
    }
}
