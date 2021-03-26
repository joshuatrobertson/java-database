import java.util.Arrays;

// Takes in a String and parses to conform with the SQL grammar
public class Parser {

    Command command;
    String[] userCommand;

    public Parser(String[] userCommand) {
        this.userCommand = userCommand;
    }


    public Command parseText() {

        if (errorTest() != Command.NO_ERRORS) {
            return errorTest();
        }

        // Use command
        if (userCommand[0].equalsIgnoreCase("use") && (userCommand.length <= 3)) {
            return Command.USE;
        }

        // Create table command
        if (testFirstTwoCommands("create", "table")) {
            return Command.CREATE_TABLE;
        }

        // Create database command
        if (testFirstTwoCommands("create", "database")) {
            return Command.CREATE_DATABASE;
        }

        // Drop command
        if (userCommand[0].equalsIgnoreCase("drop") && userCommand[1].equalsIgnoreCase("table") |
                userCommand[1].equalsIgnoreCase("database") && userCommand.length <= 4) {
            return Command.DROP;
        }

        // Alter command
        if (testFirstTwoCommands("alter", "table") && userCommand.length <= 6) {
            return Command.ALTER;
        }

        // Insert command
        if (testFirstTwoCommands("insert", "into")) {
            return Command.INSERT;
        }

        // Select command
        if (userCommand[0].equalsIgnoreCase("select")) {
            return Command.SELECT;
        }

        // Update command
        if (userCommand[0].equalsIgnoreCase("update")) {
            return Command.UPDATE;
        }

        // Delete command
        if (testFirstTwoCommands("delete", "from")) {
            return Command.DELETE;
        }

        // Join command
        if (userCommand[0].equalsIgnoreCase("join")) {
            return Command.JOIN;
        }

        // Look for errors in the query
        return Command.NO_COMMAND;
    }


    private boolean testFirstTwoCommands(String firstCommand, String secondCommand) {
        return userCommand[0].trim().equalsIgnoreCase(firstCommand) && userCommand[1].trim().equalsIgnoreCase(secondCommand);
    }

    private Command errorTest() {
        // Check for a missing semi-colon
         String stringQuery = Arrays.toString(userCommand);

        // As it is from a String array it will be concatenated with a ']'
        if (!stringQuery.endsWith(";]") && stringQuery.length() > 3) {
            return Command.MISSING_SEMI_COLON;
        }
        return Command.NO_ERRORS;
    }


}
