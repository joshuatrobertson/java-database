import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParserTest {

    String[] userCommand;
    @BeforeEach
    public void setUp() throws Exception {
        userCommand = new String[3];
        userCommand[2] = ";";
    }


    @Test
    @DisplayName("Testing parser: Semi-colon missing")
    public void SemiColonMissing() {
        userCommand[0] = "USE";
        userCommand[1] = "newDatabase";
        userCommand[2] = "";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.MISSING_SEMI_COLON, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: USE Command")
    public void UseCommand() {
        userCommand[0] = "USE";
        userCommand[1] = "newDatabase";
        Parser parser = new Parser(userCommand);

        Assertions.assertEquals(Command.USE, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: CREATE_TABLE Command")
    public void CREATE_TABLECommand() {
        userCommand[0] = "CREATE";
        userCommand[1] = "TABLE";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.CREATE_TABLE, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: CREATE_DATABASE Command")
    public void CREATE_DATABASECommand() {
        userCommand[0] = "CREATE";
        userCommand[1] = "DATABASE";
        Parser parser = new Parser(userCommand);

        userCommand[0] = " create  ";
        userCommand[1] = "DataBAse";
        Parser parser2 = new Parser(userCommand);
        Assertions.assertEquals(Command.CREATE_DATABASE, parser.parseText());
        Assertions.assertEquals(Command.CREATE_DATABASE, parser2.parseText());

    }

    @Test
    @DisplayName("Testing parser: DROP Command")
    public void DROPCommand() {
        userCommand[0] = "DROP";
        userCommand[1] = "table";
        Parser parser = new Parser(userCommand);
        userCommand[0] = "DROP";
        userCommand[1] = "database";
        Parser parser2 = new Parser(userCommand);

        Assertions.assertEquals(Command.DROP, parser.parseText());
        Assertions.assertEquals(Command.DROP, parser2.parseText());

    }

    @Test
    @DisplayName("Testing parser: ALTER Command")
    public void ALTERCommand() {
        userCommand[0] = "ALTER";
        userCommand[1] = "table";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.ALTER, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: INSERT Command")
    public void INSERTCommand() {
        userCommand[0] = "INSERT";
        userCommand[1] = "INTO";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.INSERT, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: SELECT Command")
    public void SELECTCommand() {
        userCommand[0] = "SELECT";
        userCommand[1] = "newDatabase;";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.SELECT, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: UPDATE Command")
    public void UPDATECommand() {
        userCommand[0] = "UPDATE";
        userCommand[1] = "newDatabase;";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.UPDATE, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: DELETE Command")
    public void DELETECommand() {
        userCommand[0] = "DELETE";
        userCommand[1] = "FROM";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.DELETE, parser.parseText());
    }

    @Test
    @DisplayName("Testing parser: JOIN Command")
    public void JOINCommand() {
        userCommand[0] = "JOIN";
        userCommand[1] = "newDatabase;";

        Parser parser = new Parser(userCommand);
        Assertions.assertEquals(Command.JOIN, parser.parseText());
    }


}
