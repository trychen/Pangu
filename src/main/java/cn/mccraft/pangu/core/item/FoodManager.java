package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.capability.CapabilityFood;
import cn.mccraft.pangu.core.capability.FoodStats;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public enum FoodManager {
    INSTANCE;

    private Set<FoodStats> foods = new HashSet<>();

    FoodManager() {
        addFood(new CapabilityFood.Implementation().setAmount(2).setUnlocalizedName("testFood").setModel(new ModelResourceLocation("minecraft:apple")));
    }

    public void addFood(FoodStats stats) {
        foods.add(stats);
    }

    public void removeFood(FoodStats stats) {
        foods.remove(stats);
    }

    public void addFoods(Collection<? extends FoodStats> stats) {
        foods.addAll(stats);
    }

    public Set<FoodStats> getFoods() {
        return foods;
    }

    public Set<ItemStack> toStacks() {
        Set<ItemStack> stacks = new HashSet<>();
        for (FoodStats stats : foods) {
            ItemStack stack = new ItemStack(PanguItems.PANGU_FOOD);
            stack.getCapability(CapabilityFood.CAPABILITY_FOOD_STATS, null)
                    .setUnlocalizedName(stats.getUnlocalizedName())
                    .setPotionEffectProbability(stats.getPotionEffectProbability())
                    .setPotion(stats.getPotion())
                    .setMaxItemUseDuration(stats.getMaxItemUseDuration())
                    .setAction(stats.getAction())
                    .setAmount(stats.getAmount())
                    .setSaturationModifier(stats.getSaturationModifier())
                    .setEatenCallback(stats.getEatenCallback())
                    .setUseFinishCallback(stats.getUseFinishCallback());
            if (stats.isWolfFood())
                stack.getCapability(CapabilityFood.CAPABILITY_FOOD_STATS, null).setWolfFood();
            if (stats.isAlwaysEdible())
                stack.getCapability(CapabilityFood.CAPABILITY_FOOD_STATS, null).setAlwaysEdible();
            stacks.add(stack);
        }
        return stacks;
    }
}
