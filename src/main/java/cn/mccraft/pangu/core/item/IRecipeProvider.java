package cn.mccraft.pangu.core.item;

import net.minecraft.item.crafting.IRecipe;

/**
 * Recipe provider for item and block, the item is annotated by RegRecipe.
 */
public interface IRecipeProvider {
    IRecipe[] createRecipes();
}
