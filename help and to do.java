// Everything with files needs to be inside a try statement which could catch IOException and/or FileNotFoundException
// depending on what the code is doing

// Files are accessed through a string i.e.
String filename = "file.txt";
// or use file separators i.e.
        String filename = "..home" + File.separator + "javaCode" + File.separator + "file.txt";

// Open file
        String name = "email" + File.separator + "cv.txt";
        File fileToOpen = new File(name);

// Check if exists
        if(fileToOpen.exists()) {
        // Do something to the file
        }

// If the file doesn't exist
        fileToOpen.createNewFile();


// Create a new directory with mkdir
        String name = "email";
        File emailFolder = new File(name);
        emailFolder.mkdir();

//Create multiple levels of a directory with mkdirs
        String name = "docs" + File.separator + "email";
        File emailFolder = new File(mame);
        emailFolder.mkdirs();


//Writing to a file (basic)
        FileWriter writer = new FileWriter(fileToOpen);
        writer.write("Hello\n");
        writer.write('a');
        writer.flush();
        writer.close();

//Reading to a file (basic)
        FileReader reader = new FileReader(fileToOpen);
        char[] buffer = new char[10];
        reader.read(buffer, 0, buffer.length);
        reader.close();

// Buffered reading (better)
        FileReader reader = new FileReader(fileToOpen);
        BufferedReader buffReader = new BufferedReader(reader);
        String firstLine = buffReader.readline();
        buffReader.close();

// Other useful methods of the file class
        - delete
        - renameTo
        - setReadable
        - setWritable
        - list
        - getName
        - getPath
        - getParent
        - length

