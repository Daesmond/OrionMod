/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.gui;

import Orion.Proxy.ClientProxy;
import Orion.Proxy.CommonProxy;
import Orion.Proxy.OrionMessage;
import java.io.IOException;
import java.util.Arrays;
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

    private static GuiPassword instance;
    private static final int GUIID = 14344;

    private GuiButton btnSubmit;
    private GuiLabel lblEnter;
    private GuiTextField txtPass;
    private FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
    private boolean isInit;
    private String strHide;
    private int keyInt;
    private char keyChar;

    public GuiPassword() {
        super();
        isInit = false;
        keyInt = -1;
        keyChar = (char) -1;
    }

    public static GuiPassword getConfig() {
        if (instance == null) {
            instance = new GuiPassword();
        }
        return instance;
    }

    public int getKeyInt() {
        return keyInt;
    }

    public int getKeyCharInt() {
        return Character.getNumericValue(keyChar);
    }

    public char getKeyChar() {
        return keyChar;
    }

    public boolean isInit() {
        return isInit;
    }

    public static int getGuiID() {
        return GuiPassword.GUIID;
    }

    public void SetTxtPassFocus() {
        txtPass.setFocused(true);
    }

    private void Submit() {
        String pass = strHide.trim();
        String md5 = CommonProxy.MD5(pass);

        //CommonProxy.network.sendToServer(new OrionMessage(String.format("Text clear Password %s  and length is %d", pass, pass.length())));
        CommonProxy.network.sendToServer(new OrionMessage(String.format("Password=>%s", md5))); // Send to server for authentication
        this.mc.displayGuiScreen(null);

        if (this.mc.currentScreen == null) {
            this.mc.setIngameFocus();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClientProxy.Sleep(10); // There's a race condition thus try to sleep before default background
        this.drawDefaultBackground();
        txtPass.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.btnSubmit) {
            String pass = strHide;

            if (pass == null || pass.trim().length() == 0) {
                txtPass.setFocused(true);

                return;
            }

            Submit();
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        try {
            keyChar = par1;
            keyInt = par2;

            super.keyTyped(par1, par2);

            if (keyInt == 28) { // enter key
                Submit();
                return;
            }

            txtPass.textboxKeyTyped(par1, par2);
            String s = txtPass.getText();

            if (s.length() == 0) {
                strHide = "";
            } else if (strHide.length() < s.length()) {
                String r = Character.toString(s.charAt(s.length() - 1));
                char[] ch = new char[s.length()];
                
                Arrays.fill(ch, '*');
                strHide += r;
                s = new String(ch);
                txtPass.setText(s);
            } else if (strHide.length() > s.length()) {
                strHide = strHide.substring(0, s.length());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateScreen() {
        if (txtPass != null) {
            txtPass.updateCursorCounter();
        }

        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        try {
            super.mouseClicked(x, y, btn);
            //this.txtPass.mouseClicked(x, y, btn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        int btnWidth = 50;

        super.initGui();
        allowUserInput = true;
        strHide = "";

        lblEnter = new GuiLabel(fr, 1, this.width / 2 - 68, this.height / 2 - 72, 150, 20, 0xFFFFFF);
        txtPass = new GuiTextField(2, fr, this.width / 2 - 68, this.height / 2 - 48, 150, 20);
        btnSubmit = new GuiButton(3, this.width / 2 - 68 + btnWidth, this.height / 2 - 24, "Submit");

        lblEnter.addLine("Enter Password");
        lblEnter.setCentered();

        txtPass.setMaxStringLength(16);
        txtPass.setText("");
        txtPass.setFocused(true);

        btnSubmit.setWidth(btnWidth);

        labelList.add(lblEnter);
        buttonList.add(btnSubmit);
        isInit = true;
    }

}
