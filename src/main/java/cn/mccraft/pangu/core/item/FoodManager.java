package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.capability.food.CapabilityFood;
import cn.mccraft.pangu.core.capability.food.FoodStats;
import cn.mccraft.pangu.core.util.Environment;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.item.ItemStack;

import java.util.*;

public enum FoodManager {
    INSTANCE;

    private Set<FoodStats> foods = new HashSet<>();

    FoodManager() {
        Environment.devOnly(() -> addFood(new CapabilityFood.Implementation().setAmount(2).setTranslationKey("testFood").setModel(PanguResLoc.ofModel("minecraft", "apple"))));
    }

    /**
     * Add a food
     */
    public void addFood(FoodStats stats) {
        foods.add(stats);
    }

    /**
     * Remove a food
     */
    public void removeFood(FoodStats stats) {
        foods.remove(stats);
    }

    /**
     * Add foods
     */
    public void addFoods(Collection<? extends FoodStats> stats) {
        foods.addAll(stats);
    }

    /**
     * Add foods
     */
    public void addFoods(FoodStats... stats) {
        Collections.addAll(foods, stats);
    }

    /**
     * Get all foods
     */
    public Set<FoodStats> getFoods() {
        return foods;
    }

    /**
     * Convert all FoodStats to ItemStack
     */
    public Set<ItemStack> toStacks() {
        Set<ItemStack> stacks = new HashSet<>();
        for (FoodStats stats : foods) {
            ItemStack stack = new ItemStack(PanguItems.FOOD);
            FoodStats foodStats = Objects.requireNonNull(stack.getCapability(CapabilityFood.FOOD_STATS, null));

            foodStats
                    .setTranslationKey(stats.getUnlocalizedName())
                    .setPotionEffectProbability(stats.getPotionEffectProbability())
                    .setPotion(stats.getPotion())
                    .setMaxItemUseDuration(stats.getMaxItemUseDuration())
                    .setAction(stats.getAction())
                    .setAmount(stats.getAmount())
                    .setSaturationModifier(stats.getSaturationModifier())
                    .setEatenCallback(stats.getEatenCallback())
                    .setUseFinishCallback(stats.getUseFinishCallback());
            if (stats.isWolfFood()) foodStats.setWolfFood();
            if (stats.isAlwaysEdible()) foodStats.setAlwaysEdible();

            stacks.add(stack);
        }
        return stacks;
    }
}
