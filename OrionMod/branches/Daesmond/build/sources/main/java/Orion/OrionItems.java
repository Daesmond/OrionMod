/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import Orion.items.ItemDiamondWand;
import Orion.items.ItemGoldWand;
import Orion.items.ItemIronWand;
import Orion.items.ItemOrionKey;
import Orion.items.ItemPearlOrb;
import Orion.items.ItemStoneWand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
    public static Item OrionKey;

    public static OrionCreativeTab tab = new OrionCreativeTab(OrionMain.MODID);

    public static void load() {
        System.out.println("Loading Orion Items!");

        GoldWand = new ItemGoldWand().setUnlocalizedName("goldwand").setFull3D();
        IronWand = new ItemIronWand().setUnlocalizedName("ironwand").setFull3D();
        StoneWand = new ItemStoneWand().setUnlocalizedName("stonewand").setFull3D();
        DiamondWand = new ItemDiamondWand().setUnlocalizedName("diamondwand").setFull3D();
        PearlOrb = new ItemPearlOrb().setUnlocalizedName("pearl_orb").setFull3D();
        OrionKey= new ItemOrionKey().setUnlocalizedName("orionkey").setFull3D();
    }



    public static boolean isOp(EntityPlayer player) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getPlayerList().canSendCommands(player.getGameProfile());
    }
}
