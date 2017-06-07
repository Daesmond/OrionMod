/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import Orion.Proxy.CommonProxy;
import Orion.items.ItemDiamondWand;
import Orion.items.ItemGoldWand;
import Orion.items.ItemIronWand;
import Orion.items.ItemPearlOrb;
import Orion.items.ItemStoneWand;
import net.minecraft.item.Item;

/**
 *
 * @author Daesmond
 */
public class OrionItems {

    public static Item GoldWand;
    public static Item IronWand;
    public static Item StoneWand;
    public static Item DiamondWand;
    public static Item PearlOrb;

    public static OrionCreativeTab tab = new OrionCreativeTab(OrionMain.MODID);

    public static void load() {
        System.out.println("Loading Orion Items!");

        GoldWand = new ItemGoldWand().setUnlocalizedName("goldwand").setFull3D();
        IronWand = new ItemIronWand().setUnlocalizedName("ironwand").setFull3D();
        StoneWand = new ItemStoneWand().setUnlocalizedName("stonewand").setFull3D();
        DiamondWand = new ItemDiamondWand().setUnlocalizedName("diamondwand").setFull3D();
        PearlOrb = new ItemPearlOrb().setUnlocalizedName("pearl_orb").setFull3D();
    }

    public static void registerClient() {
        System.out.println("Set Orion model Items!");
        
        CommonProxy.ModelItem(IronWand, "inventory");
        CommonProxy.ModelItem(GoldWand, "inventory");
        CommonProxy.ModelItem(StoneWand, "inventory");
        CommonProxy.ModelItem(DiamondWand, "inventory");
        CommonProxy.ModelItem(PearlOrb, "inventory");
    }
}
