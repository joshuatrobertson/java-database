package Parser;

// Takes in a String and parses to conform with the SQL grammar
public class Parser {

    private String[] splitText;

    public Parser() {
    }


    public Command parseText(String textToParse) {
        textToParse = textToParse.toLowerCase();
        // Remove empty spaces before parsing using '\\s'
        splitText = textToParse.split("\\s+");

        // Use command
        if (splitText[0].equals("use") && (splitText.length == 2)) {
            return Command.USE;
        }

        // Create command
        if (testFirstTwoCommands("create", "table") ||
                testFirstTwoCommands("create", "database") &&
        splitText.length == 3) {
            return Command.CREATE;
        }

        // Drop command
        if (splitText[0].equals("drop") && splitText.length == 2) {
            return Command.DROP;
        }

        // Alter command
        if (testFirstTwoCommands("alter", "table") &&
                (splitText[3].equals("add") || splitText[3].equals("drop") &&
                splitText.length == 5)) {
            return Command.ALTER;
        }

        // Insert command
        if (testFirstTwoCommands("insert", "into") &&
                splitText.length >= 5) {
            return Command.INSERT;
        }

        // Select command
        if (splitText[0].equals("select") && splitText.length >= 5) {
            return Command.SELECT;
        }

        // Update command
        if (splitText[0].equals("update")) {
            return Command.UPDATE;
        }

        // Delete command
        if (testFirstTwoCommands("delete", "from")) {
            return Command.DELETE;
        }

        // Join command
        if (splitText[0].equals("join") && splitText[2].equals("and") &&
                splitText[4].equals("on") && splitText[6].equals("and") &&
        splitText.length == 8) {
            return Command.JOIN;
        }

    return Command.NO_COMMAND;
    }

    private boolean testFirstTwoCommands(String firstCommand, String secondCommand) {
        if (splitText[0].equals(firstCommand) && splitText[1].equals(secondCommand)) {
            return true;
        }
        return false;
    }


}
