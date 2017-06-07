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

    public CommonProxy() {
    }

    public static void Sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(OrionMain.instance, GuiHandlerRegistry.getInstance());
        GuiHandlerRegistry.getInstance().registerGuiHandler(new GuiHandler(), GuiHandler.getGuiID());
        CommonProxy.network = NetworkRegistry.INSTANCE.newSimpleChannel(OrionMain.MODID);

        if (event.getSide().isServer()) {
            CommonProxy.network.registerMessage(OrionMessageHandler.class, OrionMessage.class, 0, Side.SERVER);
        } else {
            CommonProxy.network.registerMessage(OrionMessageHandler.class, OrionMessage.class, 0, Side.CLIENT);
        }

        OrionItems.load();
    }

    public void init(FMLInitializationEvent event) {       
        MinecraftForge.EVENT_BUS.register(new OrionChatListener());
        MinecraftForge.EVENT_BUS.register(new OrionLevitateListener());
        MinecraftForge.EVENT_BUS.register(new OrionProtectListener());
        MinecraftForge.EVENT_BUS.register(new OrionDeathSpawnListener());
        OrionRecipes.loadDefaultRecipes(-1);
    }

    public void onServerStarting(FMLServerStartingEvent event) {
    }

    public void onServerStopping(FMLServerStoppingEvent event) {

    }

    public static String MD5(String md5) {
        try {
            String pass = String.format("ORION%s2017", md5);
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(pass.getBytes());
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    abstract public boolean playerIsInCreativeMode(EntityPlayer player);

    abstract public boolean isDedicatedServer();
}
