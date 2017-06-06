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
 * @author Daesmond
 */
public class OrionProtectBlock {

    public OrionProtectBlock() {
        block = null;
        BlockName = null;
        Hardness = 0.0f;
        Resistance = 0.0f;
        pos = null;
        axis = null;
        ByName = null;
    }

    public String ByName;
    public BlockPos pos;
    public String axis;
    public String BlockName;
    public float Hardness;
    public float Resistance;
    public Block block;

}
