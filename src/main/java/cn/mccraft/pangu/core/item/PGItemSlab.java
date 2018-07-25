package cn.mccraft.pangu.core.item;

import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;

public class PGItemSlab extends ItemSlab {
    public PGItemSlab(BlockSlab singleSlab, BlockSlab doubleSlab) {
        super(singleSlab, singleSlab, doubleSlab);
    }
}
