/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.statics.StaticOrion;
import Orion.statics.StaticProtected;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 *
 * @author Daesmond
 */
public class OrionChatListener {

    private BlockPos pPos = new BlockPos(0, 0, 0);
    private StaticOrion so = StaticOrion.getConfig();
    private StaticProtected sp = StaticProtected.getConfig();

    public OrionChatListener() {
    }

    @SubscribeEvent
    public void eServerChatEvent(ServerChatEvent event) {
        //if (event.getMessage().contains("potato")) {
        //	event.getPlayer().inventory.addItemStackToInventory(new ItemStack(
        //			Items.POTATO, 64));
        //event.getWorld().setBlockToAir(new BlockPos(event.getPos().getX(), event.getPos().getY(),event.getPos().getZ()));

        String msg = event.getMessage().toLowerCase();
        EntityPlayerMP player = event.getPlayer();
        String pname = player.getName();
        World world = player.getEntityWorld();

        if (msg.contains(("setglobalspawn"))) {
            setPPos(event.getPlayer().getPosition());
            so.setDefaultSpawnAxis("X", String.format("%s", pPos.getX()));
            so.setDefaultSpawnAxis("Y", String.format("%s", pPos.getY()));
            so.setDefaultSpawnAxis("Z", String.format("%s", pPos.getZ()));
            so.setForUpdate();
            player.sendMessage(new TextComponentTranslation(String.format("%s global spawn is set to%s\n", pname, so.getDefaultSpawnPrintable())));
        } else if (msg.contains("setmyspawn")) {
            setPPos(event.getPlayer().getPosition());
            so.setMySpawnAxis(pname, "X", String.format("%s", pPos.getX()));
            so.setMySpawnAxis(pname, "Y", String.format("%s", pPos.getY()));
            so.setMySpawnAxis(pname, "Z", String.format("%s", pPos.getZ()));
            so.setForUpdate();
            player.sendMessage(new TextComponentTranslation(String.format("%s your spawn is set to %s\n", pname, so.getMySpawnPrintable(pname))));
        } else if (msg.contains("protection save")) {
            sp.setNotForUpdate();
            sp.resetTicks();
            sp.SaveConfig();
            player.sendMessage(new TextComponentTranslation(String.format("%s protection file is now saved!\n", pname)));
        } else if (msg.contains("protection on")) {
            sp.SetProtection(player.getName());
            player.sendMessage(new TextComponentTranslation(String.format("%s protection is now set on!\n", pname)));
        } else if (msg.contains("protection off")) {
            sp.UnsetProtection(player.getName());
            player.sendMessage(new TextComponentTranslation(String.format("%s protection is now set off!\n", pname)));
        } else if (msg.startsWith("protect coord ")) {
            String s = msg.replace("protect coord ", "");
            String[] s2 = s.split(" ");
            String[] s3 = s2[0].split("\\,");
            String[] s4 = s2[1].split("\\,");
            BlockPos a;
            BlockPos b;

            a = new BlockPos(Integer.parseInt(s3[0]), Integer.parseInt(s3[1]), Integer.parseInt(s3[2]));
            b = new BlockPos(Integer.parseInt(s4[0]), Integer.parseInt(s4[1]), Integer.parseInt(s4[2]));
            sp.Protection3D(player.getServerWorld(), pname, a, b);
            player.sendMessage(new TextComponentTranslation(String.format("%s protection is now set on block coordinates!\n", pname)));
        } else if (msg.startsWith("unprotect coord ")) {
            String s = msg.replace("unprotect coord ", "");
            String[] s2 = s.split(" ");
            String[] s3 = s2[0].split("\\,");
            String[] s4 = s2[1].split("\\,");
            BlockPos a; 
            BlockPos b;

            a = new BlockPos(Integer.parseInt(s3[0]), Integer.parseInt(s3[1]), Integer.parseInt(s3[2]));
            b = new BlockPos(Integer.parseInt(s4[0]), Integer.parseInt(s4[1]), Integer.parseInt(s4[2]));
            sp.UnProtection3D(player.getServerWorld(), pname, a, b);
            player.sendMessage(new TextComponentTranslation(String.format("%s protection is now unset on block coordinates!\n", pname)));
        } else if (msg.contains("test me")) {
            System.out.format("Size=%d  minx=%1.2f  maxx=%1.2f  minz=%1.2f  maxz=%1.2f centerx"
                    + "=%1.2f centerz=%1.2f height=%d\n", world.getWorldBorder().getSize(),
                    world.getWorldBorder().minX(),
                    world.getWorldBorder().maxX(),
                    world.getWorldBorder().minZ(),
                    world.getWorldBorder().maxZ(),
                    world.getWorldBorder().getCenterX(),
                    world.getWorldBorder().getCenterZ(),
                    world.getHeight());
        } else if (msg.contains("test mo")) {

        }
    }

    private void setPPos(BlockPos mypos) {
        pPos = new BlockPos(mypos);
    }

    @SubscribeEvent
    public void onServerWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && so.IsUpdateNeeded()) {
            if (so.getTicks() >= 1200) {
                so.setNotForUpdate();
                so.resetTicks();
                so.SaveConfig();
            } else {
                so.incTicks();
            }
        }
    }

}
