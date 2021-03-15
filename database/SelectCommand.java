import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectCommand extends MainCommand {

    Table tableToPrint;
    Database currentDatabase;

    public SelectCommand(String[] incomingCommand, Database currentDatabase) {
        super.tokenizedText = incomingCommand;
        this.currentDatabase = currentDatabase;
    }


    private Table getTableToPrint() { return currentDatabase.getTable(tokenizedText[3]); }

    public String run() {
        tableToPrint = getTableToPrint();
        String stringToPrint = tableToPrint.printFullTable();
        return "[OK]\n" + stringToPrint;
    }





}
