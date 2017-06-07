/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import Orion.statics.StaticProtected;
import Orion.statics.StaticOrion;
import Orion.listeners.OrionProtectListener;
import Orion.listeners.OrionChatListener;
import Orion.listeners.OrionDeathSpawnListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Daesmond
 */
@Mod(modid = OrionMain.MODID, name = OrionMain.NAME, version = OrionMain.VERSION, dependencies = OrionMain.DEPENDENCIES, acceptedMinecraftVersions = OrionMain.MCVERSION)
public class OrionMain {

    @Mod.Instance("orionmod")
    public static OrionMain instance;

    public static final String MODID = "orionmod";
    public static final String NAME = "Orion Mod";
    public static final String VERSION = "1.0.4";
    public static final String DEPENDENCIES = "required-after:forge@[13.19.0.2142,)";
    public static final String MCVERSION = "[1.10, 1.12)";
    public static StaticOrion so;
    public static StaticProtected sp;

    @Mod.EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
        try {           
            if (event.getSide() == Side.SERVER) {
                so = StaticOrion.getConfig();
                sp = StaticProtected.getConfig();
                System.out.format("Global Spawn Location >> %s\n", so.getDefaultSpawnPrintable());
            }

            OrionItems.load();

            if (event.getSide() == Side.CLIENT) {
                OrionItems.registerClient();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new OrionChatListener());
        MinecraftForge.EVENT_BUS.register(new OrionDeathSpawnListener());
        MinecraftForge.EVENT_BUS.register(new OrionProtectListener());

        if (event.getSide() == Side.CLIENT) {
            System.out.println("Client Side Registry Ito");
            NetworkRegistry.INSTANCE.registerGuiHandler(OrionMain.instance, new GuiHandler());
        }

        OrionRecipes.loadDefaultRecipes(-1);
    }

    @Mod.EventHandler
    public void started(FMLServerStartedEvent event) {
        //OrionRecipes.loadDefaultRecipes(-1);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        World world = event.getServer().getEntityWorld();

        if (world.isRemote) {
            sp.InitProtection(world);
        }
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        if (event.getSide() == Side.SERVER) {
            sp.SaveConfig();
            so.SaveConfig();
        }
    }

    public static String PosToStr(BlockPos b) {
        return String.format("%s|%s|%s", b.getX(), b.getY(), b.getZ());
    }

}
