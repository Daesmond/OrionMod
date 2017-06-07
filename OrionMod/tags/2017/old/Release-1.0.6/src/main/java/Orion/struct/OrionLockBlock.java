/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.struct;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

/**
 *
 * @author Admin
 */
public class OrionLockBlock {

    public OrionLockBlock() {
        ByName = null;
        Allowed = null;
        ItemName = null;
        isLocked = null;
    }

    public String ByName;
    public String Allowed;
    public String ItemName;
    public String isLocked;
}
