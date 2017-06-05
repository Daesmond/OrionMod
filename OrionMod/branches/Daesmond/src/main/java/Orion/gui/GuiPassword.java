/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.gui;

import Orion.Proxy.CommonProxy;
import Orion.Proxy.OrionMessage;
import Orion.Proxy.OrionMessageHandler;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

/**
 *
 * @author Daesmond
 */
public class GuiPassword extends GuiScreen {

    private static final int GUIID = 14344;

    private GuiButton btnSubmit;
    //private GuiButton b;
    private GuiLabel lblEnter;
    private GuiTextField txtPass;
    private FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
    private boolean isClick;

    public GuiPassword() {
        super();
    }

    public static int getGuiID() {
        return GuiPassword.GUIID;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.txtPass.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.btnSubmit) {
            String pass = txtPass.getText();

            if (pass == null || pass.trim().length() == 0) {
                txtPass.setFocused(true);

                return;
            }

            pass = CommonProxy.MD5(pass);

            CommonProxy.network.sendToServer(new OrionMessage(String.format("Password=>%s", pass)));

            isClick = true;
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        try {
            System.out.format("Napindot ko ay %c %d", par1, par2);
            super.keyTyped(par1, par2);
            System.out.format("Napindot2 ko ay %c %d", par1, par2);
            this.txtPass.textboxKeyTyped(par1, par2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.txtPass.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        try {
            super.mouseClicked(x, y, btn);
            this.txtPass.mouseClicked(x, y, btn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onGuiClosed() {
        if (!isClick) {
            OrionMessageHandler.isFirst = false;
            CommonProxy.network.sendToServer(new OrionMessage("NOTAUTH"));
        }

        super.onGuiClosed();
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
    }

    @Override
    public void initGui() {
        int btnWidth = 50;
        super.initGui();
        //this.buttonList.add(this.b = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 4, "This is button b"));

        lblEnter = new GuiLabel(fr, 1, this.width / 2 - 68, this.height / 2 - 72, 150, 20, 0xFFFFFF);
        txtPass = new GuiTextField(2, fr, this.width / 2 - 68, this.height / 2 - 48, 150, 20);
        btnSubmit = new GuiButton(3, this.width / 2 - 68, this.height / 2 - 24, "Submit");

        lblEnter.addLine("Enter Password");
        lblEnter.setCentered();

        txtPass.setMaxStringLength(16);
        txtPass.setText("");
        txtPass.setFocused(true);

        btnSubmit.setWidth(btnWidth);

        labelList.add(lblEnter);
        buttonList.add(btnSubmit);
        allowUserInput = true;
        isClick = false;
    }

}
