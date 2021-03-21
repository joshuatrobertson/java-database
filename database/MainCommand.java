import java.util.Arrays;
import java.util.HashMap;

abstract class MainCommand {

    HashMap<String, Database> databases;
    FileIO file = new FileIO();
    String[] tokenizedText;
    String currentDatabase, currentTable;


    public MainCommand () {

    }

    public String printOk() { return "[OK]"; }

    public String printError(String message) { return "[ERROR]: " + message; }

    public boolean checkTableExists() { return databases.get(currentDatabase).checkTableExists(currentTable); }

    public String[] createTokens (String[] textToSplit) {
        // Splits the text up by brackets
        String startingString = Arrays.toString(textToSplit);

        startingString = startingString.replace(",", "");
        startingString = startingString.replace("[", "");
        startingString = startingString.replace(";]", "");
        String[] finalArray = startingString.split("[\\(||\\)|]|where");
        return finalArray;
    }

    public void writeTableToMemory() {
        file.writeFromTableInMemory(currentTable, currentDatabase, databases.get(currentDatabase).getTable(currentTable));
    }


}
