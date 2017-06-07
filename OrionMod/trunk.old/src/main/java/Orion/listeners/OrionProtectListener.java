/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.OrionMain;
import Orion.struct.OrionProtectBlock;
import Orion.OrionReflection;
import Orion.statics.StaticOrion;
import Orion.statics.StaticProtected;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Daesmond
 */
public class OrionProtectListener {

    @SubscribeEvent
    public void eBreakBlock(BlockEvent.BreakEvent e) {
        if (e.getWorld().isRemote) {
            return;
        }

        StaticProtected sp = StaticProtected.getConfig();

        String bpos = OrionMain.PosToStr(e.getPos());
        OrionProtectBlock opb = sp.isProtected(bpos);

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

        StaticProtected sp = StaticProtected.getConfig();
        EntityPlayer player = e.getEntityPlayer();
        String pname = e.getEntity().getName();

        if (!sp.isProtection(pname)) {
            return;
        }

        BlockPos p = e.getPos();
        String bpos = OrionMain.PosToStr(e.getPos());
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
        if (event.side == Side.CLIENT) {
            return;
        }

        StaticProtected sp = StaticProtected.getConfig();

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
        StaticProtected sp = StaticProtected.getConfig();
        boolean isCancel = false;
        Set<BlockPos> obombs = Sets.<BlockPos>newHashSet();

        for (BlockPos bp : event.getAffectedBlocks()) {
            Object o = (Object) sp.isProtected(OrionMain.PosToStr(bp));
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

    @SubscribeEvent
    public void eLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityCreeper) {
            CreeperExplode((EntityCreeper) event.getEntityLiving());
        }
    }

    private void CreeperExplode(EntityCreeper creeper) {
        StaticOrion so = StaticOrion.getConfig();
        
        if (creeper.getCreeperState() == 1) {
            int ignite = OrionReflection.getIntField(creeper, "field_70833_d");
            int fuse = OrionReflection.getIntField(creeper, "field_82225_f");

            if (ignite >= fuse - 1) {

                if (!so.AllowCreeperToExplode()) {
                    creeper.setDead();

                    if (!creeper.getEntityWorld().isRemote) {
                        creeper.getEntityWorld().playSound((EntityPlayer) null, creeper.posX, creeper.posY, creeper.posZ, SoundEvents.ENTITY_FIREWORK_TWINKLE,
                                SoundCategory.BLOCKS, 0.5f, (1.0f + (creeper.getEntityWorld().rand.nextFloat() - creeper.getEntityWorld().rand.nextFloat()) * 0.2f) * 0.7f);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void ePlaceBlock(BlockEvent.PlaceEvent e) {
//        BlockPos bp = e.getPos();
//        ItemStack i = e.getItemInHand();
//        EntityPlayer p = e.getPlayer();
//        String pname = e.getPlayer().getName();
//        World w = e.getWorld();
//
//        if (sp.isProtection(pname)) {
//
//            if (i.getUnlocalizedName().equals("tile.stone.stone")) {
//                //if (w.isRemote) {
//                //new GuiTutorial();
//                System.out.format("GUI!!!\n");
//                p.openGui(OrionMain.instance, GuiHandler.getGuiID(), w, p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ());
//                //o}
//
//            }
//            
//            e.setCanceled(true);
//            w.updateEntity(p);
//            System.out.format("Item: %s\n", i.getUnlocalizedName());
//        }
    }

    @SubscribeEvent
    public void ePlayer(PlayerInteractEvent.LeftClickBlock e) {
        EntityPlayer p = e.getEntityPlayer();
        BlockPos bp = e.getPos();
        World w = e.getWorld();
        Block blk = w.getBlockState(bp).getBlock();

        if (p == null) {
            return;
        }

        if (blk.getUnlocalizedName().toLowerCase().contains("chest")) {
            System.out.format("Block: %s\n", blk.getLocalizedName());
        }
    }

}
