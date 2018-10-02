package cn.mccraft.pangu.core.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PGEntitySpawner extends Item {
    private final EntityFactory entityFactory;

    public PGEntitySpawner(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (!world.isRemote)
            world.spawnEntity(entityFactory.createEntity());

        if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.isCreativeMode) {
            stack.shrink(1);
        }
    }

    interface EntityFactory {
        Entity createEntity();
    }
}
