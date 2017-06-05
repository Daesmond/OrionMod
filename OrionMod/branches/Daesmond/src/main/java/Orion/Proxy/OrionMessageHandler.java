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
                ServerProxy.network.sendTo(new OrionMessage("ENTERPASS"), p);
            } else if (msg.Message.startsWith(prePass)) {
                String pass = msg.Message.replaceAll(prePass, "");

                if (!su.isAuthUser(pname, pass)) {
                    //System.out.format("%s %s\r\n", preSHUT, p.getName());
                    CommonProxy.network.sendTo(new OrionMessage(String.format("%s %s", preSHUT, pname)), p);
                } else {
                    //System.out.println("Success AUTH " + pname);
                }
            }
        } else { // Client Side            
            String pname = ctx.getClientHandler().getGameProfile().getName();
            System.out.println(String.format("Received %s from Server", msg.Message));

            if (msg.Message.equals(preENTER)) {
                if (!OrionMessageHandler.isFirst) {
                    EntityPlayer p = Minecraft.getMinecraft().world.getPlayerEntityByName(pname);
                    boolean pasok = true;

                    OrionMessageHandler.isFirst = true;

                    while (pasok) { // Make sure inGameHashFocus before opening GUI
                        pasok = !Minecraft.getMinecraft().inGameHasFocus;

                        try {
                            Thread.sleep(500);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (Minecraft.getMinecraft().inGameHasFocus) {
                        p.openGui(OrionMain.instance, GuiPassword.getGuiID(), p.getEntityWorld(), (int) p.posX, (int) p.posY, (int) p.posZ);
                    }
                }
            } else if (msg.Message.startsWith(preSHUT) && msg.Message.contains(pname)) {
                Minecraft.getMinecraft().shutdown();
                System.exit(-1);
            }
        }

        return null;
    }

}
