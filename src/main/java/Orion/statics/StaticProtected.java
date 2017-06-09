/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.statics;

import Orion.OrionMain;
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
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Daesmond
 */
public class StaticProtected extends StaticAbstract {

    private static StaticProtected ConfigProtected;
    private final Map<String, OrionProtectBlock> cMap;
    private final File ConfigDir;
    private final File ConfigFile;
    private final File ConfigTemp;

    private StaticProtected() {
        super();
        cMap = Collections.synchronizedMap(new HashMap<>(1000000));
        //ConfigDir = new File("d:/prg/orionmod/run/config/Orion");
        ConfigDir = new File(Loader.instance().getConfigDir() + "/Orion");
        ConfigFile = new File(String.format("%s/Protected.json", ConfigDir));
        ConfigTemp = new File(String.format("%s/Protected.json.tmp", ConfigDir));

        if (!ConfigDir.exists()) {
            ConfigDir.mkdir();
        }

        if (!ConfigFile.exists()) {
            SaveConfig();
        }

        LoadConfig();
    }

    public static StaticProtected getConfig() {
        if (ConfigProtected == null) {
            ConfigProtected = new StaticProtected();
        }
        return ConfigProtected;
    }

    public OrionProtectBlock getProtectedBlockInfo(String pos) {
        return (OrionProtectBlock)  cMap.get(pos);
    }
    
    public boolean isPosProtected(String pos) {
        return (cMap.get(pos) == null);
    }
    
    public boolean isPosProtectedBy(String pos, String pname) {
        OrionProtectBlock o = (OrionProtectBlock) cMap.get(pos);

        if (o == null) {
            return false;
        }

        return o.ByName.equals(pname);
    }    

    public void Protect(World world, BlockPos bp, String pname) {
        String axis = OrionMain.PosToStr(bp);
        Object o = cMap.get(axis);

        if (o == null) {
            OrionProtectBlock opb = new OrionProtectBlock();
            Block blk = world.getBlockState(bp).getBlock();

            opb.ByName = pname;
            opb.axis = axis;
            opb.pos = bp;
            opb.block = blk;
            opb.BlockName = blk.getUnlocalizedName().trim();

            cMap.put(axis, opb);
        }
    }

    public void Unprotect(World world, BlockPos bp, String pname) {
        String axis = OrionMain.PosToStr(bp);
        Object o = cMap.get(axis);

        if (o != null) {
            OrionProtectBlock opb = (OrionProtectBlock) o;

            opb.block = null;
            opb.pos = null;
            opb = null;
            cMap.remove(axis);
        }
    }

    private void LoadConfig() {
        try {
            System.out.println("Loading Orion Protected Configurations!");

            JsonReader reader = new JsonReader(new FileReader(ConfigFile));
            reader.beginObject();

            while (reader.hasNext()) {
                JsonToken tokenType = reader.peek();

                if (tokenType == JsonToken.NAME) {
                    String text = reader.nextName();
                    String value = reader.nextString();
                    OrionProtectBlock opb = new OrionProtectBlock();
                    String[] axis = text.split("\\|");

                    opb.ByName = value;
                    opb.axis = text;
                    opb.pos = new BlockPos(Integer.parseInt(axis[0]), Integer.parseInt(axis[1]), Integer.parseInt(axis[2]));
                    cMap.put(text, opb);
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

            System.out.println("Saving Orion Protected Configurations!");

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
            System.out.println("Done Saving Orion Protected Configurations!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void InitProtection(World world) {
        try {
            Set sMap = cMap.entrySet();
            Iterator iMap = sMap.iterator();

            while (iMap.hasNext()) {
                Map.Entry mentry = (Map.Entry) iMap.next();
                OrionProtectBlock opb = (OrionProtectBlock) mentry.getValue();

                BlockPos bp = opb.pos;
                Block blk = world.getBlockState(bp).getBlock();

                opb.block = blk;
                opb.BlockName = blk.getUnlocalizedName().trim();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean chkDir(int a, int b) {
        return ((a - b) < 0);
    }

    public void Protection3D(World world, String pname, BlockPos a, BlockPos b) {
        boolean dirx, diry, dirz;
        int resx = 0, resy = 0, resz = 0;

        dirx = chkDir(a.getX(), b.getX());
        diry = chkDir(a.getY(), b.getY());
        dirz = chkDir(a.getZ(), b.getZ());
        resx = a.getX();

        while (true) {
            resy = a.getY();

            while (true) {
                resz = a.getZ();

                while (true) {
                    BlockPos bp = new BlockPos(resx, resy, resz);
                    Block blk = world.getBlockState(bp).getBlock();

                    if ((blk != Blocks.AIR) && (blk != Blocks.LAVA) && (blk != Blocks.LEAVES) && (blk != Blocks.LEAVES2) && (blk != Blocks.WATER)) {
                        this.Protect(world, bp, pname);
                    }

                    if (resz == b.getZ()) {
                        break;
                    }

                    if (dirz) {
                        resz++;
                    } else {
                        resz--;
                    }
                }

                if (resy == b.getY()) {
                    break;
                }

                if (diry) {
                    resy++;
                } else {
                    resy--;
                }
            }

            if (resx == b.getX()) {
                break;
            }

            if (dirx) {
                resx++;
            } else {
                resx--;
            }
        }

        System.out.format("Protected Coordinates: %s-%s\n", OrionMain.PosToStr(a), AxisToStr(resx, resy, resz));
        this.setForUpdate();
    }

    public void UnProtection3D(World world, String pname, BlockPos a, BlockPos b) {
        boolean dirx, diry, dirz;
        int resx = 0, resy = 0, resz = 0;

        dirx = chkDir(a.getX(), b.getX());
        diry = chkDir(a.getY(), b.getY());
        dirz = chkDir(a.getZ(), b.getZ());
        resx = a.getX();

        while (true) {
            resy = a.getY();

            while (true) {
                resz = a.getZ();

                while (true) {
                    BlockPos bp = new BlockPos(resx, resy, resz);

                    this.Unprotect(world, bp, pname);

                    if (resz == b.getZ()) {
                        break;
                    }

                    if (dirz) {
                        resz++;
                    } else {
                        resz--;
                    }
                }

                if (resy == b.getY()) {
                    break;
                }

                if (diry) {
                    resy++;
                } else {
                    resy--;
                }
            }

            if (resx == b.getX()) {
                break;
            }

            if (dirx) {
                resx++;
            } else {
                resx--;
            }
        }

        System.out.format("UnProtected Coordinates: %s-%s\n", OrionMain.PosToStr(a), AxisToStr(resx, resy, resz));
        this.setForUpdate();
    }

}
