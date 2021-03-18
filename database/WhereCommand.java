import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhereCommand extends MainCommand {
    List<String> operator = new ArrayList<String>();
    List<String> logicalOperators = new ArrayList<String>();
    List<String> item = new ArrayList<String>();
    List<String> searchTerm = new ArrayList<String>();
    List<Integer> bracketOrder = new ArrayList<>();
    String[] incomingCommand;
    Table tableToPrint;

    public WhereCommand(String[] incomingCommand, List<Integer> bracketOrder, Table tableToPrint) {
        this.incomingCommand = incomingCommand;
        this.bracketOrder = bracketOrder;
        this.tableToPrint = tableToPrint;
    }

    public void run() {
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

    private void findOperators() {
        String joinedCommand = Arrays.toString(incomingCommand);
        Matcher matcher = Pattern.compile("(!=)|(>=)|(<=)|(==)|[>=<]|(like)")
                .matcher(joinedCommand);
        while (matcher.find()) {
            operator.add(matcher.group());
        }
    }




    public List<Integer> getRowIds() {
        Search searchFunction = new Search(tableToPrint);
        List<Integer> columnIds = new ArrayList<>();

        // Only one condition
        if (item.size() == 1) {
            Integer columnToSearch = tableToPrint.getColumnId(item.get(0));
            // Is it an integer?
            if (searchTerm.get(0).matches("-?(0|[1-9]\\d*)")) {
                columnIds = searchFunction.searchWithOperator(operator.get(0), searchTerm.get(0), columnToSearch);
            }
            // String search
            else {
                columnIds = searchFunction.searchString(operator.get(0), searchTerm.get(0), columnToSearch);
            }
        }
        return columnIds;
    }
}
