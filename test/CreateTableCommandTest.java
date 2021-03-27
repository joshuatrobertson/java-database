import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CreateTableCommandTest {

    String[] userCommand;
    String output;
    HashMap<String, Database> databases;
    JrSQLMain jrSQLMain = new JrSQLMain();
    FileIO file;


    @AfterEach
    public void tearDown() throws Exception {
        output = jrSQLMain.run("DROP database testDatabase;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        File direct = new File("files" + File.separator + "testDatabase");
        boolean doesItExist = direct.exists();
        Assertions.assertFalse(doesItExist);


    }

    @BeforeEach
    public void setUp() throws Exception {
        userCommand = new String[3];
        userCommand[0] = "CREATE";
        userCommand[1] = "table";
        databases = new HashMap<>();

        databases = jrSQLMain.getDatabases();
        file = new FileIO();
        output = jrSQLMain.run("CREATE database testDatabase;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        Assertions.assertEquals(databases.size(), 1);
        output = jrSQLMain.run("USE testDatabase;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        File direct = new File("files" + File.separator + "testDatabase");
        boolean doesItExist = direct.exists();
        Assertions.assertTrue(doesItExist);
    }

    @Test
    @DisplayName("Testing CREATE table command")
    public void CreateTableTest() {
        Database testDatabase = new Database("testDatabase");
        databases.put("testDatabase", testDatabase);
        userCommand[2] = "testTable(Condition1, Condition2, Condition3);";
        CreateTableCommand createTableCommand = new CreateTableCommand(userCommand, databases, "testDatabase");
        createTableCommand.run();
        // Only the first condition should exist as a folder
        File direct = new File("files" + File.separator + "testDatabase");
        File direct2 = new File("files" + File.separator + "doesNotExist");

        boolean doesItExist = direct.exists();
        // The second directory should not exist
        boolean doesItAlsoExist = direct2.exists();

        Assertions.assertTrue(doesItExist);
        Assertions.assertFalse(doesItAlsoExist);

    }

    @Test
    @DisplayName("Black box testing: Testing Table class")
    public void testTable() {
        // Normal command
        output = jrSQLMain.run("CREATE TABLE movies(title of movie, year, rating);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        Assertions.assertTrue(databases.get("testDatabase").checkTableExists("movies"));
        Assertions.assertFalse(databases.get("testDatabase").checkTableExists("shouldNotExist"));
        Assertions.assertTrue(databases.get("testDatabase").getTable("movies").checkAttributeExists("rating"));

        // Without capitilisation
        output = jrSQLMain.run("create table actors(name, age, sex);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        Assertions.assertTrue(databases.get("testDatabase").checkTableExists("actors"));
        Assertions.assertTrue(databases.get("testDatabase").getTable("actors").checkAttributeExists("age"));


        // Random capitilisation
        output = jrSQLMain.run("CrEaTe taBLe roles(name, movieid);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        Assertions.assertTrue(databases.get("testDatabase").checkTableExists("roles"));
        Assertions.assertTrue(databases.get("testDatabase").getTable("roles").checkAttributeExists("movieid"));

        // Spaces
        output = jrSQLMain.run("    CrEaTe taBLe     cinema (   name,    location )  ;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        Assertions.assertTrue(databases.get("testDatabase").checkTableExists("cinema"));
        Assertions.assertTrue(databases.get("testDatabase").getTable("cinema").checkAttributeExists("name"));

        // Insert
        output = jrSQLMain.run("INSERT INTO movies('Inception', 2010, 8.8);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        output = jrSQLMain.run("INSERT INTO movies('Interstellar', 2014, 8.6);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));


        /* *******ERROR TESTING******* */

        // Table already exists
        output = jrSQLMain.run("CREATE TABLE roles(name, movieid);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: " +
                "Table already exists"));

        // Missing TABLE
        output = jrSQLMain.run("CREATE name(name, movieid);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: " +
                "Invalid query"));

        // Missing braces
        output = jrSQLMain.run("CREATE TABLE newTable(name, movieid;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: " +
                "No enclosing brackets for CREATE TABLE statement"));

        output = jrSQLMain.run("CREATE TABLE newTable name, movieid);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: " +
                "No enclosing brackets for CREATE TABLE statement"));

        output = jrSQLMain.run("CREATE TABLE newTable name, movieid;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: " +
                "No enclosing brackets for CREATE TABLE statement"));

        output = jrSQLMain.run("INSERT INTO moviesInterstellar, 2014, 8.6);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: " +
                "No enclosing brackets for INSERT statement"));

        // Missing INTO
        output = jrSQLMain.run("INSERT movies(Interstellar, 2014, 8.6);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: " +
                "Invalid query"));
    }

}