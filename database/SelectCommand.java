import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SelectCommand extends MainCommand {

    Table tableToPrint;
    Database currentDatabase;
    List<Integer> columnIds = new ArrayList<>();
    List<Integer> rowIds = new ArrayList<>();
    String[] tokens;
    boolean returnAllColumns = false;
    boolean returnOnlyKeys = false;


    public SelectCommand(HashMap<String, Database> databases, String[] incomingCommand, Database currentDatabase) {
        super.tokenizedText = incomingCommand;
        this.currentDatabase = currentDatabase;
        super.databases = databases;
    }

    // Assign the table to the Table tableToPrint and return true, return false if it does not exist
    private boolean getTableToPrint() {
        // The table will always be the first item
        String tableName = tokens[0];
        tableName = tableName.split("(?i)from")[1].trim();
        return (tableToPrint = currentDatabase.getTable(tableName)) != null;
    }

    private boolean getColumnsToPrint() {
        String columns = tokens[0];
        columns = columns.split("((?i)from)")[0].trim();
        columns = columns.replaceAll("((?i)select)", "").trim();
        // If the star operator is given then fetch all the column names
        if (columns.contains("*")) {
            returnAllColumns = true;
            return true;
        }
        // If id is given, then only print the keys
        if (columns.toLowerCase().contains("id")) {
            returnOnlyKeys = true;
            return true;
        }
        // Else loop through all columns, add the position to an array (ColumnsToReturn) if they exist
        // else return false if they don't
        else {
            String[] columnArray = columns.trim().split(" ");
            for (String attribute : columnArray) {
                if (!attribute.isBlank()) {
                    Integer id = tableToPrint.getTableId(attribute);
                    if (id != null) {
                        columnIds.add(id);
                    } else return false;
                }

            }
        }
        return true;
    }

    private boolean checkCommas() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : tokenizedText) {
            stringBuilder.append(s).append(" ");
        }
        String commaCheck = stringBuilder.toString();
        commaCheck = commaCheck.toLowerCase().split("from")[0];
        commaCheck = commaCheck.replace("select", "").trim();

        int commaCount = ( commaCheck.split(",", -1).length ) - 1;
        commaCheck = commaCheck.replace(",", " ").trim();
        int wordCount = ( commaCheck.split("\\s+", -1).length );

        return commaCount != wordCount - 1;
    }

    public String run() {
        if (currentDatabase == null) {
            return printError("No database selected");
        }
        if (!Arrays.toString(tokenizedText).toLowerCase().contains("from")) {
            return printError("Missing items or incorrect FROM statement");
        }
        tokens = createTokens(tokenizedText);

        if (checkCommas()) {
            return printError("Invalid query");
        }

        // Get the table to print
        if (!getTableToPrint()) {
            return printError("Invalid query");
        }
        // Get the list of columns
        if (!getColumnsToPrint()) {
            return printError("Attribute does not exist");
        }

        // No where statement
        if (tokens.length == 1) {
            String checkForRandomChars = Arrays.toString(tokenizedText);
            if (checkForRandomChars.contains("(") ||
                    checkForRandomChars.contains(")")) {
                return printError("Invalid query");
            }
            // * used after select and therefore must print the full table
            if (returnAllColumns) {
                return printOk() + "\n" + tableToPrint.printFullTable();
            }
            // Select specific columns to print
            return printOk() + "\n" + tableToPrint.printPartialTableAllKeys(columnIds);
        }

        // Where statement
        WhereCommand whereCommand = new WhereCommand(tokens, tableToPrint, tokenizedText);

        whereCommand.run();
        String stringToPrint;
        // has the where command thrown errors?
        if (whereCommand.checkErrorsFound()) return whereCommand.getErrorMessage();
        if (whereCommand.checkAttributesExist()) {
            return printError("Attribute does not exist");
        }
        // * in SELECT statement
        if (returnAllColumns) {
            rowIds = whereCommand.getRowIds();
            stringToPrint = tableToPrint.printPartialTableAllColumns(rowIds);
        } else if (returnOnlyKeys) {
            rowIds = whereCommand.getRowIds();

            return printOk() + "\n" + tableToPrint.printPartialTable(rowIds, columnIds);
        } else {
            rowIds = whereCommand.getRowIds();
            stringToPrint = tableToPrint.printPartialTable(rowIds, columnIds);
        }

        return printOk() + "\n" + stringToPrint;
    }

}

