import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Reads/writes using the schema found in project-files/file_schema.pdf
public class FileIO {

    public FileIO() {
    }

    private List<String> linesInFile = new ArrayList<>();


    public void readFile(String fileName, Table tableName) {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                this.linesInFile.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns the table title from a file as a String
    private String getTableTitle() {
        String tableTitle = linesInFile.get(0);
        return tableTitle.replace("TableName: ", "");
    }

    // Returns the column headers from the file into a String array
     String[] getColumnHeaders() {
         // Get the line in the file containing the headers
         String originalString = linesInFile.get(3);
         // Remove whitespace and split by '|||'
         originalString = originalString.replaceAll("\\s+","");
         return originalString.split("\\|\\|\\|");
    }

    // Returns the primary keys from the file into a String array
    String[] getPrimaryKeys() {
        // Get the line in the file containing the headers
        String originalString = linesInFile.get(5);
        // Remove whitespace and split by '||'
        originalString = originalString.replaceAll("\\s+","");
        return originalString.split("\\|\\|");
    }

    // Returns the primary keys from the file into a String array
    String[] getForeignKeys() {
        int i = 3;
        String line = linesInFile.get(i);
        // Begin from the records within the text file
        while (!line.equals("ForeignKeys:")) {
            i++;
            line = linesInFile.get(i);
        }
        line = linesInFile.get(++i);

        // Remove whitespace and split by '~'
        line = line.replaceAll("\\s+","");
        return line.split("~");
    }

    List<String[]> getRecords() {
        List<String[]> records = new ArrayList<>();
        String line;
        // Begin from the records within the text file
        int i = 7;
        line = linesInFile.get(i);
        // Scan through all records until we reach the foreign key section
        while (!line.equals("ForeignKeys:")) {
            String newString = line;
            line = line.replaceAll("\\s+","");
            String[] splitString = line.split("\\|");
            records.add(splitString);
            i++;
            line = linesInFile.get(i);
        }
        return records;
    }



    void createEmptyFile(String tableName) {
        try {
            File myObj = new File("files" + File.separator + tableName + ".jsql");
            if (myObj.createNewFile()) { System.out.println("File created: " + myObj.getName()); }
            else { System.out.println("File already exists."); }
        } catch (IOException e) {
            System.out.println("Error with file writing");
            e.printStackTrace();
        }
    }

    // Create a schema file
    public void createSchemaFile(String tableName) {
        try {
            FileWriter writer = new FileWriter("files" + File.separator + tableName + ".jsql");
            writer.write("TableName:\n" + tableName);
            writeItem(writer, getColumnHeaders(), "|||", "\nColumns");
            writeItem(writer, getPrimaryKeys(), "||", "\nPrimaryKeys");
            writeRecords(writer, getRecords());
            writeItem(writer, getForeignKeys(), "~", "ForeignKeys");
            writer.close();
            System.out.println("Successfully wrote to " + tableName + ".jsql");
        } catch (IOException e) {
            System.out.println("Error creating schema file.");
            e.printStackTrace();
        }
    }


    // Write the record/s to the file with a '|' deliminator
    private void writeRecords(FileWriter writer, List<String[]> Records) {
        try {
            writer.write("\nRecords:\n");
            for (String[] record : Records) {
                for (int i = 0; i < getColumnHeaders().length; i++) {
                    writer.write(record[i] + " | ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing record\s");
        }
    }

    // Write the items to the file with a specified deliminator
    private void writeItem(FileWriter writer, String[] items, String deliminator, String itemToWrite) {
        try {
            writer.write(itemToWrite + ":\n");
            for (String item : items) {
                writer.write(item + " " + deliminator + " ");
            }
        } catch (IOException e) {
            System.out.println("Error writing item\s");
        }
    }


}


