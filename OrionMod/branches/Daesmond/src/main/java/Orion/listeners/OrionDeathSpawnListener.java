/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.OrionMain;
import Orion.gui.GuiPassword;
import Orion.statics.StaticOrion;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 *
 * @author Daesmond
 */
public class OrionDeathSpawnListener {

    ArrayList<String> DeadPlayers = new ArrayList<String>();
    public boolean isFirst = false;

    @SubscribeEvent
    public void ePlayerLoginOrion(PlayerEvent.PlayerLoggedInEvent e) {
        String pname;

        if (e.player.getServer().isSinglePlayer()) {
            return;
        }

        if (e.player.world.isRemote) {
            StaticOrion so = StaticOrion.getConfig();
            pname = e.player.getName();

            if (e.player.getScore() == 0) {
                if (so.getMySpawnPrintable(pname) == null || so.getMySpawnPrintable(pname).trim().isEmpty()) {
                    return;
                }

                System.out.format("%s joining and spawning to location >> %s\n", pname, so.getMySpawnPrintable(pname));

                try {
                    Integer.parseInt(so.getMySpawnAxis(pname, "X"));
                } catch (Exception ex) {
                    return;
                }

                int X = Integer.parseInt(so.getMySpawnAxis(pname, "X"));
                int Y = Integer.parseInt(so.getMySpawnAxis(pname, "Y"));
                int Z = Integer.parseInt(so.getMySpawnAxis(pname, "Z"));

                e.player.velocityChanged = true;
                e.player.setPositionAndUpdate(X, Y, Z);
                e.player.velocityChanged = false;
            }
        } else {

        }

    }

    @SubscribeEvent
    public void eDeathOrion(LivingDeathEvent e) {
        if (!e.getEntity().world.isRemote) {
            if (e.getEntity() instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) e.getEntity();

                DeadPlayers.add(p.getName());
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        EntityPlayer p = e.player;

        if (p.world != null) {
            if (e.player.world.isRemote) {
                if (p.getName().equals("Daesmond")) {
                    if (!isFirst) {
                        isFirst = true;
                        //p.openGui(OrionMain.instance, GuiPassword.getGuiID(), p.getEntityWorld(), (int) p.posX, (int) p.posY, (int) p.posZ);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void eSpawnOrion(EntityJoinWorldEvent e) {
        if (e == null) {
            return;
        }

        if (!(e.getEntity() instanceof EntityPlayerMP)) {
            return;
        }

        if (e.getEntity().world == null) {
            return;
        }

        if (e.getEntity().world.isRemote) {
            StaticOrion so = StaticOrion.getConfig();
            EntityPlayerMP p = (EntityPlayerMP) e.getEntity();
            String pname = p.getName();

            try {
                Integer.parseInt(so.getMySpawnAxis(pname, "X"));
            } catch (Exception ex) {
                return;
            }

            if (DeadPlayers.contains(pname)) {
                if (so.getMySpawnPrintable(pname) == null || so.getMySpawnPrintable(pname).isEmpty()) {
                    return;
                }

                System.out.format("%s is spawning to location >> %s\n", pname, so.getMySpawnPrintable(pname));

                int X = Integer.parseInt(so.getMySpawnAxis(pname, "X"));
                int Y = Integer.parseInt(so.getMySpawnAxis(pname, "Y"));
                int Z = Integer.parseInt(so.getMySpawnAxis(pname, "Z"));

                DeadPlayers.remove(pname);
                p.velocityChanged = true;
                p.connection.setPlayerLocation(X, Y, Z, 0, 0);
                p.velocityChanged = false;
            }
        }
    }
}
