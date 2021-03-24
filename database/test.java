public class test {

    public static void main(String args[])
    {




        /*

        testFile.createDatabaseFolder("imdb");
        testFile.readFile("files/example_file.jsql", testTable);
        System.out.println(Arrays.toString(testFile.getColumnHeaders()));
        System.out.println(Arrays.toString(testFile.getPrimaryKeys()));
        System.out.println(Arrays.toString(testFile.getForeignKeys()));


        testFile.getRecords();
        testFile.createSchemaFile("testTable", "imdb");
        testFile.createSchemaFile("testTable2", "imdb");


        Database imdb = new Database("imdb");
        Database testDatabase = new Database("test");

        testDatabase.addTable(testTable);
        testTable.addColumn("Name");
        testTable.addColumn("Age");
        testTable.addColumn("Sex");
        testTable.insertRecord("'Josh', 27, M");
        testTable.insertRecord("'Peter', 34, M");
        testTable.insertRecord("'Emily', 23, F");
        testTable.insertRecord("'Emma', 54, F");
        testTable.addNewColumn("Height");
        testTable.deleteRecord(1);
        testTable.updateRecord(2, 0, "'John'");
        testTable.updateRecord(2, 3, "1.7");


        testTable.printFullTable();


        Parser testParser = new Parser(userCommands);
        //Alter


        Search searchItem1 = new Search(1, "25", testTable);
        System.out.println(searchItem1.searchLikeCommand());
        list1 = searchItem1.searchWithOperator(">");



        Search searchItem2 = new Search(0, "Emily", testTable);
        list2 = searchItem2.searchLikeCommand();


        testTable.printFullTable();
        columnsToReturn.add(1);
        columnsToReturn.add(2);
        keysToReturn.add(3);
        keysToReturn.add(4);
        System.out.println("List1/ 2 = " + keysToReturn + columnsToReturn);

        testTable.printPartialTable(keysToReturn, columnsToReturn);

*/
        JrSQLMain newsql = new JrSQLMain();
        System.out.println(newsql.run("   create database imdb ;    "));
        System.out.println(newsql.run("   use markbook ;    "));
/*
       System.out.println(newsql.run("   insert into people values ('josh', 25, 1);    "));
        System.out.println(newsql.run("   insert into people values ('emily', 30, 1);    "));
       System.out.println(newsql.run("   insert into people values ('peter', 19, 2);    "));
        System.out.println(newsql.run("   insert into people values ('paul', 39, 2);    "));
        System.out.println(newsql.run("   insert into people values ('emma', 69, 3);    "));
        System.out.println(newsql.run("   insert into people values ('sara', 34, 3);    "));
        System.out.println(newsql.run("   insert into people values ('sdffds', 67, 1);    "));
        System.out.println(newsql.run("   insert into people values ('sdsdf', 34, 1);    "));
        System.out.println(newsql.run("   insert into people values ('eheer', 65, 2);    "));
        System.out.println(newsql.run("   insert into people values ('erherhff', 45, 2);    "));
        System.out.println(newsql.run("   insert into people values ('hergge', 69, 3);    "));
        System.out.println(newsql.run("   insert into people values ('egasa', 42, 3);    "));
        System.out.println(newsql.run("   insert into people values ('josh', 25, 1);    "));
        System.out.println(newsql.run("   insert into people values ('emily', 30, 1);    "));
        System.out.println(newsql.run("   insert into people values ('peter', 19, 2);    "));
        System.out.println(newsql.run("   insert into people values ('paul', 39, 2);    "));
        System.out.println(newsql.run("   insert into people values ('emma', 69, 3);    "));
        System.out.println(newsql.run("   insert into people values ('sara', 34, 3);    "));
        System.out.println(newsql.run("   insert into people values ('sdffds', 67, 1);    "));
        System.out.println(newsql.run("   insert into people values ('sdsdf', 34, 1);    "));
        System.out.println(newsql.run("   insert into people values ('eheer', 65, 2);    "));
        System.out.println(newsql.run("   insert into people values ('erherhff', 45, 2);    "));
        System.out.println(newsql.run("   insert into people values ('hergge', 69, 3);    "));
        System.out.println(newsql.run("   insert into people values ('egasa', 42, 3);    "));

*/

        //System.out.println(newsql.run("   creaTe TablE profession(name, profession_id);    "));
        /*
        System.out.println(newsql.run("   insert into profession values ('actor', 1);    "));
        System.out.println(newsql.run("   insert into profession values ('writer', 2);    "));
        System.out.println(newsql.run("   insert into profession values ('engineer', 3);    "));

         */

       //System.out.println(newsql.run("   join  people  and profession on profession_id and id  ;  "));
       // System.out.println(newsql.run("   join  people  and profession on id and profession_id;    "));

        // System.out.println(newsql.run("   select * from profession ;    "));
     //   System.out.println(newsql.run("   select * from people ;    "));

        // System.out.println(newsql.run("   update randomtable SET age =99, sex = paul, d.o.b. = 01.01.01 where (age > 25) ;    "));
        System.out.println(newsql.run("  select pass from marks where ((name=='steve') or (mark==75)) ;    "));

     //   System.out.println(newsql.run("   delete from marks where (name=='steve') and (mark == 75) ;    "));






        //System.out.println(newsql.run("   insert into testtablenumber2 values ('josh actor', 25, 08.12.1992, MALE);    "));









//        System.out.println(newsql.run("SELECT * FROM testtable WHERE (awards > 5) AND (nationality == 'British');"));


      //  System.out.println(newsql.run("         SELECT name FROM people (where name like r);"));
        //System.out.println(newsql.run("         SELECT name, age FROM testtable where name != 'bob';"));
        //System.out.println(newsql.run("SELECT id FROM people WHERE name == 'peter';"));
     //   System.out.println(newsql.run("SELECT * FROM people WHERE (profession_id==2) AND ((age>68) OR (name LIKE 'jo'));"));
       // System.out.println(newsql.run("SELECT * FROM people WHERE (profession_id==2) or (age>20);"));


        /*
        Select does =
        Gets columns to select in List<integer> columnIds
        Gets the table in tableToPrint
        Gets the bracket count in List<integer> bracketCount
        prints select statement without where

        Need to find -
        use search statement to find

         */






    }

}
