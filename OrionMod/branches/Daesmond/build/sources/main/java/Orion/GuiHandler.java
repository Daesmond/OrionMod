/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 *
 * @author Daesmond
 */
public class GuiHandler implements IGuiHandler {

    private static final int TUTORIAL_GUI = 0;

    public static int getGuiID() {
        return TUTORIAL_GUI;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        System.out.println("getServerGuiElement");

        if (ID == this.getGuiID()) {
            return new GuiTutorial();
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        System.out.println("getClientGuiElement");

        if (ID == this.getGuiID()) {
            return new GuiTutorial();
        }

        return null;
    }
}
