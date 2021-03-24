import java.util.*;

public class CreateTableCommand extends MainCommand {

    String tableName;
    String[] columnList;

    public CreateTableCommand(String[] incomingCommand, HashMap<String, Database> databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
        super.currentTable = getTableName();
    }

    // Creates a new database and folder within /files/
    public String run() {
        if (!checkSelectedDatabase()) { return printError("No database selected") ; }
        if (checkTableExists()) { return printError("Table already exists") ; }
        this.columnList = getColumns();
        tableName = getTableName();
        createTable();
        return printOk();
    }

    private String getTableName() {
        String tableName = Arrays.toString(tokenizedText);
        // Remove everything after and including the opening brace
        tableName = tableName.substring(tableName.indexOf("table"), tableName.indexOf("("));
        // Remove "table and '|,"
        tableName = tableName.replaceAll("[,']|\\btable\\b", "").trim().toLowerCase();
        return tableName;
    }


    private boolean checkSelectedDatabase() {
        return (currentDatabase != null) && (!currentDatabase.isEmpty());
    }

    String[] getColumns() {
        StringBuilder builder = new StringBuilder();
        for (String value : tokenizedText) {
            builder.append(value).append(" ");
        }
        String columns = builder.toString();
        columns = columns.substring(columns.indexOf("("), columns.indexOf(")")).replace("(", "");
        return columns.split(",");
    }

    private void createTable() {
        Table newTable = new Table(tableName);
        for (String s : columnList) {
            newTable.addColumn(s);
        }
        databases.get(currentDatabase).addTable(newTable);
        file.createEmptyTable(tableName, currentDatabase, columnList);
    }

}
