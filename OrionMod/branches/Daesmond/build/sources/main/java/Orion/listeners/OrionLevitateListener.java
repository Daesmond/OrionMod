/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.OrionItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author Daesmond
 */
public class OrionLevitateListener {

    @SubscribeEvent
    public void eFall(LivingFallEvent event) {
        EntityPlayer player = null;

        if (event.getEntity().getEntityWorld().isRemote) {
            return;
        }

        if (event.getEntity() instanceof EntityPlayer) {
            player = (EntityPlayer) event.getEntity();

            if (!OrionItems.isOp(player)) {
                return;
            }

            handleFlight(player);

            if (player.getHeldItemOffhand().getItem().getUnlocalizedName().equals(OrionItems.PearlOrb.getUnlocalizedName())) {
                event.setDamageMultiplier(0);
                event.setDistance(0.0f);
                event.setCanceled(true);
            } else {
                event.setCanceled(false);
            }
        }
    }

    @SubscribeEvent
    public void eLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        EntityPlayer player = null;

        if (event.getEntityLiving() == null || !(event.getEntityLiving() instanceof EntityPlayer) || event.isCanceled()) {
            return;
        }

        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }

        player = (EntityPlayer) event.getEntityLiving();

        if (!player.getEntityWorld().isRemote) {
            if (OrionItems.isOp(player)) {
                handleFlight(player);
            }
        }
    }

    private void handleFlight(EntityPlayer player) {
        if (player.getHeldItemOffhand().getItem().getUnlocalizedName().equals(OrionItems.PearlOrb.getUnlocalizedName())) {
            player.capabilities.allowFlying = true;
            player.capabilities.disableDamage = true;
        } else {
            player.capabilities.allowFlying = false;
            player.capabilities.disableDamage = false;
        }

        player.sendPlayerAbilities();
        player.onLivingUpdate();
    }
}
