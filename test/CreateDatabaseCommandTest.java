import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;

class CreateDatabaseCommandTest {

    String[] userCommand;
    HashMap<String, Database> databases;
    JrSQLMain jrSQLMain = new JrSQLMain();

    @AfterAll
    public static void tearDown() {
        File database = new File("files" + File.separator + "testDatabase");
        FileIO file = new FileIO();
        file.dropDatabase(database);
        boolean doesItExist = database.exists();
        Assertions.assertFalse(doesItExist);
    }

    @BeforeEach
    public void setUp() {
        userCommand = new String[3];
        userCommand[0] = "CREATE";
        userCommand[1] = "database";
        databases = new HashMap<>();
        jrSQLMain.setCurrentDatabase("testDatabase");
    }

    @Test
    @DisplayName("Testing CREATE database command")
    public void CreateDatabaseTest() {
        CreateDatabaseCommand createDatabaseCommand = new CreateDatabaseCommand(userCommand, databases);
        userCommand[2] = "testDatabase";
        createDatabaseCommand.run();
        // Only the first condition should exist as a folder
        File tmpDir = new File("files" + File.separator + "testDatabase");
        File tmpDir2 = new File("files" + File.separator + "doesNotExist");

        boolean doesItExist = tmpDir.exists();
        // The second directory should not exist
        boolean doesItAlsoExist = tmpDir2.exists();

        Assertions.assertTrue(doesItExist);
        Assertions.assertFalse(doesItAlsoExist);
    }


    @Test
    @DisplayName("Testing CREATE database: already exists")
    public void CreateDatabaseAlreadyExists() {
        CreateDatabaseCommand createDatabaseCommand = new CreateDatabaseCommand(userCommand, databases);
        userCommand[2] = "testDatabase";
        String result = createDatabaseCommand.run();
        Assertions.assertEquals("[OK]", result);
        String result2 = createDatabaseCommand.run();
        Assertions.assertEquals("[ERROR]: Database already exists", result2);

    }
}