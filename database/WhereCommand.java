import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhereCommand extends MainCommand {
    List<String> operator = new ArrayList<>();
    List<String> logicalOperators = new ArrayList<>();
    List<String> item = new ArrayList<>();
    List<String> searchTerm = new ArrayList<>();
    List<Integer> bracketOrder = new ArrayList<>();
    String[] incomingCommand;
    Table tableToPrint;
    String[] tokenText;

    public WhereCommand(String[] incomingCommand, Table tableToPrint, String[] tokenizedText) {
        this.incomingCommand = incomingCommand;
        this.tableToPrint = tableToPrint;
        this.tokenText = tokenizedText;
    }

    public void run() {
        bracketOrder = findBracketOrder(Arrays.toString(tokenText));
        splitText();
        findOperators();
    }
    
    // Function to fetch the search term and item to search
    private void splitText() {
        // Loop through the array (the first will be the select command)
        for (int i = 1; i < incomingCommand.length; i++) {
            if (!incomingCommand[i].isBlank()) {
                // If the length is one, it is a logical operator i.e. AND, OR
                if (incomingCommand[i].trim().length() <= 3) {
                    logicalOperators.add(incomingCommand[i].trim());
                }
                // Else it is a statement i.e. age, >=, 5 and therefore add them to their respective arrays
                // First split according to operators and like so for the above command the result will be
                // age, 5. Then add to the respective arrays
                else {
                    item.add(incomingCommand[i].split("(!=)|(>=)|(<=)|(==)|[>=<]|(like)", 3)[0].trim());
                    searchTerm.add(incomingCommand[i].split("(!=)|(>=)|(<=)|(==)|[>=<]|(like)", 3)[1].trim());
                    }
                }
            }
        }


    // Uses a regex to find and split with operators. Due to this method any operators within Strings
    // will cause a crash
    private void findOperators() {
        String[] split = Arrays.toString(incomingCommand).split(",");
        split[0] = "";
        String joinedCommand = Arrays.toString(split);
        Matcher matcher = Pattern.compile("(!=)|(>=)|(<=)|(==)|[>=<]|(like)")
                .matcher(joinedCommand);
        while (matcher.find()) {
            operator.add(matcher.group());
        }
    }

    public boolean checkAttributesExist() {
        for (String i : item) {
            if(!tableToPrint.checkAttributeExists(i)) {
                return true;
            }
        }
        return false;
    }


    public List<Integer> getRowIds() {
        Search searchFunction = new Search(tableToPrint);

        // Only one condition
        if (item.size() == 1) {
            return getRowIdSingle(searchFunction);
        }
        return getRowIdMultiple(searchFunction);
    }

    // method used to find the row ids with boolean condition/s
    private List<Integer> getRowIdMultiple(Search searchFunction) {
        List<List<Integer>> multipleIds = new ArrayList<>();
        for (int i = 0; i < item.size(); i++) {
            Integer columnToSearch = tableToPrint.getColumnId(item.get(i));
            // If it is an integer/ float
            if (searchTerm.get(i).matches("-?(0|[1-9]\\d*)")) {
                multipleIds.add(searchFunction.searchWithOperator(operator.get(i), searchTerm.get(i), columnToSearch));
            }
            // If not then search as a String
            else {
                multipleIds.add(searchFunction.searchString(operator.get(i), searchTerm.get(i), columnToSearch));
            }
        }
        return getRowIdsWithBoolean(multipleIds);
    }

    // method to find the ids using a boolean condition.
    // How it works ::::::
    // First we get an array list of lists containing keys. We get the bracket order count from the below function,
    // so that if enclosed within 2 brackets it will have a count of 2. We start off by and'ing/or'ing the highest
    // brackets first, and then reducing the bracket count, and repeating until we have one item left, which is our
    // final key array. To account for multiple items with the same bracket count, we first get the highest count
    // and use boolean logic on the first two conditions of such we encounter.
    private List<Integer> getRowIdsWithBoolean(List<List<Integer>> multipleIds) {
        List<Integer> rowIdsPairToCheck = new ArrayList<>();
        List<Integer> secondRowIdsToCheck,firstRowIdsToCheck, rowIds;

        int highestBracketCount;

        // Loop until we reach the final bracket count
        while (multipleIds.size() > 1) {
            // get the highest bracket count
            highestBracketCount = findHighestBracketCount(this.bracketOrder);
            for (int i = 0; i < bracketOrder.size(); i++) {
                // if the integer is the same as the highest add until we reach a size of 2
                // to compare them using boolean logic
                if (bracketOrder.get(i) == highestBracketCount && rowIdsPairToCheck.size() < 2) {
                    rowIdsPairToCheck.add(i);
                }
            }
            int firstRow = rowIdsPairToCheck.get(0);
            int secondRow = rowIdsPairToCheck.get(1);
            firstRowIdsToCheck = multipleIds.get(firstRow);
            secondRowIdsToCheck = multipleIds.get(secondRow);
            multipleIds.remove(firstRow);
            // Index will be -1 due to the first item being removed
            multipleIds.remove(secondRow - 1);
            // Reduce the bracket count by one to remove one set of brackets
            if (bracketOrder.size() > 2) {
                bracketOrder.set(firstRow, bracketOrder.get(firstRow - 1));
                bracketOrder.remove(secondRow);
            }
            else {
                bracketOrder.remove(0);
            }
            rowIds = compareWithBoolean(firstRowIdsToCheck, secondRowIdsToCheck,  logicalOperators.get(rowIdsPairToCheck.get(0)));
            multipleIds.add(rowIds);
            rowIdsPairToCheck.clear();
        }
        // Return the only item left after boolean operators
        return multipleIds.get(0);
    }

    // Boolean AND and OR operations
    private List<Integer> compareWithBoolean(List<Integer> firstIds, List<Integer> secondIds, String booleanCommand) {
        List<Integer> results = new ArrayList<>();
        // AND boolean command
        if (booleanCommand.equals("and")) {
           results = booleanAND(firstIds, secondIds);
        }
        else if (booleanCommand.equals("or")) {
            results = booleanOR(firstIds, secondIds);
        }
        return results;
    }

    // OR method
    private List<Integer> booleanOR(List<Integer> firstIds, List<Integer> secondIds) {
        List<Integer> orResults = new ArrayList<>(firstIds);
        for (Integer secondId : secondIds) {
            if (!firstIds.contains(secondId)) {
                orResults.add(secondId);
            }
        }
        Collections.sort(orResults);
        return orResults;
    }

    // AND method
    private List<Integer> booleanAND(List<Integer> firstIds, List<Integer> secondIds) {
    List<Integer> andResults = new ArrayList<>();
        // Loop through the first list
        for (Integer firstId : firstIds) {
            // Loop through the second list until the number is = firstId (then add to the results) or > then continue
            for (Integer secondId : secondIds) {
                if (secondId > firstId) {
                    break;
                }
                if (secondId.equals(firstId)) {
                    andResults.add(firstId);
                    break;
                }
            }
        }
            return andResults;
    }

    private int findHighestBracketCount(List<Integer> bracketOrder) {
        int highest = 0;
        for (Integer integer : bracketOrder) {
            if (integer > highest) {
                highest = integer;
            }
        }
        return highest;
    }

    // Function used to find the row ids without boolean conditions
    private List<Integer> getRowIdSingle(Search searchFunction) {
        Integer columnToSearch = tableToPrint.getColumnId(item.get(0));
        // Is it an integer?
        if (searchTerm.get(0).matches("-?(0|[1-9]\\d*)")) {
            return searchFunction.searchWithOperator(operator.get(0), searchTerm.get(0), columnToSearch);
        }
        // String search
        else {
            return searchFunction.searchString(operator.get(0), searchTerm.get(0), columnToSearch);
        }
    }



    // Loops through the string to count number of brackets in order to access
    // the order of execution. For example, two items with a number of 2 will get executed together
    // before the result being executed with an item with a number of 1
    private List<Integer> findBracketOrder (String userQuery){
        int bracketCount = 0;
        List<Integer> executionOrder = new ArrayList<>();
        for (int i = 0; i < userQuery.length(); i++) {
            char c = userQuery.charAt(i);
            if (c == '(') {
                // Increase bracket count
                bracketCount++;
            } else if (userQuery.charAt(i) == ')') {
                executionOrder.add(bracketCount);
                // Closing bracket and therefore loop through the query until a non ')'
                // is reached
                while (userQuery.charAt(i) == ')') {
                    i++;
                    bracketCount--;
                }
            }
        }
        return executionOrder;
    }
}
