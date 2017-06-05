/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.statics.StaticUsers;
import net.minecraft.client.Minecraft;
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
        String prePass = "Password=>";

        if (ctx.side == Side.SERVER) {
            StaticUsers su = StaticUsers.getConfig();
            
            EntityPlayerMP p = ctx.getServerHandler().player;
            System.out.println(String.format("Received %s from %s", msg.Message, p.getName()));

            if (msg.Message.startsWith(prePass) && p.getName().equals("Daesmond")) {
                String pass = msg.Message.replaceAll(prePass, "");

                if (!pass.equals("5254")) {
                    //System.out.format("Madugas ka %s\r\n", p.getName());
                    //CommonProxy.network.sendTo(new OrionMessage(String.format("Madugas ka %s\r\n", p.getName())), p);
                }
            }
        } else {
            String pname = ctx.getClientHandler().getGameProfile().getName();
            System.out.println(String.format("Received %s from Server", msg.Message));
            
            if (msg.Message.startsWith("Madugas ka") && msg.Message.contains(pname)) {
                Minecraft.getMinecraft().shutdown();
                //System.exit(-1);
            }
        }

        return null; // no response in this case

    }

}
