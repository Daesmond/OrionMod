/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.statics.StaticOrion;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 *
 * @author Daesmond
 */
public class OrionDeathSpawnListener {

    ArrayList<String> DeadPlayers = new ArrayList<String>();
    StaticOrion so = StaticOrion.getConfig();

    @SubscribeEvent
    public void ePlayerLoginOrion(PlayerEvent.PlayerLoggedInEvent e) {
        String pname;

        if (!e.player.world.isRemote) {
            pname = e.player.getName();

            /*            
            System.out.format("Score=%d  Age=%d\n", e.player.getScore(), e.player.getAge());

            Collection<Score> sp = e.player.getWorldScoreboard().getScores();

            for (Score s : sp) {
                System.out.format("Player=%s  %s=>%d\n", s.getPlayerName(), s.getObjective().getName(), s.getScorePoints());
            }
             */
            if (e.player.getScore() == 0) {
                System.out.format("%s joining and spawning to location >> %s\n", pname, so.getDefaultSpawnPrintable());

                                
                int X = Integer.parseInt(so.getMySpawnAxis(pname, "X"));
                int Y = Integer.parseInt(so.getMySpawnAxis(pname, "Y"));
                int Z = Integer.parseInt(so.getMySpawnAxis(pname, "Z"));

                e.player.velocityChanged = true;
                //e.player.setLocationAndAngles(X, Y, Z, 0, 0);
                e.player.setPositionAndUpdate(X, Y, Z);
                e.player.velocityChanged = false;
            }
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
    public void eSpawnOrion(EntityJoinWorldEvent e) {
        if (e == null) {
            return;
        }

        if (!(e.getEntity() instanceof EntityPlayer)) {
            return;
        }

        if (e.getEntity().world == null) {
            return;
        }

        if (!e.getEntity().world.isRemote) {
            EntityPlayerMP p = (EntityPlayerMP) e.getEntity();
            String pname = p.getName();

            if (DeadPlayers.contains(pname)) {
                System.out.format("%s is spawning to location >> %s\n", pname, so.getDefaultSpawnPrintable());

                int X = Integer.parseInt(so.getMySpawnAxis(pname, "X"));
                int Y = Integer.parseInt(so.getMySpawnAxis(pname, "Y"));
                int Z = Integer.parseInt(so.getMySpawnAxis(pname, "Z"));

                DeadPlayers.remove(pname);
                p.velocityChanged = true;
                p.connection.setPlayerLocation(X, Y, Z, 0, 0);
                //p.setLocationAndAngles(X, Y, Z, 0, 0);                
                //p.setPositionAndUpdate(X, Y, Z);
                p.velocityChanged = false;
            }
        }
    }
}
