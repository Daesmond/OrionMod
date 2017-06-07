/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraft.client.main.Main;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Daesmond
 */
@Mod(modid = OrionMain.MODID, name = OrionMain.NAME, version = OrionMain.VERSION, dependencies = OrionMain.DEPENDENCIES, acceptedMinecraftVersions = OrionMain.MCVERSION)
public class OrionMain {

    @Mod.Instance("orionmod")
    public static OrionMain instance;

    public static final String MODID = "orionmode";
    public static final String NAME = "Orion Mod";
    public static final String VERSION = "1.0.3";
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new OrionChatListener());
        MinecraftForge.EVENT_BUS.register(new OrionDeathSpawnListener());
        MinecraftForge.EVENT_BUS.register(new OrionProtectListener());
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {

        World world = event.getServer().getEntityWorld();

        sp.InitProtection(world);

        if (event.getSide() == Side.CLIENT) {

        }
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        sp.SaveConfig();
        so.SaveConfig();
    }
}
