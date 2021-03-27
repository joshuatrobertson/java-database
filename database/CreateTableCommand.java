import java.util.*;

public class CreateTableCommand extends MainCommand {

    private String tableName;
    private String[] columnList;
    private String errorMessage;

    public CreateTableCommand(String[] incomingCommand, HashMap<String, Database> databases, String currentDatabase) {
        super.tokenizedText = incomingCommand;
        super.databases = databases;
        super.currentDatabase = currentDatabase;
    }

    // Creates a new database and folder within /files/
    public String run() {
        if (!checkSelectedDatabase()) { return printError("No database selected") ; }
        if (checkErrors()) { return printError(errorMessage); }
        super.currentTable = getTableName();
        if (checkTableExists()) { return printError("Table already exists") ; }
        this.columnList = getColumns();
        if (!checkAttributesAlphaNum()) { return printError("Attributes must be alphanumerical"); }
        tableName = getTableName();
        createTable();
        return printOk();
    }

    private boolean checkErrors() {
        String errors = Arrays.toString(tokenizedText);
        if (!errors.contains("(") || !errors.contains(")")) {
            errorMessage = "No enclosing brackets for CREATE TABLE statement";
            return true;
        }
        return false;
    }

    private boolean checkAttributesAlphaNum() {
        for (String s : columnList) {
            if (s.trim().matches("^.*[^a-zA-Z0-9 ].*$")){
                return false;
            }
        }
        return true;
    }

    private String getTableName() {
        String tableName = Arrays.toString(tokenizedText);
        // Remove everything after and including the opening brace
        tableName = tableName.substring(tableName.toLowerCase().indexOf("table"), tableName.toLowerCase().indexOf("("));
        // Remove "table and '|,"
        tableName = tableName.replaceAll("[,']|\\b(?i)table\\b", "").trim();
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
        for (String attribute : columnList) {
            newTable.addColumn(attribute.trim());
        }
        databases.get(currentDatabase).addTable(newTable);
        file.createEmptyTable(tableName, currentDatabase, columnList);
    }

}
