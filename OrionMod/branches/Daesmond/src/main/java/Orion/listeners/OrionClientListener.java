/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.Proxy.ClientProxy;
import Orion.Proxy.CommonProxy;
import Orion.Proxy.OrionMessage;
import Orion.gui.GuiPassword;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Daemond
 */
@SideOnly(Side.CLIENT)
public class OrionClientListener {

    @SubscribeEvent
    public void onGuiOpenEvent(GuiOpenEvent event) {
        if (event.getGui() == null) {
            if (GuiPassword.getConfig().isInit() && GuiPassword.getConfig().getKeyInt() < 0) {
                CommonProxy.network.sendToServer(new OrionMessage("OpenGuiEvent Cancelling CloseEvent!"));
                event.setCanceled(true);
            }
        }

//        if (event.getGui() == null) {
//            CommonProxy.network.sendToServer(new OrionMessage("OpenGuiEvent Close!"));
//        } else {
//            if (event.getGui().mc != null) {
//                if (event.getGui().mc.currentScreen == null) {
//                    CommonProxy.network.sendToServer(new OrionMessage("OpenGuiEvent1 Gui.MC.currentScreen is null"));
//                }
//            }
//            CommonProxy.network.sendToServer(new OrionMessage(String.format("OpenGuiEvent %s", event.getGui().getClass().getName())));
//        }
//
//        if (event.getGui() instanceof GuiPassword) {
//            GuiPassword g = (GuiPassword) event.getGui();
//
////            CommonProxy.network.sendToServer(new OrionMessage("OpenGuiEvent GuiPassword"));
////
////            if (g.mc != null) {
////                if (g.mc.currentScreen == null) {
////                    CommonProxy.network.sendToServer(new OrionMessage("OpenGuiEvent2 Gui.MC.currentScreen is null"));
////                }
////            }
//
//            if (ClientProxy.getMC().currentScreen == null) {
//                CommonProxy.network.sendToServer(new OrionMessage("OpenGuiEvent MC.currentScreen is null"));
//                CommonProxy.network.sendToServer(new OrionMessage(
//                        String.format("OpenGuiEvent ButtonClick=%s IsInit=%s Cancelable=%s", g.isButtonClick(), g.isInit(), event.isCancelable()))
//                );
//
//                if (!g.isButtonClick() && g.isInit() && event.isCancelable()) {
//                    CommonProxy.network.sendToServer(new OrionMessage("OpenGuiEvent Nagcancel"));
//
//                    event.setCanceled(true);
//                }
//            }
//        }

    }

    @SubscribeEvent
    public void onGuiKeyInputEvent(KeyboardInputEvent event) {
//        if (event.getGui() instanceof GuiPassword) {
//            GuiPassword g = (GuiPassword) event.getGui();
//
//            CommonProxy.network.sendToServer(new OrionMessage(String.format("KeyInputEvent GP keychar=%d  keyInt=%d", g.getKeyCharInt(), g.getKeyInt())));
//
//            //if (((g.getKeyInt() == 0 && g.getKeyCharInt() == -1) || (g.getKeyInt() == 1 && g.getKeyCharInt() == -1)) && (event.isCancelable())) {
//            if ((g.getKeyInt() == 0 && g.getKeyCharInt() == -1) && (event.isCancelable())) {
//                g.mc.setIngameNotInFocus();
//                g.SetTxtPassFocus();
//            } else if ((g.getKeyInt() == 1 && g.getKeyCharInt() == -1) && (event.isCancelable())) {
//                CommonProxy.network.sendToServer(new OrionMessage("KeyInputEvent Cancelling"));
//                event.setCanceled(true);
//            }
//        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
    }
}
