/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import Orion.Proxy.ClientProxy;
import Orion.gui.GuiPassword;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
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
    public void onGuiOpenEvent(InitGuiEvent event) {
        if (event.getGui() instanceof GuiPassword) {
            ClientProxy.getMC().setIngameNotInFocus();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
    }
}
