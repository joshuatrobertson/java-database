import java.util.HashMap;

public class JoinCommand extends MainCommand {
    private String[] incomingCommand;
    private String attribute1, attribute2;
    private Table firstTable, secondTable;
    private String[] firstTableKeys;
    private String[] secondTableKeys;

    public JoinCommand(String[] incomingCommand, HashMap<String, Database> databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        this.incomingCommand = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
    }


    public String run() {
        if (!incomingCommand[2].equalsIgnoreCase("and")) { return printError("Missing or incorrect AND word"); }
        if(!getTables()) { return printError("Table does not exist"); }
        if(!getAttributes()) { return printError("Attribute does not exist"); }
        getKeys();
        return printTable();
    }

    private String printTable() {
        return databases.get(currentDatabase).printJoinCommand(firstTable, secondTable, firstTableKeys, secondTableKeys);
    }

    // Find the key for the first table and corresponding matching key
    private void getKeys() {
        // First loop through the first table column
        if (attribute1.equals("id")) {
            firstTableKeys = firstTable.getPrimaryKeys();
        }
        else {
            firstTableKeys = firstTable.getForeignKeys(attribute1);
        }

        // First loop through the first table column
        if (attribute2.equals("id")) {
            secondTableKeys = secondTable.getPrimaryKeys();
        }
        else {
            secondTableKeys = secondTable.getForeignKeys(attribute2);
        }
    }

    private boolean getTables() {
        String tableName = tokenizedText[1];
        String tableName2 = tokenizedText[3];
        // Assign the tables and check they exist
        return ((firstTable = databases.get(currentDatabase).getTable(tableName)) != null) &&
                (secondTable = databases.get(currentDatabase).getTable(tableName2)) != null;
    }

    private boolean getAttributes() {
        attribute1 = tokenizedText[5];
        attribute2 = tokenizedText[7].replace(";", "");
        // Assign the attributes and check they exist
        if(!attribute1.equals("id") && !firstTable.checkAttributeExists(attribute1)) {
            return false;
        }
        return attribute2.equals("id") || secondTable.checkAttributeExists(attribute2);
    }


}
