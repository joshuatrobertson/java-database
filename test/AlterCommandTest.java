

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;

class AlterCommandTest {

    HashMap<String, Database> databases;
    String currentDatabase;
    Database testDatabase;
    Table testTable;

    @BeforeEach
    public void setUp() throws Exception {
        testDatabase = new Database("testDatabase");
        testTable = new Table("testtable");
        databases = new HashMap<>();
        currentDatabase = "testDatabase";
        testTable.addColumn("testAttribute1");
        testTable.addColumn("testAttribute2");
        testTable.addColumn("testAttribute3");
        testDatabase.addTable(testTable);
        databases.put("testDatabase", testDatabase);
    }

    @AfterEach
    public void tearDown() throws Exception {
        testDatabase.removeTable("testtable");
        databases.remove("testDatabase");
        File database = new File("files" + File.separator + "testDatabase");
        FileIO file = new FileIO();
        file.dropDatabase(database);
        boolean doesItExist = database.exists();
        Assertions.assertFalse(doesItExist);
    }

    @Test
    @DisplayName("Testing ALTER command: correct input")
    public void AlterCommandCorrectParameters() {
        // Normal Command
        String[] tokens = {"ALTER", "TABLE", "testtable", "ADD", "testAttribute4;"};
        AlterCommand alterCommand = new AlterCommand(tokens, databases, currentDatabase);
        String output = alterCommand.run();
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));

        // Spaces in between text

        String[] tokens2 = {"ALTER", "    TABLE   ", "testtable      ", "ADD", "testAttribute5      ;"};
        alterCommand = new AlterCommand(tokens2, databases, currentDatabase);
        output = alterCommand.run();
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));



        // Capitalised text

        String[] tokens3 = {"AlTER", "    tAble   ", "testtable      ", "add", "testAttribute6      ;"};
        alterCommand = new AlterCommand(tokens3, databases, currentDatabase);
        output = alterCommand.run();
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));

    }

    @Test
    @DisplayName("Testing ALTER command: table missing")
    public void AlterCommandTableMissing() {
        String[] tokens = {"ALTER", "TABLE", "tableDoesNotExist", "ADD", "age;"};
        AlterCommand alterCommand = new AlterCommand(tokens, databases, currentDatabase);
        String output = alterCommand.run();
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Table does not exist"));
    }

    @Test
    @DisplayName("Testing ALTER command: incorrect ALTER command")
    public void AlterCommandIncorrectCommand() {
        String[] tokens = {"ALTER", "TABLE", "testtable", "willFail", "age;"};
        AlterCommand alterCommand = new AlterCommand(tokens, databases, currentDatabase);
        String output = alterCommand.run();
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Incorrect ALTER command"));
    }

    @Test
    @DisplayName("Testing ALTER command: incorrect ALTER command")
    public void AlterCommandRandomCharacter() {
        String[] tokens = {"ALTER", "TABLE", "testtable", "willFail", "age)(;"};
        AlterCommand alterCommand = new AlterCommand(tokens, databases, currentDatabase);
        String output = alterCommand.run();
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Incorrect ALTER command"));
    }


}