/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 *
 * @author Daesmond
 */
public class OrionBlockPos extends BlockPos {

    protected Map<BlockPos, Block> blks;

    public OrionBlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public OrionBlockPos(BlockPos p) {
        super(p.getX(), p.getY(), p.getZ());
    }

    public OrionBlockPos CalcRadius(World world) { // ugly code of mine hahahahhaha
        BlockPos bp = this;
        BlockPos nbp;

        blks = new HashMap();
        bp = bp.north();

        nbp = new OrionBlockPos(bp).up();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());

        bp = this;
        bp = bp.south();

        nbp = new OrionBlockPos(bp).up();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());

        bp = this;
        bp = bp.west();

        nbp = new OrionBlockPos(bp).up();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());

        bp = this;
        bp = bp.east();

        nbp = new OrionBlockPos(bp).up();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).up().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().west();
        blks.put(nbp, world.getBlockState(nbp).getBlock());
        nbp = new OrionBlockPos(bp).down().east();
        blks.put(nbp, world.getBlockState(nbp).getBlock());

        return this;
    }

    public Map<BlockPos, Block> getRadius() {
        return blks;
    }

    public OrionBlockPos(double x, double y, double z) {
        super(x, y, z);
    }

    public OrionBlockPos(Entity source) {
        super(source);
    }

    public OrionBlockPos(Vec3d vec) {
        super(vec);
    }

    public OrionBlockPos(Vec3i source) {
        super(source);
    }

}
