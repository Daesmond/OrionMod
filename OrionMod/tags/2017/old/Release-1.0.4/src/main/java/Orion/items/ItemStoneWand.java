/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.items;

import Orion.CommonProxy;
import Orion.OrionMain;
import Orion.statics.StaticProtected;
import Orion.struct.OrionProtectBlock;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 *
 * @author Daesmond
 */
public class ItemStoneWand extends Item {

    StaticProtected sp = StaticProtected.getConfig();

    public ItemStoneWand() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.INVENTORY);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK; //super.getItemUseAction(stack); 
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000; //super.getMaxItemUseDuration(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        System.out.format("Held1: %s\n", itemstack.getUnlocalizedName());

        if (!worldIn.isRemote) {
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }

        // Open Gui Here?
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);

        //return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        ItemStack itemstack = player.getHeldItemMainhand();

        System.out.format("Held3: %s\n", itemstack.getUnlocalizedName());

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block t;
        String pname = player.getName();
        String bpos = OrionMain.PosToStr(pos);
        BlockPos p = pos;
        OrionProtectBlock opb;

        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }

        opb = sp.isProtected(bpos);

        if (opb != null) {
            sp.Unprotect(worldIn, p, pname);
            sp.setForUpdate();

            player.sendMessage(new TextComponentTranslation(String.format("%s %s Block x=%d  y=%d  z=%d is now unprotected\n", pname, opb.BlockName, p.getX(), p.getY(), p.getZ())));
            t = worldIn.getBlockState(p).getBlock();
            System.out.format("U Block=%s  Hardness=%1.2f  Resistance=%1.2f\n", t.getUnlocalizedName(), sp.getBlockHardness(t), sp.getBlockResistance(t));
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return stack; //super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        CommonProxy.registerItem(this, unlocalizedName, 0);
        return super.setUnlocalizedName(unlocalizedName);
    }
}
