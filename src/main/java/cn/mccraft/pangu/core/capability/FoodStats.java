package cn.mccraft.pangu.core.capability;

import cn.mccraft.pangu.core.util.function.FoodEatenCallback;
import cn.mccraft.pangu.core.util.function.FoodUseFinishCallback;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public interface FoodStats {
    /**
     *
     * @see ItemFood#getHealAmount(ItemStack)
     */
    int getAmount();

    FoodStats setAmount(int amount);

    /**
     *
     * @see ItemFood#isWolfsFavoriteMeat()
     */
    default boolean isWolfFood() {
        return false;
    }

    FoodStats setWolfFood();

    /**
     *
     * @see ItemFood#getSaturationModifier(ItemStack)
     */
    default float getSaturationModifier() {
        return 0.6f;
    }

    FoodStats setSaturationModifier(float saturationModifier);

    /**
     *
     * @see ItemFood#alwaysEdible
     */
    default boolean isAlwaysEdible() {
        return false;
    }

    FoodStats setAlwaysEdible();

    /**
     *
     * @see ItemFood#getMaxItemUseDuration(ItemStack)
     */
    default int getMaxItemUseDuration() {
        return 32;
    }

    FoodStats setMaxItemUseDuration(int maxItemUseDuration);

    /**
     *
     * @see ItemFood#getItemUseAction(ItemStack)
     */
    default EnumAction getAction() {
        return EnumAction.EAT;
    }

    FoodStats setAction(EnumAction action);

    PotionEffect getPotion();

    FoodStats setPotion(PotionEffect potion);

    float getPotionEffectProbability();

    FoodStats setPotionEffectProbability(float potionEffectProbability);

    String getUnlocalizedName();

    FoodStats setUnlocalizedName(String unlocalizedName);

    ModelResourceLocation getModel();

    FoodStats setModel(ModelResourceLocation model);

    /**
     *
     * @see ItemFood#onItemUseFinish(ItemStack, World, EntityLivingBase)
     */
    default FoodUseFinishCallback getUseFinishCallback() {
        return FoodUseFinishCallback.BASIC_CALLBACK;
    }

    FoodStats setUseFinishCallback(FoodUseFinishCallback callback);

    /**
     *
     * @see ItemFood#onFoodEaten(ItemStack, World, EntityPlayer)
     */
    default FoodEatenCallback getEatenCallback() {
        return FoodEatenCallback.BASIC_CALLBACK;
    }

    FoodStats setEatenCallback(FoodEatenCallback callback);
}
