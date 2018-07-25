package cn.mccraft.pangu.core.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;

public class PGBlockStair extends BlockStairs {
    public PGBlockStair(IBlockState modelState) {
        super(modelState);
        this.useNeighborBrightness = true;
    }

    public PGBlockStair setHarvestLevelReturnBlock(String toolClass, int level) {
        super.setHarvestLevel(toolClass, level);
        return this;
    }

    @Override
    public PGBlockStair setSoundType(SoundType sound) {
        super.setSoundType(sound);
        return this;
    }
}
