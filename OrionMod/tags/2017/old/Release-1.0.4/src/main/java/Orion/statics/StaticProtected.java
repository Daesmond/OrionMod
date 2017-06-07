/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.statics;

import Orion.OrionMain;
import Orion.struct.OrionProtectBlock;
import Orion.OrionReflection;
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
public class StaticProtected {

    private static StaticProtected ConfigProtected;
    private final Map<String, OrionProtectBlock> cMap;
    private final Map<String, Boolean> cPlayer;
    private final File ConfigDir;
    private final File ConfigFile;
    private final File ConfigTemp;
    private int ticks;
    private boolean isupdateneeded;
    private boolean isupdating;

    private StaticProtected() {
        cMap = Collections.synchronizedMap(new HashMap<>(1000000));
        cPlayer = Collections.synchronizedMap(new HashMap<>(1000));
        //ConfigDir = new File("d:/prg/orionmod/run/config/Orion");
        ConfigDir = new File(Loader.instance().getConfigDir() + "/Orion");
        ConfigFile = new File(String.format("%s/Protected.json", ConfigDir));
        ConfigTemp = new File(String.format("%s/Protected.json.tmp", ConfigDir));
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

    public static StaticProtected getConfig() {
        if (ConfigProtected == null) {
            ConfigProtected = new StaticProtected();
        }
        return ConfigProtected;
    }

    public void setBlockResistance(Block blk, float v) {
        OrionReflection.setFloatField(blk.getClass(), "field_149781_w", v);
    }

    public void setBlockHardness(Block blk, float v) {
        OrionReflection.setFloatField(blk.getClass(), "field_149782_v", v);
    }

    public float getBlockResistance(Block blk) {
        return OrionReflection.getFloatField(blk, "field_149781_w");
    }

    public float getBlockHardness(Block blk) {
        return OrionReflection.getFloatField(blk, "field_149782_v");
    }

    public OrionProtectBlock isProtected(String pos) {
        Object o = cMap.get(pos);
        OrionProtectBlock res = null;

        try {
            res = (o == null) ? null : (OrionProtectBlock) o;
        } catch (Exception ex) {
            res = null;
        }

        return res;
    }

    public void SetProtectBlock(World world, BlockPos bp) {
//        IBlockState ibs = world.getBlockState(bp);
//        IBlockState ibs2 = ibs;
//
//        try {
//            Field field = OrionReflection.getField(ibs.getClass(), "field_177239_a");
//            Block test = Blocks.BEDROCK;
//            IBlockState oibs = ibs.getBlock().getExtendedState(ibs, world, bp);
//
//            field.setAccessible(true);
//            field.set(ibs, test);
//            world.setBlockState(bp, ibs2, 3);
//            world.setBlockState(bp, oibs, 3);
//            field.setAccessible(false);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public void UnsetProtectBlock(World world, BlockPos bp) {
//        IBlockState ibs = world.getBlockState(bp);
//
//        try {
//            Field field = OrionReflection.getField(ibs.getClass(), "field_177239_a");
//            OrionProtectBlock opb = (OrionProtectBlock) cMap.get(PosToStr(bp));
//            IBlockState oibs = opb.block.getDefaultState();
//            
//            field.setAccessible(true);
//            field.set(ibs, opb.block);
//            world.setBlockState(bp, oibs,3);
//            field.setAccessible(false);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
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
            opb.Hardness = getBlockHardness(blk);
            opb.Resistance = getBlockResistance(blk);

            cMap.put(axis, opb);

            //System.out.format("Before %s Set Block=%s  Hardness=%s  Resistance=%1.2f\n", axis, opb.BlockName, opb.Hardness, opb.Resistance);
            SetProtectBlock(world, bp);
            //blk = world.getBlockState(bp).getBlock();
            //System.out.format("After %s Set Block=%s  Hardness=%s  Resistance=%1.2f\n", axis, blk.getUnlocalizedName(), getBlockHardness(blk), getBlockResistance(blk));
        }
    }

    public void Unprotect(World world, BlockPos bp, String pname) {
        String axis = OrionMain.PosToStr(bp);
        Object o = cMap.get(axis);

        if (o != null) {
            OrionProtectBlock opb = (OrionProtectBlock) o;
            //Block blk = world.getBlockState(bp).getBlock();

            //System.out.format("Before %s Unset Block=%s  Hardness=%s  Resistance=%1.2f\n", axis, opb.BlockName, opb.Hardness, opb.Resistance);
            UnsetProtectBlock(world, opb.pos);
            opb.block = null;
            opb.pos = null;
            opb = null;
            cMap.remove(axis);
            //blk = world.getBlockState(bp).getBlock();
            //System.out.format("After %s Unset Block=%s  Hardness=%s  Resistance=%1.2f\n", axis, blk.getUnlocalizedName(), getBlockHardness(blk), getBlockResistance(blk));
        }
    }

    public void SetProtection(String pname) {
        cPlayer.put(pname, true);
    }

    public void UnsetProtection(String pname) {
        cPlayer.put(pname, false);
    }

    public Boolean isProtection(String pname) {
        Object o = cPlayer.get(pname);

        if (o == null) {
            UnsetProtection(pname);
        }

        return (Boolean) cPlayer.get(pname);
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

    private String AxisToStr(int x, int y, int z) {
        return String.format("%s|%s|%s", x, y, z);
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
                opb.Hardness = getBlockHardness(blk);
                opb.Resistance = getBlockResistance(blk);

                //System.out.format("Before Init Block=%s  Hardness=%s  Resistance=%1.2f\n", opb.BlockName, opb.Hardness, opb.Resistance);
                SetProtectBlock(world, bp);
                blk = world.getBlockState(bp).getBlock();
                //System.out.format("After Init Block=%s  Hardness=%s  Resistance=%1.2f\n", blk.getUnlocalizedName(), getBlockHardness(blk), getBlockResistance(blk));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isPointCollide(BlockPos a, BlockPos b) {
        int minX = 0, maxX = 0, minY = 0, maxY = 0, minZ = 0, maxZ = 0;

        minX = b.getX() - 16;
        maxX = b.getX() + 16;
        minY = b.getY() - 16;
        maxY = b.getY() + 16;
        minZ = b.getZ();
        maxZ = (b.getZ() >= 0) ? b.getZ() + 16 : b.getZ() - 16;

        return (a.getX() >= minX && a.getX() <= maxX)
                && (a.getY() >= minY && a.getY() <= maxY)
                && (a.getZ() >= minY && a.getZ() <= maxZ);
    }

    private boolean chkDir(int a, int b) {
        boolean ret = false;
        int tmp, c, d;

//        c = (a < 0) ? a * -1 : a;
//        d = (b < 0) ? b * -1 : a;
//        if (a < 0 && b < 0) {
        tmp = a - b;
        ret = (tmp < 0);
//        } else if (a<0 && b>0) {          
//        }

        return ret;
    }

    private int chkCount(int a, int b) {
        int ret = 0;
        int c, d;

        c = (a < 0) ? a * -1 : a;
        d = (b < 0) ? b * -1 : b;
        ret = (c > d) ? c - d : d - c;

        return ret;
    }

    public void Protection3D(World world, String pname, BlockPos a, BlockPos b) {
        boolean dirx = false, diry = false, dirz = false;
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
        boolean dirx = false, diry = false, dirz = false;
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