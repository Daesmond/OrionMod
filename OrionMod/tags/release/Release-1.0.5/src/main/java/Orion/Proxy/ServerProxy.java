/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.statics.StaticOrion;
import Orion.statics.StaticProtected;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Daesmond
 */
@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {

    public static StaticOrion so;
    public static StaticProtected sp;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        so = StaticOrion.getConfig();
        sp = StaticProtected.getConfig();
        System.out.format("Global Spawn Location >> %s\n", so.getDefaultSpawnPrintable());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);

        World world = event.getServer().getEntityWorld();
        sp.InitProtection(world);
    }

    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);

        sp.SaveConfig();
        so.SaveConfig();
    }

    @Override
    public boolean playerIsInCreativeMode(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
            return entityPlayerMP.interactionManager.isCreative();
        }

        return false;
    }

    @Override
    public boolean isDedicatedServer() {
        return true;
    }
}
