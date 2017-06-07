/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.statics;

import Orion.struct.OrionLockBlock;
import Orion.struct.OrionProtectBlock;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Daesmond
 */
public class StaticLock {

    private static StaticLock ConfigLock;
    private final Map<String, OrionLockBlock> cMap;
    private final File ConfigDir;
    private final File ConfigFile;
    private final File ConfigTemp;
    private int ticks;
    private boolean isupdateneeded;
    private boolean isupdating;

    public StaticLock() {
        cMap = Collections.synchronizedMap(new HashMap<>(10000));
        //ConfigDir = new File("d:/prg/orionmod/run/config/Orion");
        ConfigDir = new File(Loader.instance().getConfigDir() + "/Orion");
        ConfigFile = new File(String.format("%s/Locked.json", ConfigDir));
        ConfigTemp = new File(String.format("%s/Locked.json.tmp", ConfigDir));
        ticks = 0;
        isupdateneeded = false;
        isupdating = false;

        if (!ConfigDir.exists()) {
            ConfigDir.mkdir();
        }

        if (!ConfigFile.exists()) {
            SaveConfig();
        }

        LoadConfig();
    }

    public static StaticLock getConfig() {
        if (ConfigLock == null) {
            ConfigLock = new StaticLock();
        }
        return ConfigLock;
    }

    public void incTicks() {
        ticks += 1;
    }

    public int getTicks() {
        return ticks;
    }

    public void resetTicks() {
        ticks = 0;
    }

    public boolean IsUpdateNeeded() {
        return isupdateneeded;
    }

    public void setForUpdate() {
        isupdateneeded = true;
    }

    public void setNotForUpdate() {
        isupdateneeded = false;
    }

    private void LoadConfig() {
        try {

            System.out.println("Loading Orion Lock chest/doors Configurations!");

            JsonReader reader = new JsonReader(new FileReader(ConfigFile));
            reader.beginObject();

            while (reader.hasNext()) {
                JsonToken tokenType = reader.peek();

                if (tokenType == JsonToken.NAME) {
                    String text = reader.nextName();
                    OrionLockBlock olb = new OrionLockBlock();

                    JsonToken tokenType2 = reader.peek();
                    
                    
                    if (tokenType2 == JsonToken.BEGIN_OBJECT) {
                        reader.beginObject();
                        
                        while (reader.hasNext()) {
                            String text2 = reader.nextName();
                            String value = reader.nextString();
                        }
                        
                        reader.endObject();
                    }
                    
                    
                    //olb.ByName = value;
                    //opb.axis = text;
                    //opb.pos = new BlockPos(Integer.parseInt(axis[0]), Integer.parseInt(axis[1]), Integer.parseInt(axis[2]));

                    cMap.put(text, olb);
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SaveConfig() {
        try {
            if (isupdating) {
                return;
            }

            isupdating = true;

            System.out.println("Saving Orion Lock chest/doors Configurations!");

            JsonWriter writer = new JsonWriter(new FileWriter(ConfigTemp));
            Set sMap = cMap.entrySet();
            Iterator iMap = sMap.iterator();

            writer.beginObject();

            while (iMap.hasNext()) {
                Map.Entry mentry = (Map.Entry) iMap.next();
                String text = (String) mentry.getKey();
                OrionProtectBlock val = (OrionProtectBlock) mentry.getValue();

                writer.name(text);
                writer.value(val.ByName);
            }

            writer.endObject();
            writer.close();

            if (ConfigFile.exists()) {
                ConfigFile.delete();
            }

            if (ConfigTemp.exists()) {
                ConfigTemp.renameTo(ConfigFile);
            }

            isupdating = false;
            System.out.println("Done Saving Orion Lock chest/doors Configurations!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
