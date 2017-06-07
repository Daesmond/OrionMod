/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 * @author Daesmond
 */
public class OrionRecipes {
    public static void loadDefaultRecipes(int i) {
        if (i < 0) {
            System.out.println("Loading Orion Recipes");
            
            GameRegistry.addRecipe(new ItemStack(OrionItems.StoneWand, 1), new Object[]{"  X", " Y ", " Y ", Character.valueOf('X'), Items.STONE_HOE, Character.valueOf('Y'), Items.STICK});
            GameRegistry.addRecipe(new ItemStack(OrionItems.IronWand, 1), new Object[]{"  X", " Y ", " Y ", Character.valueOf('X'), Items.IRON_INGOT, Character.valueOf('Y'), Items.STICK});
            GameRegistry.addRecipe(new ItemStack(OrionItems.GoldWand, 1), new Object[]{"  X", " Y ", " Y ", Character.valueOf('X'), Items.GOLD_INGOT, Character.valueOf('Y'), Items.STICK});
            GameRegistry.addRecipe(new ItemStack(OrionItems.DiamondWand, 1), new Object[]{"  X", " Y ", " Y ", Character.valueOf('X'), Items.DIAMOND, Character.valueOf('Y'), Items.STICK});
            GameRegistry.addRecipe(new ItemStack(OrionItems.OrionKey, 1), new Object[]{"  X", " Y ", "Y  ", Character.valueOf('X'), Items.GOLD_NUGGET, Character.valueOf('Y'), Items.GOLD_INGOT});
        }
    }
}
