/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.OrionBlockPos;
import Orion.OrionItems;
import Orion.OrionMain;
import Orion.struct.OrionProtectBlock;
import Orion.OrionReflection;
import Orion.Proxy.CommonProxy;
import Orion.Proxy.OrionMessage;
import Orion.items.ItemOrionKey;
import Orion.statics.StaticLock;
import Orion.statics.StaticOrion;
import Orion.statics.StaticProtected;
import Orion.statics.StaticUsers;
import Orion.struct.OrionLockBlock;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.Rotation;
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
        if (e.getWorld().isRemote) { // Client Side
            return;
        }

        // Server Side
        StaticProtected sp = StaticProtected.getConfig();
        String bpos = OrionMain.PosToStr(e.getPos());
        OrionProtectBlock opb = sp.getProtectedBlockInfo(bpos);

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
    public void onServerWorldTick(WorldTickEvent event) {
        if (event.side == Side.CLIENT) { // Client Sided
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

        StaticUsers su = StaticUsers.getConfig();

        if (event.phase == Phase.END && su.IsUpdateNeeded()) {
            if (su.getTicks() >= 80) {
                su.setNotForUpdate();
                su.resetTicks();
                su.SaveConfig();
            } else {
                su.incTicks();
            }
        }

        StaticLock sl = StaticLock.getConfig();

        if (event.phase == Phase.END && sl.IsUpdateNeeded()) {
            if (sl.getTicks() >= 80) {
                sl.setNotForUpdate();
                sl.resetTicks();
                sl.SaveConfig();
            } else {
                sl.incTicks();
            }
        }
    }

    @SubscribeEvent
    public void eBoomEvent(ExplosionEvent.Detonate event) {
        if (event.getWorld().isRemote) {
            return;
        }

        StaticProtected sp = StaticProtected.getConfig();
        boolean isCancel = false;
        Set<BlockPos> obombs = Sets.<BlockPos>newHashSet();

        for (BlockPos bp : event.getAffectedBlocks()) {
            Object o = (Object) sp.getProtectedBlockInfo(OrionMain.PosToStr(bp));
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
            if (!event.getEntityLiving().world.isRemote) {
                CreeperExplode((EntityCreeper) event.getEntityLiving());
            }
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
//            System.out.format("Item: %s\n", i.getUnlocalizedName());
//        }
    }

    @SubscribeEvent
    public void ePlayerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (!(e.getEntityPlayer() instanceof EntityPlayer)) {
            return;
        }

        if (e.getSide().isClient()) {
            return;
        }

        EntityPlayer p = e.getEntityPlayer();
        World world = e.getWorld();

        BlockPos bp = e.getPos();
        Block blk = world.getBlockState(bp).getBlock();
        String bname = blk.getUnlocalizedName().toLowerCase();
        String pname = p.getName();
        String pos = OrionMain.PosToStr(bp);
        StaticLock sl = StaticLock.getConfig();
        StaticProtected sp = StaticProtected.getConfig();

        if (bname.contains("chest") || bname.contains("door") || bname.contains("button")) {
            if (!(p.getHeldItemMainhand().getItem() instanceof ItemOrionKey)) {
                return;
            }

            OrionLockBlock olb = sl.getBlockInfo(pos);
            boolean isok = false;

            if (sp.isPosProtectedBy(pos, pname)) {
                if (olb == null) {
                    olb = new OrionLockBlock(pname, bname);
                    sl.Lock(pos, olb.getJsonLine());
                    p.sendMessage(new TextComponentTranslation(String.format("%s it is now locked!\n", pname)));
                } else {
                    sl.UnLock(pos);
                    p.sendMessage(new TextComponentTranslation(String.format("%s it is now unlocked!\n", pname)));
                }

                isok = true;
            } else if (sp.isPosProtected(pos)) {
                OrionProtectBlock opb = sp.getProtectedBlockInfo(pos);

                if (opb == null || opb.ByName == null) {
                    DMesg(world, String.format("Position: %s", pos));
                } else {
                    DMesg(world, String.format("Position: %s  Byname: %s", pos, opb.ByName));
                }

                if (olb == null) {
                    isok = false;
                    p.sendMessage(new TextComponentTranslation(String.format("%s you cannot lock it because it is protected by %s\n", pname, opb.ByName)));
                } else {
                    if (OrionItems.isOp(p)) { // Ops are only allowed to unlock but not lock what they don't own
                        isok = true;
                        sl.UnLock(pos);
                        p.sendMessage(new TextComponentTranslation(String.format("%s removing lock by %s\n", pname, olb.ByName)));
                    } else {
                        isok = false;
                        p.sendMessage(new TextComponentTranslation(String.format("%s you cannot unlock it because it is locked by %s\n", pname, olb.ByName)));
                    }
                }
            }

            if (isok) {
                sl.setForUpdate();
            }
        }
    }

    @SubscribeEvent
    public void ePlayerInteractEventRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        if (!(e.getEntityPlayer() instanceof EntityPlayer)) {
            return;
        }

        if (e.getSide().isClient()) {
            return;
        }

        EntityPlayer p = e.getEntityPlayer();
        World world = e.getWorld();
        BlockPos bp = e.getPos();
        Block blk = world.getBlockState(bp).getBlock();
        String bname = blk.getUnlocalizedName().toLowerCase();
        String pname = p.getName();
        String pos = OrionMain.PosToStr(bp);
        StaticLock sl = StaticLock.getConfig();

        if (bname.contains("button")) { // separate button and check neighbors in case player put another button to bypass lock!
            OrionLockBlock olb = sl.getBlockInfo(pos);
            String lockby = null;
            boolean oktoenter = false;
            int nullcnt = 0;
            int size = 0;

            if (olb == null) {
                OrionBlockPos obp = new OrionBlockPos(bp).CalcRadius(world);

                size = obp.getRadius().size();

                //System.out.println("=====================");
                for (Map.Entry<BlockPos, Block> entry : obp.getRadius().entrySet()) {
                    BlockPos mbp = entry.getKey();
                    Block mblk = entry.getValue();

                    olb = sl.getBlockInfo(OrionMain.PosToStr(mbp));

                    if (olb == null) {
                        nullcnt += 1;
                    }

                    if (mblk.getUnlocalizedName().contains("door")) {
//                        System.out.format("Position: %s   BlockName: %s  NCount: %d  TotalSize: %d  Owner: %s\r\n",
//                                OrionMain.PosToStr(mbp), mblk.getUnlocalizedName(), nullcnt, size, (olb==null) ? "none" : olb.ByName);

                        if (olb == null) {
                        } else if (olb.isLockedBy(pname)) {
                            oktoenter = true;
                            break;
                        } else {
                            if (lockby == null) {
                                lockby = olb.ByName;
                            }

                            if (OrionItems.isOp(p)) {
                                oktoenter = true;
                                p.sendMessage(new TextComponentTranslation(String.format("%s bypassed lock by %s\n", pname, olb.ByName)));
                                break;
                            }
                        }
                    }
                }

                if (!oktoenter || nullcnt != size) {
                    if (lockby == null) {
                        p.sendMessage(new TextComponentTranslation(String.format("%s you cannot bypass this by putting button. It is locked!\n", pname)));
                    } else {
                        p.sendMessage(new TextComponentTranslation(String.format("%s you cannot bypass this by putting button. It is locked by %s!\n", pname, lockby)));
                    }
                    e.setCanceled(true);
                }

                obp.getRadius().clear();
            } else if (olb.isLockedBy(pname)) {
            } else {
                if (OrionItems.isOp(p)) {
                    p.sendMessage(new TextComponentTranslation(String.format("%s bypassed lock by %s\n", pname, olb.ByName)));
                } else {
                    p.sendMessage(new TextComponentTranslation(String.format("%s you cannot open this. It is locked by %s\n", pname, olb.ByName)));
                    e.setCanceled(true);
                }
            }
        } else if (bname.contains("chest") || bname.contains("door")) {
            OrionLockBlock olb = sl.getBlockInfo(pos);

            if (olb == null) {
            } else if (olb.isLockedBy(pname)) {
            } else {
                if (OrionItems.isOp(p)) {
                    p.sendMessage(new TextComponentTranslation(String.format("%s bypassed lock by %s\n", pname, olb.ByName)));
                } else {
                    if (!bname.contains("dooriron")) {
                        p.sendMessage(new TextComponentTranslation(String.format("%s you cannot open this. It is locked by %s\n", pname, olb.ByName)));
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    public void DMesg(World world, String msg) {
        if (world.isRemote) { // client side
            CommonProxy.network.sendToServer(new OrionMessage(String.format("Client: %s\r\n", msg)));
        } else {
            System.out.format(String.format("Server: %s\r\n", msg));
        }
    }

//    @SubscribeEvent
//    public void eWatch(ChunkWatchEvent.Watch e) {
//        ChunkPos c = e.getChunk();
//     
//        EntityPlayer player = (EntityPlayer) e.getPlayer();
//        
//        BlockPos p = c.getBlock(0, 0, 0);
//        
//        if (player.getEntityWorld().getBlockState(p).getBlock().isBurning((IBlockAccess) world, p)) {
//            
//        }
//        
//    }
}
