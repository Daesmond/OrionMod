/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.GuiHandler;
import Orion.OrionItems;
import Orion.OrionMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Daesmond
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        OrionItems.registerClient();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        System.out.println("Client Side Registry Ito");
        NetworkRegistry.INSTANCE.registerGuiHandler(OrionMain.instance, new GuiHandler());
    }

    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);
    }

    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
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
