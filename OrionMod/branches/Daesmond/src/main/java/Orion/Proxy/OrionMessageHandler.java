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

        return null; // no response in this case

    }

}
