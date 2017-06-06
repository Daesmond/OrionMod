/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

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
            GuiPassword g = GuiPassword.getConfig();
            
            if (g.getConfig().isInit() && g.getConfig().getKeyInt() == 1) { // ESC is BAD!
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onGuiKeyInputEvent(KeyboardInputEvent event) {
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
    }
}
