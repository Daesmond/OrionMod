/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.items;

import Orion.OrionItems;
import Orion.Proxy.ClientProxy;
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
import net.minecraft.world.World;

/**
 *
 * @author Daesmond
 */
public class ItemDiamondWand extends ItemAbstract {

    public ItemDiamondWand() {
        this.maxStackSize = 1;
        this.setCreativeTab(OrionItems.tab);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        System.out.format("Held1: %s\n", itemstack.getUnlocalizedName());

        if (!worldIn.isRemote) {
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            ClientProxy.LoadGuiPassword();
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        ItemStack itemstack = player.getHeldItemMainhand();

        System.out.format("Held3: %s\n", itemstack.getUnlocalizedName());

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItemMainhand();

        System.out.format("Held2: %s\n", itemstack.getUnlocalizedName());

        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return stack;
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        registerItem(this, unlocalizedName, 0);
        return super.setUnlocalizedName(unlocalizedName);
    }


}
