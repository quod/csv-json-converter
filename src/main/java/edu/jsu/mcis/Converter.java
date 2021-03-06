package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        CSVParser parser = new CSVParser();
        BufferedReader reader = new BufferedReader(new StringReader(csvString));
        JSONObject json = new JSONObject();
        
        JSONArray colHeaders = new JSONArray();
        colHeaders.add("Total");
        colHeaders.add("Assignment 1");
        colHeaders.add("Assignment 2");
        colHeaders.add("Exam 1");
        json.put("colHeaders", colHeaders);

        JSONArray rowHeaders = new JSONArray();
        json.put("rowHeaders", rowHeaders);

        JSONArray data = new JSONArray();
        json.put("data", data);
        
        try {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] entry = parser.parseLine(line);
                rowHeaders.add(entry[0]);
                JSONArray row = new JSONArray();
                row.add(new Integer(entry[1]));
                row.add(new Integer(entry[2]));
                row.add(new Integer(entry[3]));
                row.add(new Integer(entry[4]));
                data.add(row);
            }
        } catch(IOException e) {
            e.printStackTrace();
          }
        return json.toString();
    }

    public static String jsonToCsv(String jsonString) {
        JSONObject json = null;

        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
          }
        
        String csv = "\"ID\"," + Converter.<String>joinArrays((JSONArray) json.get("colHeaders")) + "\n";
        
        JSONArray headers = (JSONArray) json.get("rowHeaders");
        JSONArray data = (JSONArray) json.get("data");

        for (int i = 0, length = headers.size(); i < length; i++) {
            csv += ("\"" + (String) headers.get(i) + "\"," +
            Converter.<Integer>joinArrays((JSONArray) data.get(i)) + "\n");
        }
        return csv;
    }

    @SuppressWarnings("unchecked")
    private static String joinArrays(JSONArray array) {
        String out = "";
        for (int i = 0, length = array.size(); i < length; i++) {
            out += "\"" + (array.get(i)) + "\"";
            if (i < length - 1) {
                out += ",";
            }
        }
        return out;
    }

    private static boolean jsonObjectsAreEqual(JSONObject aObj, JSONObject bObj) {
        for (Object cObj : aObj.keySet()) {
            String key = (String) cObj;
            if (!bObj.containsKey(key) || !jsonEqual(aObj.get(key), bObj.get(key))) {
                return false;
            }
        }
        return true;
    }

    public static boolean jsonStringsAreEqual(String s, String t) {
        try {
            return jsonEqual(new JSONParser().parse(s), new JSONParser().parse(t));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean jsonArraysAreEqual(JSONArray array1, JSONArray array2) {
        if (array1.size() != array2.size()) {
            return false;
        } else {
            for (int i = 0, length = array1.size(); i < length; i++) {
                if (!jsonEqual(array1.get(i), array2.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean jsonEqual(Object aObj, Object bObj) {
        if (aObj instanceof JSONObject && bObj instanceof JSONObject) {
            return jsonObjectsAreEqual((JSONObject) aObj, (JSONObject) bObj);
        } else if (aObj instanceof JSONArray && bObj instanceof JSONArray) {
            return jsonArraysAreEqual((JSONArray) aObj, (JSONArray) bObj);
        } else {
            return aObj.equals(bObj);
        }
    }
}
