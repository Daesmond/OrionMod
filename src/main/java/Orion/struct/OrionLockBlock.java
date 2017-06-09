/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.struct;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.StringReader;
import java.io.StringWriter;

/**
 *
 * @author Daesmond
 */
public class OrionLockBlock {

    private static String sByName = "ByName";
    private static String sItemName = "ItemName";
    public String ByName;
    public String ItemName;

    public OrionLockBlock() {
        this.Clear();
    }

    public OrionLockBlock(String text) {
        this.Clear();
        this.jsonParse(text);
    }

    public OrionLockBlock(String byname, String itemname) {
        this.Clear();
        this.setLockInfo(byname, itemname);
    }

    public void Clear() {
        ByName = null;
        ItemName = null;
    }

    public void jsonParse(String text) {
        try {    
            StringReader reader = new StringReader(text.trim());
            JsonReader jsonReader = new JsonReader(reader);

            JsonParser parser = new JsonParser();
            JsonObject jsonObj = (JsonObject) parser.parse(jsonReader);

            if (!jsonObj.get(sByName).isJsonNull()) {
                ByName = jsonObj.get(sByName).getAsString();
            }

            if (!jsonObj.get(sItemName).isJsonNull()) {
                ItemName = jsonObj.get(sItemName).getAsString();
            }

            jsonReader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getJsonLine() {
        String ret = null;
        try {
            StringWriter stringwriter = new StringWriter();
            JsonWriter writer = new JsonWriter(stringwriter);

            writer.beginObject();

            writer.name(sByName);
            writer.value(ByName);
            writer.name(sItemName);
            writer.value(ItemName);

            writer.endObject();
            writer.close();

            ret = stringwriter.toString();
        } catch (Exception ex) {
            ret = null;
        }

        return ret;
    }

    public void setLockInfo(String byname, String itemname) {
        ByName = byname;
        ItemName = itemname;
    }

    public boolean isLockedBy(String byname) {
        return byname.equals(ByName);
    }

}
