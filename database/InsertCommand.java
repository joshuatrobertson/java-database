import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InsertCommand extends MainCommand {

    String entries;
    Table tableToInsert;
    int numberOfEntries;

    public InsertCommand(HashMap databases, String[] tokenizedText, String currentDatabase) {
        super.tokenizedText = tokenizedText;
        super.currentTable = tokenizedText[2];
        super.currentDatabase = currentDatabase;
        super.databases = databases;
    }

    public String run() {
        if (!checkTableExists()) { return printError("Table does not exist"); }
        getTableToInsert();
        getEntries();
        numberOfEntries = getNumberOfEntries();
        if (!checkNumberOfEntries()) { return printError("Incorrect number of entries"); }
        tableToInsert.insertNewEntry(entries);
        writeToFile();
        return printOk();
    }

    private void getEntries() {
        StringBuilder builder = new StringBuilder();
        for (String value : tokenizedText) {
            builder.append(value + " ");
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
