// Takes in a Command from the parser and carries out the required logic, before being passing back the relevant String
// to DBRun

package Parser;

public class CommandLogic {
    private String[] userCommand;
    private Parser parser;


    public CommandLogic(String[] userCommand) {
        this.userCommand = userCommand;
        parser = new Parser(userCommand);
    }

    public String returnRelevantCommand(String[] tokenizedText) {
        Command command = parser.parseText();
        String textToPrint;
        switch (command) {
            case USE:
                useCommand();
                return "[OK]";
            case CREATE:
                createCommand();
                return "CREATE Statement!";
            case DROP:
                dropCommand();
                return "DROP Statement!";
            case ALTER:
                alterCommand();
                return "ALTER Statement!";
            case INSERT:
                insertCommand();
                return "INSERT Statement!";
            case SELECT:
                return "[OK]" + "\n" + selectCommand();
            case UPDATE:
                updateCommand();
                return "UPDATE Statement!";
            case DELETE:
                deleteCommand();
                return "DELETE Statement!";
            case JOIN:
                joinCommand();
                return "JOIN Statement!";
            case NO_COMMAND:
                return "[ERROR]";
        }
        return "No command found!";
    }

    private void useCommand() {
        //Do stuff
    }

    private void createCommand() {
        //Do stuff
    }

    private void dropCommand() {
        //Do stuff
    }

    private void alterCommand() {
        //Do stuff
    }

    private void insertCommand() {
        //Do stuff
    }

    private String selectCommand() {
        String stringToReturn = "";
        return stringToReturn;
    }

    private void updateCommand() {
        //Do stuff
    }

    private void deleteCommand() {
        //Do stuff
    }

    private void joinCommand() {
        //Do stuff
    }



}
