package cn.mccraft.pangu.core.block;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class PGBlockFence extends BlockFence {
    public PGBlockFence(Material materialIn) {
        super(materialIn, materialIn.getMaterialMapColor());
    }

    public PGBlockFence(Material materialIn, MapColor mapColorIn) {
        super(materialIn, mapColorIn);
    }
}
