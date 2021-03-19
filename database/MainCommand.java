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
}
