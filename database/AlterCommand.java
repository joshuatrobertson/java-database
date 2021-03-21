import java.util.HashMap;

public class AlterCommand extends MainCommand {

    String alterationType;
    String attributeName;

    public AlterCommand(String[] incomingCommand, HashMap databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
        super.currentTable = tokenizedText[2];
        alterationType = tokenizedText[3];
        attributeName = tokenizedText[4];
    }

    public String run() {
        if (!checkTableExists()) { return printError("Table does not exist"); }
        if (!checkAlterationType()) { return printError("Incorrect ALTER command"); }

        if (alterationType.equals("add")) { return alterTableAdd(); }
        return alterTableDrop();

    }

    private boolean checkAlterationType() { return alterationType.equals("add") | alterationType.equals("drop"); }

    private boolean checkAttributeExists() { return databases.get(currentDatabase).getTable(currentTable).checkAttributeExists(attributeName); }

    private String alterTableDrop() {
        databases.get(currentDatabase).getTable(currentTable).deleteAttribute(attributeName);
        file.writeFromTableInMemory(currentTable, currentDatabase, databases.get(currentDatabase).getTable(currentTable));
        return printOk();
    }

    private String alterTableAdd() {
        if (checkAttributeExists()) { return printError("Attribute already exists"); }
        databases.get(currentDatabase).getTable(currentTable).addNewColumn(attributeName);
        writeTableToMemory();
        return printOk();
    }

}
