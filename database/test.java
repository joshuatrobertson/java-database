import Parser.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String args[])
    {

        FileIO testFile = new FileIO();
        Table testTable = new Table("testTable");
        Table testTable1 = new Table("testTable");
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<Integer> columnsToReturn = new ArrayList<>();
        List<Integer> keysToReturn = new ArrayList<>();


        testFile.readFile("files/example_file.jsql", testTable);
        System.out.println(Arrays.toString(testFile.getColumnHeaders()));
        System.out.println(Arrays.toString(testFile.getPrimaryKeys()));
        System.out.println(Arrays.toString(testFile.getForeignKeys()));


        testFile.getRecords();
        testFile.createSchemaFile("testTable");

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


        Parser testParser = new Parser();
        //Alter
        System.out.println(testParser.parseText("ALTER TABLE actors DROP age"));
        System.out.println(testParser.parseText("ALTER TABLE actors add age"));


        System.out.println(testParser.parseText("ALTER TABLE actors add age"));

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


    }

}
