/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

/**
 *
 * @author Daesmond
 */
public class GuiTutorial extends GuiScreen {

    private GuiButton a;
    private GuiButton b;
    private GuiLabel l;
    private GuiTextField t;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.t.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.a) {
            //Main.packetHandler.sendToServer(...);
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
        if (button == this.b) {
            //Main.packetHandler.sendToServer(...);
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        try {
            super.keyTyped(par1, par2);
            this.t.textboxKeyTyped(par1, par2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.t.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        try {
            super.mouseClicked(x, y, btn);
            this.t.mouseClicked(x, y, btn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(this.a = new GuiButton(0, this.width / 2 - 100, this.height / 2 - 24, "This is button a"));
        this.buttonList.add(this.b = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 4, "This is button b"));
//        this.labelList.add(this.l = new GuiLabel(3, this.width / 2 - 100, this.height /2 + 4, "This is label l"));
//        GuiLabel test = new GuiLabel();

        this.t = new GuiTextField(4, Minecraft.getMinecraft().fontRenderer, this.width / 2 - 68, this.height / 2 - 46, 137, 20);
        t.setMaxStringLength(23);
        t.setText("sample text");
        this.t.setFocused(true);

        this.allowUserInput = true;
    }

}
