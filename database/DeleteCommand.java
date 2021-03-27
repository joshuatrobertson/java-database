import java.util.HashMap;

public class DeleteCommand extends MainCommand {

    private Table tableToUpdate;

    public DeleteCommand(String[] incomingCommand, HashMap<String, Database> databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
        super.currentTable = tokenizedText[2];
    }

    public String run() {
        String[] tokens = createTokens(tokenizedText);
        if (!checkTableExists()) { return printError("Table does not exist"); }
        getTableToPrint();
        WhereCommand whereCommand = new WhereCommand(tokens, tableToUpdate, tokenizedText);
        whereCommand.run();
        if (whereCommand.checkAttributesExist()) {
            return printError("Attribute does not exist");
        }
        keysToReturn = whereCommand.getRowIds();
        return deleteFromTable();
    }

    // Assign the table to the Table tableToPrint and return true, return false if it does not exist
    private void getTableToPrint() {
        // The table will always be the first item
        String tableName = tokenizedText[2];
        tableToUpdate = databases.get(currentDatabase).getTable(tableName);
    }

    private String deleteFromTable() {
        // Loop through each entry that needs to be updated
        for (Integer key : keysToReturn) {
            // Update each column
            for (int i = 0; i < tableToUpdate.getEntries().size(); i++) {
                    if (tableToUpdate.getEntries().get(i).getKey() == key) {
                    tableToUpdate.deleteRecord(key);
                }
            }
        }
        // Write the table back to memory
        writeTableToMemory();
        return printOk();
    }
}
