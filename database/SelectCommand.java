import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SelectCommand extends MainCommand {

    Table tableToPrint;
    Database currentDatabase;
    List<Integer> bracketCount;
    List<Integer> columnIds = new ArrayList<>();
    List<Integer> rowIds = new ArrayList<>();
    String[] tokens;
    boolean returnAllColumns = false;

    public SelectCommand(HashMap<String, Database> databases, String[] incomingCommand, Database currentDatabase) {
        super.tokenizedText = incomingCommand;
        this.currentDatabase = currentDatabase;
        super.databases = databases;
    }

    // Assign the table to the Table tableToPrint and return true, return false if it does not exist
    private boolean getTableToPrint() {
        // The table will always be the first item
        String tableName = tokens[0];
        tableName = tableName.split("from")[1].trim();
        if ((tableToPrint = currentDatabase.getTable(tableName)) != null){
            return true;
        }
        return false;
    }

    private boolean getColumnsToPrint() {
        String columns = tokens[0];
        columns = columns.split("from")[0];
        columns = columns.replace("select", "");
        // If the star operator is given then fetch all the column names
        if (columns.contains("*")) {
            returnAllColumns = true;
            return true;
        }
        // Else loop through all columns, add the position to an array (ColumnsToReturn) if they exist
        // else return false if they don't
        else {
            String[] columnArray = columns.trim().split(" ");
            for (String column : columnArray) {
                Integer id = null;
                id = tableToPrint.getTableId(column);

                if (id != null) {
                    columnIds.add(id);
                }
                else return false;
            }
        }
        return true;
    }



    public String run() {
        tokens = createTokens();
        // Get the table to print
        if (!getTableToPrint()) {
            return printError("Table does not exist");
        }
        // Get the list of columns
        if (!getColumnsToPrint()) {
            return printError("Attribute does not exist");
        }

        // No where statement
        if (tokens.length == 1) {
            // * used after select and therefore must print the full table
            if (returnAllColumns) {
                return printOk() + "\n" + tableToPrint.printFullTable();
            }
            // Select specific columns to print
            return printOk() + "\n" + tableToPrint.printPartialTableAllKeys(columnIds);
        }

        // Where statement
        WhereCommand whereCommand = new WhereCommand(tokens, tableToPrint);

        whereCommand.run();
        String stringToPrint;
        // * in SELECT statement
        if (returnAllColumns) {
            rowIds = whereCommand.getRowIds();
            stringToPrint = tableToPrint.printPartialTableAllColumns(rowIds);
        }
        else {
            rowIds = whereCommand.getRowIds();
            stringToPrint = tableToPrint.printPartialTable(rowIds, columnIds);
        }

        return printOk() + "\n" + stringToPrint;
    }

        private String[] createTokens () {
            // Splits the text up by brackets
            String startingString = Arrays.toString(tokenizedText);

            startingString = startingString.replace(",", "");
            startingString = startingString.replace("[", "");
            startingString = startingString.replace(";]", "");
            String[] finalArray = startingString.split("[\\(||\\)|]|where");
            return finalArray;
        }



}

