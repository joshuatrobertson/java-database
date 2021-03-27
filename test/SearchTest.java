import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SearchTest {

    Search searchTest;
    @BeforeEach
    public void setUp() {
        Table testTable = new Table("testTable");
        testTable.addColumn("name");
        testTable.addColumn("age");
        testTable.addColumn("sex");
        testTable.addColumn("height");
        testTable.insertNewEntry("'josh', 27, 'male', 173");
        testTable.insertNewEntry("'adam', 10, 'male', 172");
        testTable.insertNewEntry("'sarah', 20, 'female', 145");
        testTable.insertNewEntry("'peter', 40, 'male', 165");
        testTable.insertNewEntry("'john', 86, 'male', 100");
        testTable.insertNewEntry("'jess', 34, 'female', 148");
        testTable.insertNewEntry("'katrina', 35, 'female', 185");

        searchTest = new Search(testTable);
    }

    @Test
    @DisplayName("Testing Search class: Search string")
    public void testSearchString() {
        List<Integer> searchResults = searchTest.searchString("like", "jo", 0);
        // Results should just be keys 1 and 5 i.e. josh and john
        Integer[] expectedResults = {1, 5};
        Assertions.assertArrayEquals(expectedResults, searchResults.toArray());

        // Results should be all due to 'like' male including female
        List<Integer> searchResults2 = searchTest.searchString("like", "male", 2);
        // Results should just be keys 1 and 5 i.e. josh and john
        Integer[] expectedResults2 = {1, 2, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expectedResults2, searchResults2.toArray());

        List<Integer> searchResults3 = searchTest.searchString("==", "josh", 0);
        // Results should just return josh entry
        Integer[] expectedResults3 = {1};
        Assertions.assertArrayEquals(expectedResults3, searchResults3.toArray());

        List<Integer> searchResults4 = searchTest.searchString("!=", "josh", 0);
        // Results should just return all other than josh
        Integer[] expectedResults4 = {2, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expectedResults4, searchResults4.toArray());
    }

    @Test
    @DisplayName("Testing Search class: Search with operator")
    public void testSearchOperator() {
        List<Integer> searchResults = searchTest.searchWithOperator("==", "172", 3);
        Integer[] expectedResults = {2};
        Assertions.assertArrayEquals(expectedResults, searchResults.toArray());

        List<Integer> searchResults2 = searchTest.searchWithOperator(">", "172", 3);
        Integer[] expectedResults2 = {1, 7};
        Assertions.assertArrayEquals(expectedResults2, searchResults2.toArray());

        List<Integer> searchResults3 = searchTest.searchWithOperator("<=", "172", 3);
        Integer[] expectedResults3 = {2, 3, 4, 5, 6};
        Assertions.assertArrayEquals(expectedResults3, searchResults3.toArray());

        List<Integer> searchResults4 = searchTest.searchWithOperator(">=", "172", 3);
        Integer[] expectedResults4 = {1, 2, 7};
        Assertions.assertArrayEquals(expectedResults4, searchResults4.toArray());

        List<Integer> searchResults5 = searchTest.searchWithOperator("<", "172", 3);
        Integer[] expectedResults5 = {3, 4, 5, 6};
        Assertions.assertArrayEquals(expectedResults5, searchResults5.toArray());

        List<Integer> searchResults6 = searchTest.searchWithOperator("!=", "172", 3);
        Integer[] expectedResults6 = {1, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expectedResults6, searchResults6.toArray());
    }

    @Test
    @DisplayName("Testing Search class: Search with operator (floats)")
    public void testSearchOperatorFloats() {
        List<Integer> searchResults = searchTest.searchWithOperator("==", "172.0", 3);
        Integer[] expectedResults = {2};
        Assertions.assertArrayEquals(expectedResults, searchResults.toArray());

        List<Integer> searchResults2 = searchTest.searchWithOperator(">", "171.5", 3);
        Integer[] expectedResults2 = {1, 2, 7};
        Assertions.assertArrayEquals(expectedResults2, searchResults2.toArray());

        List<Integer> searchResults3 = searchTest.searchWithOperator("<=", "130.2", 3);
        Integer[] expectedResults3 = {5};
        Assertions.assertArrayEquals(expectedResults3, searchResults3.toArray());

        List<Integer> searchResults4 = searchTest.searchWithOperator(">=", "184.9", 3);
        Integer[] expectedResults4 = {7};
        Assertions.assertArrayEquals(expectedResults4, searchResults4.toArray());

        List<Integer> searchResults5 = searchTest.searchWithOperator("<", "172.1", 3);
        Integer[] expectedResults5 = {2, 3, 4, 5, 6};
        Assertions.assertArrayEquals(expectedResults5, searchResults5.toArray());

        List<Integer> searchResults6 = searchTest.searchWithOperator("!=", "172.0", 3);
        Integer[] expectedResults6 = {1, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expectedResults6, searchResults6.toArray());

    }
}