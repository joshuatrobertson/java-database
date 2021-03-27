import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

abstract class MainCommand {

    HashMap<String, Database> databases;
    FileIO file = new FileIO();
    String[] tokenizedText;
    String currentDatabase, currentTable;
    List<Integer> keysToReturn = new ArrayList<>();



    public MainCommand () {
    }

    public String printOk() { return "[OK]"; }

    public String printError(String message) { return "[ERROR]: " + message; }

    public boolean checkTableExists() {
        if (!databases.isEmpty()) {
            if (databases.get(currentDatabase.trim()) != null)
            return databases.get(currentDatabase.trim()).checkTableExists(currentTable.trim());
        }
        return false;
    }

    public String[] createTokens (String[] textToSplit) {
        // Splits the text up by brackets
        String startingString = Arrays.toString(textToSplit);

        startingString = startingString.replace(",", "");
        startingString = startingString.replace("[", "");
        startingString = startingString.replace(";]", "");
        return startingString.split("[(|)]|(?i)where");
    }

    public void writeTableToMemory() {
        file.writeFromTableInMemory(currentTable, currentDatabase, databases.get(currentDatabase).getTable(currentTable));
    }
}
