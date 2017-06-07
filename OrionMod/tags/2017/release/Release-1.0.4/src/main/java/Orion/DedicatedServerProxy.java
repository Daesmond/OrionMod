/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 *
 * @author Daesmond
 */
public class DedicatedServerProxy extends CommonProxy {

    public void preInit() {
        super.preInit();
    }

    public void init() {
        super.init();
    }

    public void postInit() {
        super.postInit();
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
