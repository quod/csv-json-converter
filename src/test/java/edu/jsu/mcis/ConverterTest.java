package edu.jsu.mcis;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConverterTest {
    private String csvString;
    private String jsonString;

    private static String readFile(String path) throws IOException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        String out = "";

        try {
            while(scanner.hasNextLine()) {
                out += scanner.nextLine() + "\n";
            }
            return out;
        } finally {
            scanner.close();
        }
    }

    @Before
    public void setUp() {
        try {
            csvString = readFile("src/test/resources/grades.csv");
            jsonString = readFile("src/test/resources/grades.json");
        } catch(IOException e) {}
    }
    
    @Test
    public void testConvertCSVtoJSON() {
        // You should test using the files in src/test/resources.
        assertTrue(Converter.jsonStringsAreEqual(Converter.csvToJson(csvString), jsonString));
    }

    @Test
    public void testConvertJSONtoCSV() {
        // You should test using the files in src/test/resources.
        assertEquals(Converter.jsonToCsv(jsonString), csvString);
    }
}







