package test;

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
import net.minecraft.client.Minecraft;

/**
 *
 * @author Daesmond
 */
public class test1 {

    private final Map<String, String> cMap;
    private final File ConfigDir;
    private final File ConfigFile;
    private final File ConfigTemp;

    public test1() {

        cMap = Collections.synchronizedMap(new HashMap<>(10000));
        ConfigDir = new File("f:/games/mcsf/config/Orion");
        //ConfigDir = new File(Loader.instance().getConfigDir() + "/Orion");
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

    public static void main(String[] args) {
        test1 t = new test1();
        OrionLockBlock olb1 = new OrionLockBlock();
        
        System.out.println((String)t.cMap.get("0|0|0"));

        olb1.ByName = "Daesmond";
        olb1.ItemName = "item.orionkey";
        olb1.isLocked = "1";
        olb1.axis = "0|0|1";

        t.cMap.put(olb1.axis, olb1.getJsonLine());

        t.SaveConfig();

        Minecraft.getMinecraft().shutdown();
    }

}
