/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 *
 * @author Daesmond
 */
public class OrionProtectListener {

    StaticProtected sp = StaticProtected.getConfig();

    private String PosToStr(BlockPos b) {
        return String.format("%s|%s|%s", b.getX(), b.getY(), b.getZ());
    }

    @SubscribeEvent
    public void eBreakBlock(BlockEvent.BreakEvent e) {
        String bpos = PosToStr(e.getPos());
        OrionProtectBlock opb = sp.isProtected(bpos);

        if (e.getWorld().isRemote) {
            return;
        }

        if (opb != null) {
            String byname = opb.ByName;
            String bname = e.getWorld().getBlockState(e.getPos()).getBlock().getLocalizedName();
            String pname = e.getPlayer().getName();

            if (pname.trim().equals(byname.trim())) {
                byname = "You";
            }

            e.setCanceled(true);
            e.getPlayer().sendMessage(new TextComponentTranslation(String.format("%s you cannot destroy %s because it is protected by %s!", pname, bname, byname)));
        }
    }

    @SubscribeEvent
    public void eProtectObjectTag(PlayerInteractEvent.RightClickBlock e) {
        World world = e.getWorld();

        if (world.isRemote) {
            return;
        }

        if (!(e.getEntity() instanceof EntityPlayer)) {
            return;
        }
        
        EntityPlayer player = e.getEntityPlayer();
        String pname = e.getEntity().getName();

        if (!sp.isProtection(pname)) {
            return;
        }

        BlockPos p = e.getPos();
        String bpos = PosToStr(e.getPos());
        Item i = player.getHeldItemMainhand().getItem();
        Block t;
        OrionProtectBlock opb = sp.isProtected(bpos);

        if (opb != null) {
            System.out.println(opb.axis);

            if (i.getUnlocalizedName().contains("item.sword") && e.getItemStack().getUnlocalizedName().equals(i.getUnlocalizedName())) {
                sp.Unprotect(world, p, pname);
                sp.setForUpdate();

                player.sendMessage(new TextComponentTranslation(String.format("%s %s Block x=%d  y=%d  z=%d is now unprotected\n", pname, opb.BlockName, p.getX(), p.getY(), p.getZ())));
                t = e.getWorld().getBlockState(p).getBlock();
                System.out.format("U Block=%s  Hardness=%1.2f  Resistance=%1.2f\n", t.getUnlocalizedName(), sp.getBlockHardness(t), sp.getBlockResistance(t));
            }
        } else {
            if (i.getUnlocalizedName().contains("item.pickaxe") && e.getItemStack().getUnlocalizedName().equals(i.getUnlocalizedName())) {
                sp.Protect(world, p, pname);
                opb = sp.isProtected(bpos);
                sp.setForUpdate();

                player.sendMessage(new TextComponentTranslation(String.format("%s %s Block x=%d  y=%d  z=%d is now protected\n", pname, opb.BlockName, p.getX(), p.getY(), p.getZ())));
                t = world.getBlockState(p).getBlock();
                System.out.format("P Block=%s  Hardness=%1.2f  Resistance=%1.2f\n", t.getUnlocalizedName(), sp.getBlockHardness(t), sp.getBlockResistance(t));
            }
        }
    }

    @SubscribeEvent
    public void onServerWorldTick(WorldTickEvent event) {
        if (event.phase == Phase.END && sp.IsUpdateNeeded()) {
            if (sp.getTicks() >= 1200) {
                sp.setNotForUpdate();
                sp.resetTicks();
                sp.SaveConfig();
            } else {
                sp.incTicks();
            }
        }
    }

    @SubscribeEvent
    public void eBoomEvent(ExplosionEvent.Detonate event) {
        boolean isCancel = false;
        Set<BlockPos> obombs = Sets.<BlockPos>newHashSet();

        for (BlockPos bp : event.getAffectedBlocks()) {
            Object o = (Object) sp.isProtected(PosToStr(bp));
            Block blk = event.getWorld().getBlockState(bp).getBlock();

            if (o != null) {
                isCancel = true;
            }

            if (blk.getUnlocalizedName().contains("tile.tnt")) {
                obombs.add(bp);
            }
        }

        if (isCancel) {
            event.getAffectedBlocks().clear();
            event.getAffectedBlocks().addAll(obombs);
        }
    }
}
