/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.items;

import Orion.OrionMain;
import Orion.Proxy.CommonProxy;
import Orion.Proxy.OrionMessage;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 * @author Daesmond
 */
public abstract class ItemAbstract extends Item {

    public void registerItem(Item item, String name, int meta) {
        ResourceLocation loc = new ResourceLocation(OrionMain.MODID, name);

        if (!Item.REGISTRY.containsKey(loc)) {
            GameRegistry.register(item, loc);
        }
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        this.registerItem(this, unlocalizedName, 0);
        
        return super.setUnlocalizedName(unlocalizedName);
    }

    public void DMesg(World world, String msg) {
        if (world.isRemote) { // client side
            CommonProxy.network.sendToServer(new OrionMessage(String.format("Client: %s\r\n", msg)));
        } else {
            System.out.format(String.format("Server: %s\r\n", msg));
        }
    }
    
    public BlockPos toBlockPos(Vec3d vec) {
        int x = new Double(vec.xCoord).intValue();
        int y = new Double(vec.yCoord).intValue();
        int z = new Double(vec.zCoord).intValue();
        
        return new BlockPos(x, y, z);
    }
    
    public String BlockPosToStr(BlockPos bp) {
        return String.format("%s|%s|%s", bp.getX(), bp.getY(), bp.getZ());
    }

}
