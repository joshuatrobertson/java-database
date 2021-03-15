import java.util.*;

public class Search {
    int columnToSearch;
    String itemToSearch;
    Table tableToSearch, newTable;
    List<Integer> searchResults = new ArrayList<Integer>();
    List<Entry> oldEntries;


    public Search(int columnToSearch, String itemToSearch, Table tableToSearch) {
        this.columnToSearch = columnToSearch;
        this.itemToSearch = itemToSearch;
        this.tableToSearch = tableToSearch;
        oldEntries = tableToSearch.getEntries();
    }

    List<Integer> searchSelectColumns(List<Integer> columnsToReturn, List<Integer> keysToReturn, boolean returnAll) {
        Object[] columns = columnsToReturn.toArray();
        List<Integer> columnList = new ArrayList<Integer>();
        // First we reverse the array so we know which items to remove, rather than to keep
        Collections.reverse(Arrays.asList(columns));
        for (Object i : columns) {
            columnList.add((Integer) columns[(int) i]);
        }
        Collections.reverse(Arrays.asList(columnsToReturn));

        return columnList;
    }



    // Searches for instances of the String itemToSearch and returns the
    // relevant keys
    List<Integer> searchLikeCommand() {
        // Loop through each record
        for (int i = 0; i < oldEntries.size(); i++) {
            //Search the column of the specific record
            List<String> items = oldEntries.get(i).getElements();
            // Search for instances of the itemToSearch String
            if (items.get(columnToSearch).contains(itemToSearch)) {
                searchResults.add(oldEntries.get(i).getKey());
            }
        }
        return searchResults;
    }

    // Searches the table for instances relevant to the operator and number given
    // in itemToSearch
    List<Integer> searchWithOperator(String operator) {
        float numberFromRecords, numberToSearch = Float.parseFloat(itemToSearch);

        // Loop through the records from the table
        for (int i = 0; i < oldEntries.size(); i++) {
            //Search the column of the specific record
            List<String> items = oldEntries.get(i).getElements();
            // Fetch the number within the record without spaces
            numberFromRecords = Float.parseFloat(items.get(columnToSearch).trim());

            // Use the operatorCompare function to check the records
            if (operatorCompare(numberFromRecords, numberToSearch, operator)) {
                searchResults.add(oldEntries.get(i).getKey());
            }
        }
        return searchResults;
    }

    // Takes in two numbers and a String comparative operator and performs the operation
    private static boolean operatorCompare(double firstNumber, double secondNumber, String operator)
    {
        if ("==".equals(operator)) {
            return Double.compare(firstNumber, secondNumber) == 0;
        } else if (">".equals(operator)) {
            return Double.compare(firstNumber, secondNumber) == 1;
        } else if ("<=".equals(operator)) {
            return Double.compare(firstNumber, secondNumber) <= 0;
        } else if (">=".equals(operator)) {
            return Double.compare(firstNumber, secondNumber) >= 0;
        } else if ("<".equals(operator)) {
            return Double.compare(firstNumber, secondNumber) == -1;
        } else if ("!=".equals(operator)) {
            return Double.compare(firstNumber, secondNumber) != 0;
        } else {
            throw new IllegalArgumentException("Invalid operator given");
        }
    }



}
