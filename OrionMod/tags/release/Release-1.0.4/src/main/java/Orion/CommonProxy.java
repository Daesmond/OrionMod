/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 * @author Daesmond
 */
public abstract class CommonProxy {

    public void preInit() {
        // minecraftbyexample.mbe31_inventory_furnace.StartupCommon.preInitCommon();
        NetworkRegistry.INSTANCE.registerGuiHandler(OrionMain.instance, GuiHandlerRegistry.getInstance());
        GuiHandlerRegistry.getInstance().registerGuiHandler(new GuiHandler(), GuiHandler.getGuiID());
    }

    public void init() {
        // minecraftbyexample.mbe31_inventory_furnace.StartupCommon.initCommon();
    }

    public void postInit() {
        // minecraftbyexample.mbe31_inventory_furnace.StartupCommon.postInitCommon();
    }

    abstract public boolean playerIsInCreativeMode(EntityPlayer player);

    abstract public boolean isDedicatedServer();

    public static void registerItem(Item item, String name, int meta) {
        ResourceLocation loc = new ResourceLocation("orionmod", name);

        if (!Item.REGISTRY.containsKey(loc)) {
            //System.out.format("Register: %s  Domain: %s  Path: %s\n", name, loc.getResourceDomain(), loc.getResourcePath());
            GameRegistry.register(item, loc);
        }
    }

    public static void ModelItem(Item item, String loc) {
        //System.out.format("Model Regname: %s\n", item.getRegistryName());
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), loc));
    }
}
