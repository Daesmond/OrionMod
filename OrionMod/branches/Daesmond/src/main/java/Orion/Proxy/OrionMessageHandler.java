/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.OrionMain;
import Orion.gui.GuiPassword;
import Orion.statics.StaticUsers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Daesmond
 */
public class OrionMessageHandler implements IMessageHandler<OrionMessage, IMessage> {

    public static boolean isFirst = false;

    @Override
    public IMessage onMessage(OrionMessage msg, MessageContext ctx) {
        String prePass = "Password=>";
        String preENTER = "ENTERPASS";
        String preNOTAUTH = "NOTAUTH";
        String preSHUT = "Madugas ka";

        if (ctx.side == Side.SERVER) { // Server side
            StaticUsers su = StaticUsers.getConfig();
            EntityPlayerMP p = ctx.getServerHandler().player;
            String pname = p.getName();

            //System.out.println(String.format("Received %s from %s", msg.Message, pname));
            if (msg.Message.startsWith(preNOTAUTH)) {
                //System.out.format("NOT YET AUTH ENTER PASS AGAIN %s\r\n", pname);
                //ServerProxy.network.sendTo(new OrionMessage(preENTER), p);

                ServerProxy.network.sendTo(new OrionMessage(String.format("%s %s", preSHUT, pname)), p); // Probably escaped GUI so shutdown client instead
            } else if (msg.Message.startsWith(prePass)) {
                String pass = msg.Message.replaceAll(prePass, "");

                if (!su.isAuthUser(pname, pass)) { // Failed authentication so shutdown client
                    //System.out.format("%s %s\r\n", preSHUT, p.getName());
                    ServerProxy.network.sendTo(new OrionMessage(String.format("%s %s", preSHUT, pname)), p);
                } else { // Successful authentication
                    System.out.format("%s is now authenticted!", pname);
                    p.sendMessage(new TextComponentTranslation(String.format("%s you are now authenticated!\n", pname)));
                }
            }
        } else { // Client Side            
            String pname = ctx.getClientHandler().getGameProfile().getName();
            // System.out.println(String.format("Received %s from Server", msg.Message));

            if (msg.Message.equals(preENTER)) {
                if (!OrionMessageHandler.isFirst) {
                    EntityPlayer p = Minecraft.getMinecraft().world.getPlayerEntityByName(pname);
                    boolean pasok = true;

                    while (pasok) { // Make sure inGameHashFocus before opening GUI
                        pasok = !Minecraft.getMinecraft().inGameHasFocus;

                        try {
                            Thread.sleep(500);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (Minecraft.getMinecraft().inGameHasFocus) {
                        OrionMessageHandler.isFirst = true;
                        p.openGui(OrionMain.instance, GuiPassword.getGuiID(), p.getEntityWorld(), (int) p.posX, (int) p.posY, (int) p.posZ);
                    }
                }
            } else if (msg.Message.startsWith(preSHUT)) {
                Minecraft.getMinecraft().shutdown();
                System.exit(-1);
            }
        }

        return null;
    }

}
