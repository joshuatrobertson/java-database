import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateCommand extends MainCommand {

    private String[] tokens;
    private Table tableToUpdate;
    private List<Integer> updateColumns;
    private List<String> updateItems;


    public UpdateCommand(String[] incomingCommand, HashMap<String, Database> databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
        super.currentTable = tokenizedText[1];
    }

    public String run() {
        tokens = createTokens(tokenizedText);
        if (!getTableToPrint()) {
            return printError("Table does not exist");
        }

        getValues();
        return getKeysAndRun();
    }

    private void getValues() {
        String[] whereValues = tokens[1].split(" ");
    }


    // Assign the table to the Table tableToPrint and return true, return false if it does not exist
    private boolean getTableToPrint() {
        return (tableToUpdate = databases.get(currentDatabase).getTable(currentTable)) != null;
    }

    private String getKeysAndRun() {
        WhereCommand whereCommand = new WhereCommand(tokens, tableToUpdate, tokenizedText);
        if (whereCommand.checkAttributesExist()) {
            return printError("Attributes do not exist");
        }
        whereCommand.run();
        keysToReturn = new ArrayList<>();
        keysToReturn = whereCommand.getRowIds();
        getUpdateItems();
        return updateTable();
    }

    private void getUpdateItems() {
        updateItems = new ArrayList<>();
        updateColumns = new ArrayList<>();
        String[] newTokens = tokens[0].toLowerCase().split("set");
        String tokenString = newTokens[1];
        String[] tokenStringSplit = tokenString.split("[= ]");
        int count = 0;
        for (String s : tokenStringSplit) {
            // The items will be in the order item to update, update value and therefore those with an even number position
            // will always be the item and those with odd will be the value
            if (!s.isBlank()) {
                if (count % 2 == 0) {
                    // If even number find the column position
                    int columnNumber = tableToUpdate.getAttributePosition(s);
                    updateColumns.add(columnNumber);
                }
                else {
                    updateItems.add(s);
                }
                count++;
            }
        }

    }
    private String updateTable() {
        // Loop through each entry that needs to be updated
        for (Integer key : keysToReturn) {
            // Update each column
            for (int i = 0; i < tableToUpdate.getEntries().size(); i++) {
                if (tableToUpdate.getEntries().get(i).getKey() == key) {
                    // Update single value in table
                    if (updateColumns.size() == 1) {
                        tableToUpdate.getEntries().get(i).updateElement(updateColumns.get(0), updateItems.get(0));
                    }
                    else {
                        // update > 1 values, therefore cycle through ArrayList updateColumns && updateItems
                        for (Integer column : updateColumns) {
                            tableToUpdate.getEntries().get(i).updateElement(updateColumns.get(column), updateItems.get(column));
                        }
                    }
                }
            }
        }
        // Write the table back to memory
        writeTableToMemory();
        return printOk();
    }
}
