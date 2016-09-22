package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class JSONCompare {
    
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