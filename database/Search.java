import java.util.*;

public class Search {
    Table tableToSearch;
    final List<Integer> searchResults = new ArrayList<>();
    List<Entry> oldEntries;


    public Search(Table tableToSearch) {
        this.tableToSearch = tableToSearch;
        oldEntries = tableToSearch.getEntries();
    }

    List<Integer> searchString(String operator, String searchTerm, Integer columnToSearch) {
        return switch (operator) {
            case "like" -> searchLikeEqualsCommand(searchTerm, columnToSearch, "like");
            case "==" -> searchLikeEqualsCommand(searchTerm, columnToSearch, "==");
            case "!=" -> searchLikeEqualsCommand(searchTerm, columnToSearch, "!=");
            default -> throw new InputMismatchException("Invalid operator");
            };
        }


    // Searches for instances of the String itemToSearch and returns the
    // relevant keys
    List<Integer> searchLikeEqualsCommand(String itemToSearch, int columnToSearch, String likeOrEquals) {
        ArrayList<Integer> searchResultsCopy = new ArrayList<>(searchResults);
        // Loop through each record
        for (Entry oldEntry : oldEntries) {
            //Search the column of the specific record
            List<String> items = oldEntry.getElements();
            // Search for instances of the itemToSearch String
            switch (likeOrEquals) {
                case "like":
                    // get rid of string ( ' )
                    itemToSearch = itemToSearch.replace("'", "");
                    // If string '.contains() the item return it
                    if (items.get(columnToSearch).toLowerCase().contains(itemToSearch.replace("'", ""))) {
                        searchResultsCopy.add(oldEntry.getKey());
                    }
                    break;
                // Add if the term matches
                case "==":
                    if (items.get(columnToSearch).replace("'", "").toLowerCase().trim().equals(itemToSearch.replace("'", ""))) {
                        searchResultsCopy.add(oldEntry.getKey());
                    }
                    break;
                // Add if the term doesn't matches
                case "!=":
                    if (!items.get(columnToSearch).replace("'", "").toLowerCase().trim().equals(itemToSearch.replace("'", ""))) {
                        searchResultsCopy.add(oldEntry.getKey());
                    }
                    break;
            }
        }
        return searchResultsCopy;
    }

    // Searches the table for instances relevant to the operator and number given
    // in itemToSearch
    List<Integer> searchWithOperator(String operator, String itemToSearch, int columnToSearch) {
        List<Integer> searchResultsCopy = new ArrayList<>(searchResults);

        float numberFromRecords, numberToSearch = Float.parseFloat(itemToSearch);
        List<String> items;
        // Loop through the records from the table
        for (Entry oldEntry : oldEntries) {
            //Search the column of the specific record
            items = oldEntry.getElements();
            // Fetch the number within the record without spaces
            numberFromRecords = Float.parseFloat(items.get(columnToSearch).trim());

            // Use the operatorCompare function to check the records
            if (operatorCompare(numberFromRecords, numberToSearch, operator)) {
                searchResultsCopy.add(oldEntry.getKey());
            }
        }
        return searchResultsCopy;
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
