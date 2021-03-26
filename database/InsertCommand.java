import java.util.Arrays;
import java.util.HashMap;
public class InsertCommand extends MainCommand {

    String entries;
    Table tableToInsert;
    int numberOfEntries;
    String errorMessage;

    public InsertCommand(HashMap<String, Database> databases, String[] tokenizedText, String currentDatabase) {
        super.tokenizedText = tokenizedText;
        super.currentDatabase = currentDatabase;
        super.databases = databases;
    }

    public String run() {
        if (checkErrors()) { return printError(errorMessage); }
        super.currentTable = getTableName();
        if (!checkTableExists()) { return printError("Table does not exist"); }
        getTableToInsert();
        getEntries();
        numberOfEntries = getNumberOfEntries();
        if (!checkNumberOfEntries()) { return printError("Incorrect number of entries"); }
        tableToInsert.insertNewEntry(entries);
        writeToFile();
        return printOk();
    }

    private boolean checkErrors() {
        String errors = Arrays.toString(tokenizedText);
        if (!errors.contains("(") || !errors.contains(")")) {
            errorMessage = "No enclosing brackets for INSERT statement";
            return true;
        }
        return false;
    }

    private String getTableName() {
        String tableName = Arrays.toString(tokenizedText);
        // Remove everything after and including the opening brace
        tableName = tableName.substring(tableName.toLowerCase().indexOf("into"), tableName.toLowerCase().indexOf("("));
        // Remove "table and '|,"
        tableName = tableName.replaceAll("[,']|\\b(?i)into\\b", "").trim();
        return tableName;
    }

    private void getEntries() {
        StringBuilder builder = new StringBuilder();
        for (String value : tokenizedText) {
            builder.append(value).append(" ");
        }
        entries = builder.toString();
        entries = entries.substring(entries.indexOf("("), entries.indexOf(")")).replace("(", "");
    }

    private void writeToFile() { file.writeFromTableInMemory(currentTable, currentDatabase, tableToInsert); }

    private void getTableToInsert() { tableToInsert = databases.get(currentDatabase).getTable(currentTable); }

    private boolean checkNumberOfEntries() { return numberOfEntries == databases.get(currentDatabase).getTable(currentTable).getNumberOfColumns(); }

    private int getNumberOfEntries() {
        String[] numberOfEntries = entries.split(",");
        return numberOfEntries.length;
    }

}
