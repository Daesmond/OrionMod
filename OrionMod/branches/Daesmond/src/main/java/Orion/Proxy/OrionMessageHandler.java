/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.statics.StaticUsers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Daesmond
 */
public class OrionMessageHandler implements IMessageHandler<OrionMessage, IMessage> {

    private boolean isFirst = false;
    private GameType myGameType;

    @Override
    public IMessage onMessage(OrionMessage msg, MessageContext ctx) {
        String prePass = "Password=>";
        String preENTER = "ENTERPASS";
        String preNOTAUTH = "NOTAUTH";
        String preSHUT = "Madugas ka";
        String preAUTH = "you are now authenticated!";

        if (ctx.side == Side.SERVER) { // Server side
            StaticUsers su = StaticUsers.getConfig();
            EntityPlayerMP p = ctx.getServerHandler().player;
            String pname = p.getName();

            System.out.println(String.format("Received %s from %s", msg.Message, pname));
            if (msg.Message.startsWith(preNOTAUTH)) {
                //System.out.format("NOT YET AUTH ENTER PASS AGAIN %s\r\n", pname);
                //ServerProxy.network.sendTo(new OrionMessage(preENTER), p);
                ServerProxy.network.sendTo(new OrionMessage(String.format("%s %s", preSHUT, pname)), p); // Probably escaped GUI so shutdown client instead
            } else if (msg.Message.startsWith(prePass)) {
                String pass = msg.Message.replaceAll(prePass, "");

                if (!su.isAuthUser(pname, pass)) { // Failed authentication so shutdown client
                    System.out.format("> %s %s\r\n", preSHUT, p.getName());
                    ServerProxy.network.sendTo(new OrionMessage(String.format("%s %s", preSHUT, pname)), p);
                } else { // Successful authentication
                    System.out.format("> %s is now authenticated!\r\n", pname);
                    p.capabilities.disableDamage = false;
                    p.sendMessage(new TextComponentTranslation(String.format("%s %s", pname, preAUTH)));
                }
            }
        } else { // Client Side            
            String pname = ctx.getClientHandler().getGameProfile().getName();
            Minecraft mc = ClientProxy.getMC();
            // System.out.println(String.format("Received %s from Server", msg.Message));

            if (msg.Message.equals(preENTER)) {
                if (!isFirst) {
                    EntityPlayer p = ClientProxy.getMC().world.getPlayerEntityByName(pname);
                    int sleeptime = 50;

                    while (!Minecraft.getMinecraft().inGameHasFocus) { // Make sure inGameHashFocus before opening GUI
                        try {
                            Thread.sleep(sleeptime);
                            sleeptime += 50;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (mc.inGameHasFocus) {
                        isFirst = true;
                        ClientProxy.LoadGuiPassword();
                    }
                }
            } else if (msg.Message.startsWith(preSHUT)) {
                Minecraft.getMinecraft().shutdown();
                //System.exit(-1);
            }
        }

        return null;
    }

}
