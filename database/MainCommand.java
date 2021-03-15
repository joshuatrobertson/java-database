import java.util.HashMap;

public class MainCommand {

    protected HashMap databases;
    FileIO file = new FileIO();
    String[] tokenizedText;


    public MainCommand () {

    }

    public String printOk() { return "[OK]"; }

    public String printError(String message) { return "[ERROR]: " + message; }
}
