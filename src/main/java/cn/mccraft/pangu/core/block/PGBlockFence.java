package cn.mccraft.pangu.core.block;

import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PGBlockFence extends BlockFence {
    public PGBlockFence(Material materialIn) {
        super(materialIn, materialIn.getMaterialMapColor());
    }

    public PGBlockFence(Material materialIn, MapColor mapColorIn) {
        super(materialIn, mapColorIn);
    }

    @Override
    public PGBlockFence setSoundType(SoundType sound) {
        super.setSoundType(sound);
        return this;
    }

    public PGBlockFence setHarvestLevelR(String toolClass, int level) {
        super.setHarvestLevel(toolClass, level);
        return this;
    }
}
