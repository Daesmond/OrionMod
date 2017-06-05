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
public class StaticUsers extends StaticAbstract {

    private static StaticUsers ConfigUsers;
    private final Map<String, String> cPlayer;
    private final File ConfigDir;
    private final File ConfigFile;
    private final File ConfigTemp;

    private StaticUsers() {
        super();
        cPlayer = Collections.synchronizedMap(new HashMap<>(1000));
        //ConfigDir = new File("d:/prg/orionmod/run/config/Orion");
        ConfigDir = new File(Loader.instance().getConfigDir() + "/Orion");
        ConfigFile = new File(String.format("%s/users.json", ConfigDir));
        ConfigTemp = new File(String.format("%s/users.json.tmp", ConfigDir));

        if (!ConfigDir.exists()) {
            ConfigDir.mkdir();
        }

        if (!ConfigFile.exists()) {
            SaveConfig();
        }

        LoadConfig();
    }

    public static StaticUsers getConfig() {
        if (ConfigUsers == null) {
            ConfigUsers = new StaticUsers();
        }
        return ConfigUsers;
    }
    
    public void SetPass(String player, String pass) {
        cPlayer.put(player, pass);
    }

    private void LoadConfig() {
        try {

            System.out.println("Loading Orion Users Configurations!");

            JsonReader reader = new JsonReader(new FileReader(ConfigFile));
            reader.beginObject();

            while (reader.hasNext()) {
                JsonToken tokenType = reader.peek();

                if (tokenType == JsonToken.NAME) {
                    String text = reader.nextName();
                    String value = reader.nextString();

                    cPlayer.put(text, value);
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

            System.out.println("Saving Orion Users Configurations!");

            JsonWriter writer = new JsonWriter(new FileWriter(ConfigTemp));
            Set sMap = cPlayer.entrySet();
            Iterator iMap = sMap.iterator();

            writer.beginObject();

            while (iMap.hasNext()) {
                Map.Entry mentry = (Map.Entry) iMap.next();
                String text = (String) mentry.getKey();
                String value = (String) mentry.getValue();

                writer.name(text);
                writer.value(value);
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
            System.out.println("Done Saving Orion Users Configurations!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
