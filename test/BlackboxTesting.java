import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;

public class BlackboxTesting {

    JrSQLMain jrSQLMain = new JrSQLMain();
    String output;
    FileIO file;


    // Create a new database each time
    @BeforeEach
    public void setUp() throws Exception {
        HashMap<String, Database> databases = jrSQLMain.getDatabases();
        file = new FileIO();
        output = jrSQLMain.run("CREATE database testDatabase;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        Assertions.assertEquals(databases.size(), 1);
        output = jrSQLMain.run("USE testDatabase;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        File direct = new File("files" + File.separator + "testDatabase");
        boolean doesItExist = direct.exists();
        Assertions.assertTrue(doesItExist);

        // Create tables to test
        jrSQLMain.run("CREATE TABLE movies(title, year, rating);");
        jrSQLMain.run("INSERT INTO movies(Inception, 2010, 8.8);");
        jrSQLMain.run("INSERT INTO movies(Interstellar, 2014, 8.6);");
        jrSQLMain.run("INSERT INTO movies('The Bourne Identity', 2014, 7.1);");
        jrSQLMain.run("INSERT INTO movies('Jaws', 1975, 7.3);");
        jrSQLMain.run("INSERT INTO movies('Lawrence of Arabia', 1934, 8.1);");

        jrSQLMain.run("CREATE TABLE actors(name, age, sex);");
        jrSQLMain.run("INSERT INTO actors('Will Smith', 54, 'male');");
        jrSQLMain.run("INSERT INTO actors('Tom Hanks', 45, 'male');");
        jrSQLMain.run("INSERT INTO actors('Tom Cruise', 63, 'male');");
        jrSQLMain.run("INSERT INTO actors('Julia Roberts', 45, 'female');");

        jrSQLMain.run("CREATE TABLE roles(name, movieid, actorid);");
        jrSQLMain.run("INSERT INTO roles('Cobb', 1, 1);");
        jrSQLMain.run("INSERT INTO roles('Cooper', 2, 1);");
        jrSQLMain.run("INSERT INTO roles('Jason Bourne', 3, 2);");
        jrSQLMain.run("INSERT INTO roles('Random', 7, 3);");
        jrSQLMain.run("INSERT INTO roles('Random2', 8, 7);");

    }

    // Delete each time to clean up
    @AfterEach
    public void tearDown() throws Exception {
        output = jrSQLMain.run("DROP database testDatabase;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        File direct = new File("files" + File.separator + "testDatabase");
        boolean doesItExist = direct.exists();
        Assertions.assertFalse(doesItExist);
    }

    @Test
    @DisplayName("Black box testing: testing JOIN command")
    public void testInnerJoin() {
        // Inner join

        output = jrSQLMain.run("JOIN movies AND roles on id AND movieid;");
        Assertions.assertTrue(output.contains("""
                id\tmovies.title\tmovies.year\tmovies.rating\troles.name\troles.movieid\troles.actorid\t
                1\tInception\t2010\t8.8\tCobb\t1\t1\t
                2\tInterstellar\t2014\t8.6\tCooper\t2\t1\t
                3\tThe Bourne Identity\t2014\t7.1\tJason Bourne\t3\t2"""), output);




        // Inner join spaces

        output = jrSQLMain.run("    JOIN     movies     AND      roles    on    id      AND    movieid   ;");
        Assertions.assertTrue(output.contains("""
                id\tmovies.title\tmovies.year\tmovies.rating\troles.name\troles.movieid\troles.actorid\t
                1\tInception\t2010\t8.8\tCobb\t1\t1\t
                2\tInterstellar\t2014\t8.6\tCooper\t2\t1\t
                3\tThe Bourne Identity\t2014\t7.1\tJason Bourne\t3\t2"""), output);


        // Capitilisation

        output = jrSQLMain.run("join   movies     and      roles    on    id      and    movieid   ;");
        Assertions.assertTrue(output.contains("""
                id\tmovies.title\tmovies.year\tmovies.rating\troles.name\troles.movieid\troles.actorid\t
                1\tInception\t2010\t8.8\tCobb\t1\t1\t
                2\tInterstellar\t2014\t8.6\tCooper\t2\t1\t
                3\tThe Bourne Identity\t2014\t7.1\tJason Bourne\t3\t2"""), output);



        /* ********Error testing******** */
        // Mispelled words
        //JOIN

        output = jrSQLMain.run("joiin   movies     and      roles    on    id      and    movieid   ;");
        Assertions.assertTrue(output.contains("[ERROR]"), output);


        //AND

        output = jrSQLMain.run("join   movies     andd      roles    on    id      and    movieid   ;");
        Assertions.assertTrue(output.contains("[ERROR]"), output);



        // Attributes do not exist

        output = jrSQLMain.run("join movies AND roles ON doesNotExist AND movieid;");
        Assertions.assertTrue(output.contains("[ERROR]: Attribute does not exist"), output);

        output = jrSQLMain.run("join movies AND roles ON id AND doesNotExist;");
        Assertions.assertTrue(output.contains("[ERROR]: Attribute does not exist"), output);

        output = jrSQLMain.run("join movies AND roles ON id AND doesNotExist ;");
        Assertions.assertTrue(output.contains("[ERROR]: Attribute does not exist"), output);

    }

    @Test
    @DisplayName("Black box testing: testing compound WHERE statements")
    public void testCompoundWhere() {

        output = jrSQLMain.run("SELECT name FROM actors WHERE (age == 45) AND (sex == 'female');");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        Assertions.assertFalse(output.contains("Tom Hanks"), output);
        Assertions.assertFalse(output.contains("Tom Cruise"), output);
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        Assertions.assertFalse(output.contains("female"), output);

        output = jrSQLMain.run("SELECT name FROM actors WHERE (age > 61.5) OR ((age == 45) AND (sex == 'female'));");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Cruise"));
        Assertions.assertFalse(output.contains("Tom Hanks"), output);
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        Assertions.assertFalse(output.contains("female"), output);

        output = jrSQLMain.run("SELECT name FROM actors WHERE ((age > 61.5) OR ((age == 45) AND (sex == 'female'))) AND (name LIKE 'ia');");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        Assertions.assertFalse(output.contains("Tom Cruise"), output);
        Assertions.assertFalse(output.contains("Tom Hanks"), output);
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        Assertions.assertFalse(output.contains("female"), output);
        Assertions.assertFalse(output.contains("male"), output);

        output = jrSQLMain.run("SELECT name, age FROM actors WHERE (((age > 61.5) OR ((age == 45) AND (sex == 'female'))) AND (name LIKE 'ia')) OR (name like 'Cruise');");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Cruise"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("45"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("63"));
        Assertions.assertFalse(output.contains("Tom Hanks"), output);
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("54"), output);
        Assertions.assertFalse(output.contains("female"), output);
        Assertions.assertFalse(output.contains("male"), output);


        /* ******* Error Testing ******* */
        // Incorrect brackets (last one on the right missing)
        output = jrSQLMain.run("SELECT name FROM actors WHERE (age > 61.5) OR ((age == 45) AND (sex == 'female');");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Missing closing/ opening brackets"));

        // To the left of age
        output = jrSQLMain.run("SELECT name FROM actors WHERE (age > 61.5) OR (age == 45) AND (sex == 'female'));");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Missing closing/ opening brackets"));

    }

    @Test
    @DisplayName("Black box testing: testing WHERE class")
    public void testWhere() {
        output = jrSQLMain.run("SELECT * FROM actors WHERE name == 'Will Smith';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Will Smith"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("54"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("male"));
        Assertions.assertFalse(output.contains("Tom Hanks"), output);
        Assertions.assertFalse(output.contains("Tom Cruise"), output);
        Assertions.assertFalse(output.contains("Julia Roberts"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        Assertions.assertFalse(output.contains("female"), output);

        output = jrSQLMain.run("SELECT name FROM actors WHERE age == 45;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Hanks"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        Assertions.assertFalse(output.contains("Tom Cruise"), output);
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        Assertions.assertFalse(output.contains("male"), output);
        Assertions.assertFalse(output.contains("female"), output);

        output = jrSQLMain.run("SELECT name FROM actors WHERE age != 45;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Will Smith"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Cruise"));
        Assertions.assertFalse(output.contains("Julia Roberts"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        Assertions.assertFalse(output.contains("male"), output);
        Assertions.assertFalse(output.contains("female"), output);

        output = jrSQLMain.run("SELECT name, age FROM actors WHERE name LIKE 'To';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Hanks"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("63"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Cruise"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("45"));
        Assertions.assertFalse(output.contains("Julia Roberts"), output);
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("54"), output);
        Assertions.assertFalse(output.contains("female"), output);
        Assertions.assertFalse(output.contains("male"), output);

        /* ******** Error Testing ******** */
        // Missing single quotes

        output = jrSQLMain.run("SELECT name, age FROM actors WHERE name LIKE To';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Attribute is not enclosed with ''"));
        output = jrSQLMain.run("SELECT name, age FROM actors WHERE name like 'To;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Attribute is not enclosed with ''"));

        // Mispelled 'LIKE'
        output = jrSQLMain.run("SELECT name, age FROM actors WHERE name likee 'To';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));

        // Wrong attribute
        output = jrSQLMain.run("SELECT wrongAttribute1 FROM actors WHERE name == 'Tom';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Attribute does not exist"));
        output = jrSQLMain.run("SELECT wrongAttribute1, wrongAttribute2 FROM actors WHERE name == 'Tom';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: Attribute does not exist"));

        //Mispelled SELECT
        output = jrSQLMain.run("SELEC name FROM actors WHERE name == 'Tom';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));

        //Mispelled FROM
        output = jrSQLMain.run("SELEC name FROMM actors WHERE name == 'Tom';");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));

        // Missing WHERE statement
        output = jrSQLMain.run("SELECT * FROM actors age == 45;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));

        // Missing comma between attribute names
        output = jrSQLMain.run("SELECT name age, sex FROM actors WHERE age == 45;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));
        output = jrSQLMain.run("SELECT name age FROM actors WHERE age == 45;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));
        output = jrSQLMain.run("SELECT name,      age, FROM actors WHERE age == 45;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));

        //Use of LIKE on numerical data
        output = jrSQLMain.run("SELECT * FROM actors WHERE age like 45;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]: String expected"));
    }




    @Test
    @DisplayName("Black box testing: Testing SELECT class")
    public void testSelect() {

        // * SELECT
        output = jrSQLMain.run("SELECT * FROM actors;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Will Smith"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Hanks"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Cruise"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("54"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("45"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("63"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("male"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("female"));

        output = jrSQLMain.run("sElEcT     *      from actors ;  ");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Will Smith"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Hanks"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Cruise"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("54"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("45"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("63"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("male"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("female"));

        // SELECT name, age
        output = jrSQLMain.run("SELECT name, sex FROM actors;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Will Smith"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Hanks"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Tom Cruise"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("Julia Roberts"));
        Assertions.assertFalse(output.contains("54"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        MatcherAssert.assertThat(output, CoreMatchers.containsString("male"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("female"));

        //SELECT sex
        output = jrSQLMain.run("SELECT sex FROM actors;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("male"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("female"));
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("Tom Hanks"), output);
        Assertions.assertFalse(output.contains("Tom Cruise"), output);
        Assertions.assertFalse(output.contains("Julia Roberts"), output);
        Assertions.assertFalse(output.contains("54"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);

        //SELECT id
        output = jrSQLMain.run("SELECT id FROM actors;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("1"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("2"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("3"));
        MatcherAssert.assertThat(output, CoreMatchers.containsString("4"));
        Assertions.assertFalse(output.contains("Will Smith"), output);
        Assertions.assertFalse(output.contains("Tom Hanks"), output);
        Assertions.assertFalse(output.contains("Tom Cruise"), output);
        Assertions.assertFalse(output.contains("Julia Roberts"), output);
        Assertions.assertFalse(output.contains("54"), output);
        Assertions.assertFalse(output.contains("45"), output);
        Assertions.assertFalse(output.contains("63"), output);
        Assertions.assertFalse(output.contains("male"), output);
        Assertions.assertFalse(output.contains("female"), output);

        /* ********* Error testing ********** */
        //Rogue bracket at the end of the line
        output = jrSQLMain.run("SELECT * FROM actors);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));
        //Rogue bracket at the beginning of the line
        output = jrSQLMain.run("(SELECT * FROM actors;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]:"));

    }

    @Test
    @DisplayName("Black box testing: Testing INSERT class")
    public void testInsert() {
        output = jrSQLMain.run("INSERT INTO actors('josh', 27, male);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[OK]"));
        output = jrSQLMain.run("INSERT INTO actors('josh', 27, male, male);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]"));
        output = jrSQLMain.run("INSERT INTO actors('josh', male);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]"));
        output = jrSQLMain.run("INSERT INTO actors('josh' 27 male);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]"));
        output = jrSQLMain.run("INSERT INTO actors('josh', 27, male;");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]"));
        output = jrSQLMain.run("INSERT INTO actors 'josh', 27, male);");
        MatcherAssert.assertThat(output, CoreMatchers.containsString("[ERROR]"));




    }



}
