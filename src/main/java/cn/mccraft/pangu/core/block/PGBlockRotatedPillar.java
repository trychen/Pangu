package cn.mccraft.pangu.core.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class PGBlockRotatedPillar extends BlockRotatedPillar {
    public PGBlockRotatedPillar(Material materialIn) {
        super(materialIn);
    }

    public PGBlockRotatedPillar(Material materialIn, MapColor color) {
        super(materialIn, color);
    }
}
