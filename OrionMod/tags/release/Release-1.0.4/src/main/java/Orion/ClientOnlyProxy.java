/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 *
 * @author Daesmond
 */
public class ClientOnlyProxy extends CommonProxy {

    public void preInit() {
        super.preInit();
        // minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.preInitClientOnly();
    }

    public void init() {
        super.init();
        // minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.initClientOnly();
    }

    public void postInit() {
        super.postInit();
        // minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.postInitClientOnly();
    }

    @Override
    public boolean playerIsInCreativeMode(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
            return entityPlayerMP.interactionManager.isCreative();
        } else if (player instanceof EntityPlayerSP) {
            return Minecraft.getMinecraft().playerController.isInCreativeMode();
        }
        return false;
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }

}
