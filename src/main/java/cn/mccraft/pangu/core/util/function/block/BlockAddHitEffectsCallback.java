package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockAddHitEffectsCallback {
  boolean apply(IBlockState arg0, World arg1, RayTraceResult arg2, ParticleManager arg3);
}
