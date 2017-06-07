/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 *
 * @author Daesmond
 */
public abstract class CommonProxy {

    public void preInit() {
        // minecraftbyexample.mbe31_inventory_furnace.StartupCommon.preInitCommon();
        NetworkRegistry.INSTANCE.registerGuiHandler(OrionMain.instance, GuiHandlerRegistry.getInstance());
        GuiHandlerRegistry.getInstance().registerGuiHandler(new GuiHandler(), GuiHandler.getGuiID());
    }

    public void init() {
        // minecraftbyexample.mbe31_inventory_furnace.StartupCommon.initCommon();
    }

    public void postInit() {
        // minecraftbyexample.mbe31_inventory_furnace.StartupCommon.postInitCommon();
    }

    abstract public boolean playerIsInCreativeMode(EntityPlayer player);

    abstract public boolean isDedicatedServer();
}
