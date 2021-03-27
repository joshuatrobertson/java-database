public class UniTestingScript {

    public static void main(String args[]) {

        /* 1st section */

        JrSQLMain newsql = new JrSQLMain();
        System.out.println(newsql.run("         Create database markbook;"));
        System.out.println(newsql.run("         use  markbook;"));
        System.out.println(newsql.run("  CREATE TABLE marks (name, mark, pass);"));
        System.out.println(newsql.run("   INSERT INTO marks VALUES ('Steve', 65, true);"));
        System.out.println(newsql.run("   INSERT INTO marks VALUES ('Dave', 55, true);"));
        System.out.println(newsql.run("   INSERT INTO marks VALUES ('Bob', 35, false);"));
        System.out.println(newsql.run("     INSERT INTO marks VALUES ('Clive', 20, false);"));
        System.out.println(newsql.run("     SELECT * FROM marks;"));
        System.out.println(newsql.run("    SELECT * FROM marks WHERE name != 'Dave';"));
        System.out.println(newsql.run(" SELECT * FROM marks WHERE pass == true;"));
        System.out.println(newsql.run("     UPDATE marks SET mark = 38 WHERE name == 'Clive';"));
        System.out.println(newsql.run("     SELECT * FROM marks WHERE name == 'Clive';"));
        System.out.println(newsql.run("     DELETE FROM marks WHERE name == 'Dave';"));
        System.out.println(newsql.run("     SELECT * FROM marks;"));
        System.out.println(newsql.run("   DELETE FROM marks WHERE mark < 40;"));
        System.out.println(newsql.run("   SELECT * FROM marks;"));

        /* 2nd section */

        System.out.println(newsql.run("        USE imdb;"));
        System.out.println(newsql.run("        DROP TABLE actors;"));
        System.out.println(newsql.run("         DROP TABLE movies;"));
        System.out.println(newsql.run("        DROP TABLE roles;"));
        System.out.println(newsql.run("         DROP DATABASE imdb;"));
        System.out.println(newsql.run("         CREATE DATABASE imdb;"));
        System.out.println(newsql.run("        USE imdb;"));
        System.out.println(newsql.run("        CREATE TABLE actors (name, nationality, awards);"));
        System.out.println(newsql.run("       INSERT INTO actors VALUES ('Hugh Grant', 'British', 3);"));
        System.out.println(newsql.run("        INSERT INTO actors VALUES ('Toni Collette', 'Australian', 12);"));
        System.out.println(newsql.run("       INSERT INTO actors VALUES ('James Caan', 'American', 8);"));
        System.out.println(newsql.run("         INSERT INTO actors VALUES ('Emma Thompson', 'British', 10);"));
        System.out.println(newsql.run("      CREATE TABLE movies (name, genre);"));
        System.out.println(newsql.run("       INSERT INTO movies VALUES ('Mickey Blue Eyes', 'Comedy');"));
        System.out.println(newsql.run("     INSERT INTO movies VALUES ('About a Boy', 'Comedy');"));
        System.out.println(newsql.run("       INSERT INTO movies VALUES ('Sense and Sensibility', 'Period Drama');"));
        System.out.println(newsql.run("    SELECT id FROM movies WHERE name == 'Mickey Blue Eyes';   "));
        System.out.println(newsql.run("     SELECT id FROM movies WHERE name == 'About a Boy';    "));
        System.out.println(newsql.run("    SELECT id FROM movies WHERE name == 'Sense and Sensibility';    "));
        System.out.println(newsql.run("   SELECT id FROM actors WHERE name == 'Hugh Grant';   "));
        System.out.println(newsql.run("    SELECT id FROM actors WHERE name == 'Toni Collette';   "));
        System.out.println(newsql.run("       SELECT id FROM actors WHERE name == 'James Caan';  "));
        System.out.println(newsql.run("      SELECT id FROM actors WHERE name == 'Emma Thompson';  "));
        System.out.println(newsql.run("     CREATE TABLE roles (name, movieid, actorid); "));
        System.out.println(newsql.run("    INSERT INTO roles VALUES ('Edward', 3, 1);   "));
        System.out.println(newsql.run("      INSERT INTO roles VALUES ('Frank', 1, 3);   "));
        System.out.println(newsql.run("     INSERT INTO roles VALUES ('Fiona', 2, 2);   "));
        System.out.println(newsql.run("     INSERT INTO roles VALUES ('Elinor', 3, 4); "));

        /* Advanced Queries */

        System.out.println(newsql.run("      SELECT * FROM actors WHERE awards < 5;    "));
        System.out.println(newsql.run("     ALTER TABLE actors ADD age;     "));
        System.out.println(newsql.run("     SELECT * FROM actors;     "));
        System.out.println(newsql.run("     UPDATE actors SET age = 45 WHERE name == 'Hugh Grant';     "));
        System.out.println(newsql.run(" SELECT * FROM actors WHERE name == 'Hugh Grant';         "));
        System.out.println(newsql.run("   SELECT nationality FROM actors WHERE name == 'Hugh Grant';       "));
        System.out.println(newsql.run("    ALTER TABLE actors DROP age;      "));
        System.out.println(newsql.run("     SELECT * FROM actors WHERE name == 'Hugh Grant';     "));
        System.out.println(newsql.run("            SELECT * FROM actors WHERE (awards>5) AND (nationality=='British'); "));
        System.out.println(newsql.run("   SELECT * FROM actors WHERE (awards > 5) AND ((nationality == 'British') OR (nationality == 'Australian'));  "));
        System.out.println(newsql.run("     SELECT * FROM actors WHERE name LIKE 'an';  "));
        System.out.println(newsql.run("    SELECT * FROM actors WHERE awards>=10;   "));
        System.out.println(newsql.run("  DELETE FROM actors WHERE name == 'Hugh Grant';     "));
        System.out.println(newsql.run("   DELETE FROM actors WHERE name == 'James Caan';    "));
        System.out.println(newsql.run("     DELETE FROM actors WHERE name == 'Emma Thompson';  "));
        System.out.println(newsql.run("       JOIN actors AND roles ON id AND actorid;"));
        System.out.println(newsql.run("   JOIN movies AND roles ON id AND movieid;   "));
        System.out.println(newsql.run("   SELECT * FROM actors;    "));
        System.out.println(newsql.run("    USE imdb;  "));


        /* Robustness Testing Queries */

        System.out.println(newsql.run("   SELECT * FROM actors   "));
        System.out.println(newsql.run("    SELECT * FROM crew;  "));
        System.out.println(newsql.run("  SELECT spouse FROM actors;    "));
        System.out.println(newsql.run("    SELECT * FROM actors);  "));
        System.out.println(newsql.run("  SELECT * FROM actors WHERE name == 'Hugh Grant;    "));
        System.out.println(newsql.run(" SELECT * FROM actors WHERE name > 10;    "));

        System.out.println(newsql.run("   SELECT name awards FROM actors;  "));
        System.out.println(newsql.run("SELECT * FROM actors awards > 10;  "));
        System.out.println(newsql.run("  SELECT * FROM actors WHERE name LIKE 10;    "));
        System.out.println(newsql.run("  SELECT * FROM actors WHERE awards > 10;    "));
        System.out.println(newsql.run("   USE ebay;   "));



        /*



        System.out.println(newsql.run("      "));
        System.out.println(newsql.run("      "));
        System.out.println(newsql.run("      "));

        */
    }


}