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
        if (userCommand[0].equals("use") && (userCommand.length == 2)) {
            return Command.USE;
        }

        // Create table command
        if (testFirstTwoCommands("create", "table")) {
            return Command.CREATE_TABLE;
        }

        // Create database command
        if (testFirstTwoCommands("create", "database") &&
                        userCommand.length == 3) {
            return Command.CREATE_DATABASE;
        }

        // Drop command
        if (userCommand[0].equals("drop") && userCommand.length == 2) {
            return Command.DROP;
        }

        // Alter command
        if (testFirstTwoCommands("alter", "table") &&
                (userCommand[3].equals("add") || userCommand[3].equals("drop") &&
                        userCommand.length == 5)) {
            return Command.ALTER;
        }

        // Insert command
        if (testFirstTwoCommands("insert", "into") &&
                userCommand.length >= 5) {
            return Command.INSERT;
        }

        // Select command
        if (userCommand[0].equals("select") && userCommand.length >= 5) {
            return Command.SELECT;
        }

        // Update command
        if (userCommand[0].equals("update")) {
            return Command.UPDATE;
        }

        // Delete command
        if (testFirstTwoCommands("delete", "from")) {
            return Command.DELETE;
        }

        // Join command
        if (userCommand[0].equals("join") && userCommand[2].equals("and") &&
                userCommand[4].equals("on") && userCommand[6].equals("and") &&
                userCommand.length == 8) {
            return Command.JOIN;
        }

        // Look for errors in the query
        return Command.NO_COMMAND;
    }


    private boolean testFirstTwoCommands(String firstCommand, String secondCommand) {
        if (userCommand[0].equals(firstCommand) && userCommand[1].equals(secondCommand)) {
            return true;
        }
        return false;
    }

    private Command errorTest() {
        // Check for a missing semi-colon
        String stringQuery = Arrays.toString(userCommand);

        // As it is from a String array it will be concatenated with a ']'
        if (!stringQuery.endsWith(";]")) {
            return Command.MISSING_SEMI_COLON;
        }
        return Command.NO_ERRORS;
    }


}
