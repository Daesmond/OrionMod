/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.gui.GuiHandler;
import Orion.gui.GuiHandlerRegistry;
import Orion.OrionItems;
import Orion.OrionMain;
import Orion.OrionRecipes;
import Orion.listeners.OrionChatListener;
import Orion.listeners.OrionDeathSpawnListener;
import Orion.listeners.OrionLevitateListener;
import Orion.listeners.OrionProtectListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Daesmond
 */
public abstract class CommonProxy {

    public static SimpleNetworkWrapper network;

    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(OrionMain.instance, GuiHandlerRegistry.getInstance());
        GuiHandlerRegistry.getInstance().registerGuiHandler(new GuiHandler(), GuiHandler.getGuiID());
        CommonProxy.network = NetworkRegistry.INSTANCE.newSimpleChannel(OrionMain.MODID);
        OrionItems.load();
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new OrionChatListener());
        MinecraftForge.EVENT_BUS.register(new OrionDeathSpawnListener());
        MinecraftForge.EVENT_BUS.register(new OrionProtectListener());
        MinecraftForge.EVENT_BUS.register(new OrionLevitateListener());
        OrionRecipes.loadDefaultRecipes(-1);
    }

    public void onServerStarting(FMLServerStartingEvent event) {
    }

    public void onServerStopping(FMLServerStoppingEvent event) {

    }

    abstract public boolean playerIsInCreativeMode(EntityPlayer player);

    abstract public boolean isDedicatedServer();
}
