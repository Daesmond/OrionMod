/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.items;

import Orion.OrionItems;
import Orion.Proxy.ClientProxy;
import Orion.Proxy.OrionMessage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Daesmond
 */
public class ItemOrionKey extends ItemAbstract {

    public ItemOrionKey() {
        this.maxStackSize = 1;
        this.setCreativeTab(OrionItems.tab);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
//        ItemStack itemstack = playerIn.getHeldItem(handIn);
//        String test = String.format("onItemRightClick Unlocal: %s  Dname: %s", itemstack.getUnlocalizedName(), itemstack.getDisplayName());
//
//        DMesg(worldIn, test);
//
//        if (!worldIn.isRemote) {
//            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
//        }
//        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
//    }

//    @Override
//    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
//        ItemStack itemstack = player.getHeldItemMainhand();
//        String test = String.format("onLeftClickEntity Unlocal: %s  Dname: %s", itemstack.getUnlocalizedName(), itemstack.getDisplayName());
//
//        DMesg(player.getEntityWorld(), test);
//        return super.onLeftClickEntity(stack, player, entity);
//    }

//    @Override
//    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        ItemStack itemstack = player.getHeldItemMainhand();
//        Block t;
//        String pname = player.getName();
//        String bpos = OrionMain.PosToStr(pos);
//        BlockPos p = pos;
//        OrionProtectBlock opb;
//
//        String test = String.format("onItemUse Unlocal: %s  Dname: %s", itemstack.getUnlocalizedName(), itemstack.getDisplayName());
//
//        DMesg(player.getEntityWorld(), test);
//
//        if (worldIn.isRemote) {
//            return EnumActionResult.SUCCESS;
//        }
//        return EnumActionResult.SUCCESS;
//    }

//    @Override
//    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        ItemStack itemstack = player.getHeldItemMainhand();
//        //String test = String.format("onItemUseFirst Unlocal: %s  Dname: %s", itemstack.getUnlocalizedName(), itemstack.getDisplayName());
//        String un = "item.orionkey";
//        String dn = "Orion Key";
//
//        //DMesg(world, test);
//        if (itemstack.getUnlocalizedName().equals(un) && itemstack.getDisplayName().equals(dn)) {
//            //NBTTagCompound data = new NBTTagCompound(itemstack);            
//            //itemstack.setStackDisplayName(String.format("Orion key: %s's key", player.getName()));
//
//            String oKey = String.format("Orion %s's key", player.getName());
//
//            ClientProxy.network.sendToServer(new OrionMessage(String.format("ORIONKEY=>%s\r\n", oKey)));
//        }
//
//        //return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
//        return EnumActionResult.SUCCESS;
//    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ItemStack itemstack = playerIn.getHeldItemMainhand();
        World world = playerIn.getEntityWorld();
        String test;
        Block b;
        BlockPos bp;

        //itemstack.setStackDisplayName(String.format("%s's key", player.getName()));

        bp = this.toBlockPos(playerIn.getLookVec());
        b = world.getBlockState(bp).getBlock();
        
        test = String.format("itemInteractionForEntity Unlocal: %s  Dname: %s  to block=%s  Coord=%s", 
                itemstack.getUnlocalizedName(), itemstack.getDisplayName(), b.getUnlocalizedName(), this.BlockPosToStr(bp));
        DMesg(playerIn.getEntityWorld(), test);

        return true; //super.itemInteractionForEntity(stack, playerIn, target, hand); 
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        String test;
        World world = player.getEntityWorld();
        Block b;


        b = world.getBlockState(pos).getBlock();
        
        test = String.format("onBlockStartBreak Unlocal: %s  Dname: %s  to block=%s", 
                itemstack.getUnlocalizedName(), itemstack.getDisplayName(), b.getUnlocalizedName());

        DMesg(world, test);
        
        return super.onBlockStartBreak(itemstack, pos, player); 
    }

}
