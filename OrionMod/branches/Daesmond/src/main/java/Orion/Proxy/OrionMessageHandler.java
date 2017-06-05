/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Daesmond
 */
public class OrionMessageHandler implements IMessageHandler<OrionMessage, IMessage> {

    @Override
    public IMessage onMessage(OrionMessage msg, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            EntityPlayerMP p = ctx.getServerHandler().player;
            System.out.println(String.format("Received %s from %s", msg.Message, p.getName()));
        }

        // No response packet        
//        EntityPlayer p;
//
//        if (ctx.side == Side.SERVER) {
//            p = (EntityPlayer) ctx.getServerHandler().player;
//            IThreadListener mainThread = (WorldServer) p.getServer().getEntityWorld();
//
//            mainThread.addScheduledTask(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(String.format("Received %s from %s", msg.Message, p.getName()));
//                }
//            });
//
//            //System.out.println(String.format("Received %s from %s", msg.Message, p.getName()));
//        } else {
//            //p = (EntityPlayer) ctx.getClientHandler().getNetworkManager();   
//        }
//        System.out.println(String.format("Received %s from %s", message.Message, ctx.getServerHandler().player.getName()));
//        return null;
//        
//
//        IThreadListener mainThread = (WorldServer) ctx.getServerHandler() // or Minecraft.getMinecraft() on the client
//
//                
//        mainThread.addScheduledTask(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(String.format("Received %s from %s", msg.Message, ctx.getServerHandler().playerEntity.getDisplayName()));
//            }
//        });
        return null; // no response in this case

    }

}
