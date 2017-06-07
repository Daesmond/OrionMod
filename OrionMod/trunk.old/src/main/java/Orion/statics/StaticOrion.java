/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.statics;

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
public class StaticOrion {

    private static StaticOrion ConfigOrion;
    private static final String SPAWN_DEFAULT = "SpawnDefault";
    private static final String ORIONCONFIG = "CONFIG";
    private static final String CREEPERS_BLOWS = "CreepersBlows";
    private final Map<String, HashMap> cMap;
    private final Map<String, String> Config;
    private final File ConfigDir;
    private final File ConfigFile;
    private final File ConfigTemp;
    private int ticks;
    private boolean isupdateneeded;
    private boolean isupdating;

    private StaticOrion() {
        System.out.println(Thread.currentThread().getStackTrace());
        
        cMap = Collections.synchronizedMap(new HashMap<String, HashMap>(1000));
        //ConfigDir = new File("d:/prg/orionmod/run/config/Orion");
        ConfigDir = new File(Loader.instance().getConfigDir() + "/Orion");
        ConfigFile = new File(String.format("%s/OrionConfig.json", ConfigDir));
        ConfigTemp = new File(String.format("%s/OrionConfig.json.tmp", ConfigDir));
        ticks = 0;
        isupdateneeded = false;
        isupdating = false;

        if (!ConfigDir.exists()) {
            ConfigDir.mkdir();
        }

        if (!ConfigFile.exists()) {
            HashMap<String, String> cb = new HashMap<>();

            cb.put(CREEPERS_BLOWS, "false");

            cMap.put(SPAWN_DEFAULT, new HashMap<>());
            cMap.put(ORIONCONFIG, cb);
            SaveConfig();
        }

        LoadConfig();
        Config = cMap.get(ORIONCONFIG);
//        System.out.println(Config.size());
    }

    public static String getCallerCallerClassName() {
        System.out.println("Caller Class");
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(StaticOrion.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName();
                }
            }
        }
        return null;
    }

    public static StaticOrion getConfig() {
        if (ConfigOrion == null) {
            //System.out.println(getCallerCallerClassName());
            ConfigOrion = new StaticOrion();
        }
        return ConfigOrion;
    }

    public String getMySpawnPrintable(String pname) {
        try {
            Object o = cMap.get(pname);

            if (o == null) {
                return getDefaultSpawnPrintable();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return String.format("X=>%s  Y=>%s  Z=>%s", getMySpawnAxis(pname, "X"), getMySpawnAxis(pname, "Y"), getMySpawnAxis(pname, "Z"));
    }

    public String getDefaultSpawnPrintable() {
        return String.format("X=>%s  Y=>%s  Z=>%s", getDefaultSpawnAxis("X"), getDefaultSpawnAxis("Y"), getDefaultSpawnAxis("Z"));
    }

    public void setMySpawnAxis(String pname, String axis, String value) {
        try {
            Object o = cMap.get(pname);

            if (o == null) {
                cMap.put(pname, new HashMap<String, String>());
            }

            HashMap<String, String> hString = (HashMap<String, String>) cMap.get(pname);

            hString.put(axis, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getMySpawnAxis(String pname, String axis) {
        try {
            Object o = cMap.get(pname);

            if (o == null) {
                return getDefaultSpawnAxis(axis);
            }

            HashMap<String, String> hString = (HashMap<String, String>) cMap.get(pname);

            return (String) hString.get(axis);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String getDefaultSpawnAxis(String axis) {
        HashMap<String, String> hString = (HashMap<String, String>) cMap.get(SPAWN_DEFAULT);

        return (String) hString.get(axis);
    }

    public void setDefaultSpawnAxis(String axis, String value) {
        HashMap<String, String> hString = (HashMap<String, String>) cMap.get(SPAWN_DEFAULT);

        hString.put(axis, value);
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

    public boolean AllowCreeperToExplode() {
        return Boolean.parseBoolean(Config.get(CREEPERS_BLOWS));
    }

    private void LoadConfig() {
        try {

            System.out.println("Loading Orion Configurations!");

            JsonReader reader = new JsonReader(new FileReader(ConfigFile));
            reader.beginObject();

            while (reader.hasNext()) {
                JsonToken tokenType = reader.peek();
                String name = null;

                if (tokenType == JsonToken.NAME) {
                    name = reader.nextName();
                    cMap.put(name, new HashMap<String, String>());
                } else {
                    reader.skipValue();
                }

                tokenType = reader.peek();

                if (tokenType == JsonToken.BEGIN_OBJECT) {
                    HashMap<String, String> hString = (HashMap<String, String>) cMap.get(name);
                    reader.beginObject();

                    while (reader.hasNext()) {
                        JsonToken tokenType2 = reader.peek();

                        if (tokenType2 == JsonToken.NAME) {
                            String text = reader.nextName();
                            String value = reader.nextString();

                            hString.put(text, value);
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();

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

            System.out.println("Saving Orion Configurations!");

            JsonWriter writer = new JsonWriter(new FileWriter(ConfigTemp));
            Set sMap = cMap.entrySet();
            Iterator iMap = sMap.iterator();

            writer.beginObject();

            while (iMap.hasNext()) {
                Map.Entry mentry = (Map.Entry) iMap.next();

                if (mentry.getValue() instanceof HashMap) {
                    String name = (String) mentry.getKey();

                    writer.name(name);

                    HashMap<String, String> hString = (HashMap<String, String>) mentry.getValue();
                    Set sString = hString.entrySet();
                    Iterator iString = sString.iterator();

                    writer.beginObject();

                    while (iString.hasNext()) {
                        Map.Entry mString = (Map.Entry) iString.next();
                        String text = (String) mString.getKey();
                        String val = (String) mString.getValue();

                        writer.name(text);
                        writer.value(val);
                    }
                    writer.endObject();
                }
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
            System.out.println("Done Saving Orion Configurations!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
