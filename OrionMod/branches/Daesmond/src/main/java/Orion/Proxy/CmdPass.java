/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import Orion.statics.StaticUsers;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Daesmond
 */
public class CmdPass implements ICommand {

    private final List aliases;
    private final String strsetpass = "setpass";

    // protected String fullEntityName;
    // protected Entity conjuredEntity;
    public CmdPass() {
        aliases = new ArrayList();
        aliases.add(strsetpass);
    }

    @Override
    public String getName() {
        return strsetpass;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("%s <password> [playername]", strsetpass);
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = sender.getEntityWorld();

        if (world.isRemote) {
            return;
        }

        if (args.length < 1) {
            return;
        }

        String argsstr = String.join(" ", args);
        //System.out.format("Arguments: %s\r\n", argsstr);
        StaticUsers su = StaticUsers.getConfig();
        String pass = CommonProxy.MD5(argsstr.split(" ")[0]);
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(argsstr);
        String pname = (m.find(0)) ? m.group() : sender.getName();

        if (pname.compareToIgnoreCase("Server") == 0) {
            return;
        } else {
            pname = pname.replaceAll("\\[", "").replaceAll("\\]", "");
        }
        
        su.SetPass(pname, pass);
        su.setForUpdate();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        return aliases;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

}
