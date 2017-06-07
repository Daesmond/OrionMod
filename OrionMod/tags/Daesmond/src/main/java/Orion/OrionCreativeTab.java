/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import Orion.items.ItemPearlOrb;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Daesmond
 */
public class OrionCreativeTab extends CreativeTabs {

    public Item item = (Item) new ItemPearlOrb();
    public int meta = 0;

    public OrionCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(this.item, 1, this.meta);
    }
    
    
}
