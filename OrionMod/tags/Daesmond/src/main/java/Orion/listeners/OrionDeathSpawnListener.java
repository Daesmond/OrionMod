/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.Proxy.OrionMessage;
import Orion.Proxy.ServerProxy;
import Orion.statics.StaticOrion;
import Orion.statics.StaticUsers;
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

    private ArrayList<String> DeadPlayers = new ArrayList<String>();

    @SubscribeEvent
    public void ePlayerLoginOrion(PlayerEvent.PlayerLoggedInEvent e) {

        if (e.player.getServer().isSinglePlayer()) {
            return;
        }

        if (e.player.world.isRemote) { // Client side
            return;
        }

        // Server Side
        StaticOrion so = StaticOrion.getConfig();
        EntityPlayerMP p = (EntityPlayerMP) e.player;
        String pname = p.getName();

        if (p.getScore() == 0) {
            String spawn = so.getMySpawnPrintable(pname);

            if (spawn == null || spawn.trim().isEmpty()) {
                return;
            }

            System.out.format("Login: %s joining and spawning to location => %s\r\n", pname, so.getMySpawnPrintable(pname));

            try {
                Integer.parseInt(so.getMySpawnAxis(pname, "X"));
            } catch (Exception ex) {
                return;
            }

            int X = Integer.parseInt(so.getMySpawnAxis(pname, "X"));
            int Y = Integer.parseInt(so.getMySpawnAxis(pname, "Y"));
            int Z = Integer.parseInt(so.getMySpawnAxis(pname, "Z"));

            p.velocityChanged = true;
            p.connection.setPlayerLocation(X, Y, Z, 0, 0);
            p.velocityChanged = false;
        }

        if (StaticUsers.getConfig().withPassword(pname)) {
            p.capabilities.disableDamage = true;
            System.out.format("ENTER PASSWORD FOR %s\r\n", pname);
            ServerProxy.network.sendTo(new OrionMessage("ENTERPASS"), (EntityPlayerMP) e.player);
        }
    }

    @SubscribeEvent
    public void eDeathOrion(LivingDeathEvent e) {
        if (!e.getEntity().world.isRemote) { // Server Side
            if (e.getEntity() instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) e.getEntity();

                DeadPlayers.add(p.getName());
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

        if (e.getEntity().world.isRemote) { // Client Side
            return;
        }

        StaticOrion so = StaticOrion.getConfig();
        EntityPlayerMP p = (EntityPlayerMP) e.getEntity();
        String pname = p.getName();

        try {
            Integer.parseInt(so.getMySpawnAxis(pname, "X"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if (DeadPlayers.contains(pname)) {
            String spawn = so.getMySpawnPrintable(pname);

            if (spawn == null || spawn.trim().isEmpty()) {
                return;
            }

            System.out.format("Dead Joining: %s is spawning to location => %s\r\n", pname, so.getMySpawnPrintable(pname));

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
