/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.Proxy;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 *
 * @author Daesmond
 */
public class OrionMessage implements IMessage {

    public String Message;

    public OrionMessage() {
        this.Message = "";
    }
    
    public OrionMessage(String msg) {
        System.out.format("Test: %s\r\n", msg);
        this.Message = msg;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.Message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
         ByteBufUtils.writeUTF8String(buf, this.Message);
    }

}
