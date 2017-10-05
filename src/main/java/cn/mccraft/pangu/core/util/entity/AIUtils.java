package cn.mccraft.pangu.core.util.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by trychen on 17/9/30.
 */
public class AIUtils {

    /**
     * checking if two Vec3d can see each other (there are not any block between)
     *
     * the started pos can be likely to:
     *                   for block: new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
     *                   for entity: entity.getPositionVector().addVector(0, entity.getEyeHeight(), 0);
     *
     * @return if can see each other
     */
    public static boolean canSee(World world, Vec3d traceStart, Vec3d traceEnd) {
        Vec3d vecDelta = new Vec3d(
                traceEnd.x - traceStart.x,
                traceEnd.y - traceStart.y,
                traceEnd.z - traceStart.z
        );

        // Normalize vector to the largest delta axis
        double vecDeltaLength = MathHelper.absMax(vecDelta.x, MathHelper.absMax(vecDelta.y, vecDelta.z));

        vecDelta = vecDelta.scale(1 / vecDeltaLength);

        // Limit how many non solid block a turret can see through
        for (int i = 0; i < 10; i++) {
            // Offset start position toward the target to prevent self collision
            traceStart = traceStart.add(vecDelta);

            RayTraceResult traced = world.rayTraceBlocks(traceStart.add(Vec3d.ZERO), traceEnd.add(Vec3d.ZERO));

            if (traced != null && traced.typeOfHit == RayTraceResult.Type.BLOCK) {
                IBlockState hitBlock = world.getBlockState(traced.getBlockPos());

                // If non solid block is in the way then proceed to continue
                // tracing
                if (hitBlock != null && !hitBlock.getMaterial().isSolid() && MathHelper.absMax(
                        MathHelper.absMax(traceStart.x - traceEnd.x, traceStart.y - traceEnd.y),
                        traceStart.z - traceEnd.z) > 1) {
                    // Start at new position and continue
                    traceStart = traced.hitVec;
                    continue;
                }
            }
            return traced != null;
        }

        // If all above failed, the target cannot be seen
        return false;
    }
}
