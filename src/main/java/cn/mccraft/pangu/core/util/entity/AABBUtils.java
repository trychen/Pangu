package cn.mccraft.pangu.core.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class AABBUtils {

    /**
     * get the nearest entity, by AxisAlignedBB
     *
     * @param type the type of entity, such as Entity, EntityLiving, EntityZombie ...
     * @param downLowAmount the down low amount
     * @param pos the pos of searching center
     * @param turretRange the searching radius
     * @param predicate checker
     * @param <T> Entity
     */
    @Nullable
    public static <T extends Entity> T getTargetWithMinimumRange(Class<T> type, World worldObj, int downLowAmount, Vec3d pos, int turretRange, Predicate<Entity> predicate) {
        Entity nearestEntity = null;
        if (worldObj.isRemote) return null;

        AxisAlignedBB axis = new AxisAlignedBB(
                pos.addVector(-turretRange, -turretRange, turretRange),
                pos.addVector(-downLowAmount, turretRange, turretRange)
        );

        return worldObj.getEntitiesWithinAABB(type, axis)
                .stream()
                .filter(target -> nearestEntity.getDistance(pos.x, pos.y, pos.z) > target.getDistance(pos.x, pos.y, pos.z))
                .filter(predicate).findFirst().get();
    }
}
