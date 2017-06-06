/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.statics.StaticOrion;
import Orion.statics.StaticProtected;
import Orion.statics.StaticUsers;
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

    public ServerProxy() {
        super();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        if (event.getSide().isClient()) {
            return;
        }

        System.out.format("Global Spawn Location >> %s\n", StaticOrion.getConfig().getDefaultSpawnPrintable());
        StaticUsers.getConfig();
        StaticProtected.getConfig();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);

        if (event.getSide().isClient()) {
            return;
        }

        World world = event.getServer().getEntityWorld();
        StaticProtected.getConfig().InitProtection(world);
        event.registerServerCommand(new CmdPass());
    }

    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);

        if (event.getSide().isClient()) {
            return;
        }

        StaticProtected.getConfig().SaveConfig();
        StaticOrion.getConfig().SaveConfig();
        StaticUsers.getConfig().SaveConfig();
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
