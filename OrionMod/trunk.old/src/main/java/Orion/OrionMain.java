/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import Orion.Proxy.CommonProxy;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

/**
 *
 * @author Daesmond
 */
@Mod(modid = OrionMain.MODID, name = OrionMain.NAME, version = OrionMain.VERSION, dependencies = OrionMain.DEPENDENCIES, acceptedMinecraftVersions = OrionMain.MCVERSION)
public class OrionMain {

    @Mod.Instance(OrionMain.MODID)
    public static OrionMain instance;
    
    @SidedProxy(clientSide = "Orion.Proxy.ClientProxy", serverSide = "Orion.Proxy.ServerProxy")
    public static CommonProxy proxy;
    

    public static final String MODID = "orionmod";
    public static final String NAME = "Orion Mod";
    public static final String VERSION = "1.0.6";
    public static final String DEPENDENCIES = "required-after:forge@[13.19.0.2142,)";
    public static final String MCVERSION = "[1.10, 1.12)";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        proxy.onServerStarting(event);
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        proxy.onServerStopping(event);
    }

    public static String PosToStr(BlockPos b) {
        return String.format("%s|%s|%s", b.getX(), b.getY(), b.getZ());
    }

}
