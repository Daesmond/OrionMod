/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.listeners;

import net.minecraft.util.math.BlockPos;

/**
 *
 * @author Daesmond
 */
public abstract class ListenerAbstract {

    protected BlockPos pPos = new BlockPos(0, 0, 0);

    protected void setPPos(BlockPos mypos) {
        pPos = new BlockPos(mypos);
    }
}
