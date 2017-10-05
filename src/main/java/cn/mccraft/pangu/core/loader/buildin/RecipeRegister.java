package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.item.IRecipeProvider;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.RegRecipe;
import cn.mccraft.pangu.core.util.resource.PanguResourceLocation;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

/**
 * the recipes register
 *
 * @author trychen
 */
@AutoWired
public class RecipeRegister extends BaseRegister<Object, RegRecipe>{

    @Load(LoaderState.INITIALIZATION)
    public void registerRecipe(){
        itemSet.forEach(item -> {
            if (item.getItem() instanceof IRecipeProvider) {
                // check is a provider
                IRecipeProvider provider = (IRecipeProvider) item.getItem();
                Arrays.stream(provider.createRecipes()).forEach(GameData::register_impl);
            } else if (item.getItem() instanceof IRecipe){
                // check if is a recipe
                registerRecipe(item.getAnnotation().value(), (IRecipe) item.getItem());
            }
        });
    }

    public void registerRecipe(String name, @Nonnull IRecipe recipe){
        GameData.register_impl(recipe.setRegistryName(new PanguResourceLocation(name)));
    }

    /**
     * build Shaped Recipe
     * @param name
     * @param stack
     * @param recipeComponents
     * @return
     */
    public static ShapedRecipes buildShapedRecipe(String name, ItemStack stack, Object... recipeComponents) {
        name = PanguCore.MODID.toLowerCase() + ":" + name;
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (recipeComponents[i] instanceof String[])
        {
            String[] astring = (String[])recipeComponents[i++];

            for (String s2 : astring)
            {
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }
        else
        {
            while (recipeComponents[i] instanceof String)
            {
                String s1 = (String)recipeComponents[i++];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }

        Map<Character, ItemStack> map;

        for (map = Maps.newHashMap(); i < recipeComponents.length; i += 2)
        {
            Character character = (Character)recipeComponents[i];
            ItemStack itemstack = ItemStack.EMPTY;

            if (recipeComponents[i + 1] instanceof Item)
            {
                itemstack = new ItemStack((Item)recipeComponents[i + 1]);
            }
            else if (recipeComponents[i + 1] instanceof Block)
            {
                itemstack = new ItemStack((Block)recipeComponents[i + 1], 1, 32767);
            }
            else if (recipeComponents[i + 1] instanceof ItemStack)
            {
                itemstack = (ItemStack)recipeComponents[i + 1];
            }

            map.put(character, itemstack);
        }

        NonNullList<Ingredient> aitemstack = NonNullList.withSize(j * k, Ingredient.EMPTY);

        for (int l = 0; l < j * k; ++l)
        {
            char c0 = s.charAt(l);

            if (map.containsKey(Character.valueOf(c0)))
            {
                aitemstack.set(l, Ingredient.fromStacks((map.get(Character.valueOf(c0))).copy()));
            }
        }

        return new ShapedRecipes(name, j, k, aitemstack, stack);
    }
}
