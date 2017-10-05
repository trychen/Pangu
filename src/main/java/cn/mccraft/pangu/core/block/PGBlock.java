package cn.mccraft.pangu.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class PGBlock extends Block {
    public PGBlock(Material material) {
        super(material);
    }

    public PGBlock(Material material, MapColor mapColor) {
        super(material, mapColor);
    }
}