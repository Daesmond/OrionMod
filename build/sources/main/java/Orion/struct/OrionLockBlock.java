/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.struct;

import Orion.OrionMain;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.StringReader;
import java.io.StringWriter;
import net.minecraft.util.math.BlockPos;

/**
 *
 * @author Daesmond
 */
public class OrionLockBlock {

    private static String sByName = "ByName";
    private static String sItemName = "ItemName";
    private static String sisLocked = "isLocked";

    public OrionLockBlock() {
        this.Clear();
    }

    public OrionLockBlock(String text) {
        this.Clear();
        this.jsonParse(text);
    }

    public void Clear() {
        ByName = null;
        ItemName = null;
        isLocked = null;
        pos = null;
        axis = null;
    }

    public void jsonParse(String text) {
        try {
            StringReader reader = new StringReader(text.trim());
            JsonReader jsonReader = new JsonReader(reader);

            jsonReader.beginObject();

            JsonParser parser = new JsonParser();
            JsonObject jsonObj = (JsonObject) parser.parse(jsonReader);

            if (!jsonObj.get(sByName).isJsonNull()) {
                ByName = jsonObj.get(sByName).getAsString();
            }

            if (!jsonObj.get(sItemName).isJsonNull()) {
                ItemName = jsonObj.get(sItemName).getAsString();
            }

            if (!jsonObj.get(sisLocked).isJsonNull()) {
                isLocked = jsonObj.get(sisLocked).getAsString();
            }

            jsonReader.endObject();
            jsonReader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setAxis(String text) {
        String[] split = text.split("\\|");

        axis = text;
        pos = new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public void setAxis(BlockPos bp) {
        axis = OrionMain.PosToStr(bp);
        pos = bp;
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
            writer.name(sisLocked);
            writer.value(isLocked);

            writer.endObject();
            writer.close();
            
            ret = stringwriter.toString();
        } catch (Exception ex) {
            ret = null;
        }

        return ret;
    }

    public BlockPos pos;
    public String axis;
    public String ByName;
    public String ItemName;
    public String isLocked;
}
