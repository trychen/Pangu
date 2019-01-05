package cn.mccraft.pangu.core.item;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;

public class PGEntitySpawner extends Item {
    @Getter
    @Setter
    private final Function<EntityPlayer, Entity> entityFactory;

    @Getter
    private SoundEvent soundEvent;

    @Getter
    private SoundCategory soundCategory;

    public PGEntitySpawner(Function<EntityPlayer, Entity> entityFactory) {
        this.entityFactory = entityFactory;
    }

    public Function<EntityPlayer, Entity> getEntityFactory() {
        return entityFactory;
    }

    public PGEntitySpawner setSoundOnClick(SoundEvent soundEvent, SoundCategory soundCategory) {
        this.soundEvent = soundEvent;
        this.soundCategory = soundCategory;
        return this;
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (soundEvent != null)
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, soundEvent, soundCategory, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            Entity entity = entityFactory.apply(playerIn);
            if (entity != null) worldIn.spawnEntity(entity);
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }

        if (!playerIn.capabilities.isCreativeMode)
            itemstack.shrink(1);


        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
}
