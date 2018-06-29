package cn.mccraft.pangu.core.item;

import net.minecraft.item.crafting.IRecipe;

/**
 * Recipe provider for item and block, the item is annotated by {@link cn.mccraft.pangu.core.loader.annotation.RegRecipe}.
 */
public interface IRecipeProvider {
    IRecipe[] createRecipes();
}
