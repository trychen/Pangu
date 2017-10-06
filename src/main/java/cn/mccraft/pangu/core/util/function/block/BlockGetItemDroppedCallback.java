package cn.mccraft.pangu.core.util.function.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

@FunctionalInterface
public interface BlockGetItemDroppedCallback {
  Item apply(IBlockState arg0, Random arg1, int arg2);
}
