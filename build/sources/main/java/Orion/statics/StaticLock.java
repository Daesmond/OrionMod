/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.statics;

import Orion.struct.OrionLockBlock;
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
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Daesmond
 */
public class StaticLock extends StaticAbstract {
// For deprecation until I successfully used nbtags
    
    private static StaticLock ConfigLock;
    private final Map<String, String> cMap;
    private final File ConfigDir;
    private final File ConfigFile;
    private final File ConfigTemp;

    public StaticLock() {
        super();
        cMap = Collections.synchronizedMap(new HashMap<>(10000));
        //ConfigDir = new File("d:/prg/orionmod/run/config/Orion");
        ConfigDir = new File(Loader.instance().getConfigDir() + "/Orion");
        ConfigFile = new File(String.format("%s/Locked.json", ConfigDir));
        ConfigTemp = new File(String.format("%s/Locked.json.tmp", ConfigDir));

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
    
    public OrionLockBlock getBlockInfo(String pos) {
        String s = cMap.get(pos);
        
        if (s == null) {
            return null;
        }
        
        return new OrionLockBlock(s) ;
    }
        
    public void Lock(String pos, String jsonline) {
        cMap.put(pos, jsonline);
    }
    
    public void UnLock(String pos) {
        cMap.remove(pos);
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
                    String val = reader.nextString();

                    cMap.put(text, val);
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
            System.out.println("Saving Orion Lock chest/doors Configurations!");

            JsonWriter writer = new JsonWriter(new FileWriter(ConfigTemp));
            Set sMap = cMap.entrySet();
            Iterator iMap = sMap.iterator();

            writer.beginObject();

            while (iMap.hasNext()) {
                Map.Entry mentry = (Map.Entry) iMap.next();
                String text = (String) mentry.getKey();
                String val = (String) mentry.getValue();

                writer.name(text);
                writer.value(val);
            }

            writer.endObject();
            writer.close();

            if (ConfigFile.exists()) {
                ConfigFile.delete();
            }

            if (ConfigTemp.exists()) {
                ConfigTemp.renameTo(ConfigFile);
            }

            System.out.println("Done Saving Orion Lock chest/doors Configurations!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
