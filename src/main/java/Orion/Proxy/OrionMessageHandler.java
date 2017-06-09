/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.statics.StaticUsers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        String preOKey = "ORIONKEY=> ";

        if (ctx.side == Side.SERVER) { // Server side
            StaticUsers su = StaticUsers.getConfig();
            EntityPlayerMP player = ctx.getServerHandler().player;
            String pname = player.getName();

            System.out.println(String.format("Received %s from %s", msg.Message, pname));

            if (msg.Message.startsWith(preNOTAUTH)) {
                //System.out.format("NOT YET AUTH ENTER PASS AGAIN %s\r\n", pname);
                //ServerProxy.network.sendTo(new OrionMessage(preENTER), p);
                //ServerProxy.network.sendTo(new OrionMessage(String.format("%s %s", preSHUT, pname)), player);
            } else if (msg.Message.startsWith(prePass)) {
                String pass = msg.Message.replaceAll(prePass, "");

                if (!su.isAuthUser(pname, pass)) { // Failed authentication so shutdown client
                    System.out.format("> %s %s\r\n", preSHUT, player.getName());
                    ServerProxy.network.sendTo(new OrionMessage(String.format("%s %s", preSHUT, pname)), player);
                } else { // Successful authentication
                    System.out.format("> %s is now authenticated!\r\n", pname);
                    player.capabilities.disableDamage = false;
                    player.sendMessage(new TextComponentTranslation(String.format("%s %s", pname, preAUTH)));
                }
            } else if (msg.Message.startsWith(preOKey)) {
                String keyname = msg.Message.replaceAll(preOKey, "");
                ItemStack stack = player.getHeldItemMainhand();

                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setString("keyname", keyname);
                }
                

                //NBTTagCompound tagKey = new NBTTagCompound(stack);
                //tagKey.setTag(keyname, tagKey);
                //stack.setTagInfo("keyname", tagKey);
                //ItemStack book = new ItemStack(Items.written_book);
                //NBTTagList bookPages = new NBTTagList();
                //bookPages.appendTag(new NBTTagString("hello hi"));
                //bookPages.appendTag(new NBTTagString("page 2"));
                //book.setTagInfo("pages", bookPages);
                //book.setTagInfo("author", new NBTTagString("author"));
                //book.setTagInfo("title", new NBTTagString("title"));
                //playerIn.inventory.addItemStackToInventory(book);
            }
        } else { // Client Side            
            //String pname = ctx.getClientHandler().getGameProfile().getName();
            Minecraft mc = ClientProxy.getMC();
            // System.out.println(String.format("Received %s from Server", msg.Message));

            if (msg.Message.equals(preENTER)) {
                if (!isFirst) {
                    //EntityPlayer p = ClientProxy.getMC().world.getPlayerEntityByName(pname);
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
