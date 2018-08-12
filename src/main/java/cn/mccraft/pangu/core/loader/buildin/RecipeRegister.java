package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.item.RecipeProvider;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.RegRecipe;
import cn.mccraft.pangu.core.recipe.RecipeColor;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

import static java.lang.Character.valueOf;

/**
 * the recipes register
 *
 * @author trychen
 */
@AutoWired(registerCommonEventBus = true)
public class RecipeRegister extends StoredElementRegister<Object, RegRecipe> {
    @Load(LoaderState.INITIALIZATION)
    public void registerRecipe() {
        items.forEach(element -> {
            try {
                if (element.getInstance() instanceof RecipeProvider) {
                    // check is a provider
                    RecipeProvider provider = (RecipeProvider) element.getInstance();
                    Arrays.stream(provider.createRecipes()).forEach(GameData::register_impl);
                } else if (element.getInstance() instanceof IRecipe) {
                    // check if is a recipe
                    registerRecipe(element.getResLoc(element.getAnnotation().value()), (IRecipe) element.getInstance());
                }
            } catch (Exception ex) {
                PanguCore.getLogger().error("Unable to register " + element.getField().toGenericString(), ex);
            }
        });
        PanguCore.getLogger().info("Processed " + items.size() + " @RegRecipe Recipes");
    }

    public void registerRecipe(@Nonnull ResourceLocation resourceLocation, @Nonnull IRecipe recipe) {
        GameData.register_impl(recipe.setRegistryName(resourceLocation));
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeColor().setRegistryName(PanguResLoc.of("color")));
    }

    /**
     * build Shaped Recipe
     *
     * @param group            group name
     * @param stack            recipe return
     * @param recipeComponents components
     * @return unregistered recipe
     */
    public static ShapedRecipes buildShapedRecipe(String domain, String group, ItemStack stack, Object... recipeComponents) {
        group = domain + ":" + group;
        StringBuilder s = new StringBuilder();
        int i = 0;
        int j = 0;
        int k = 0;

        if (recipeComponents[i] instanceof String[]) {
            String[] astring = (String[]) recipeComponents[i++];

            for (String s2 : astring) {
                ++k;
                j = s2.length();
                s.append(s2);
            }
        } else {
            while (recipeComponents[i] instanceof String) {
                String s1 = (String) recipeComponents[i++];
                ++k;
                j = s1.length();
                s.append(s1);
            }
        }

        Map<Character, ItemStack> map;

        for (map = Maps.newHashMap(); i < recipeComponents.length; i += 2) {
            Character character = (Character) recipeComponents[i];
            ItemStack itemstack = ItemStack.EMPTY;

            if (recipeComponents[i + 1] instanceof Item) {
                itemstack = new ItemStack((Item) recipeComponents[i + 1]);
            } else if (recipeComponents[i + 1] instanceof Block) {
                itemstack = new ItemStack((Block) recipeComponents[i + 1], 1, 32767);
            } else if (recipeComponents[i + 1] instanceof ItemStack) {
                itemstack = (ItemStack) recipeComponents[i + 1];
            }

            map.put(character, itemstack);
        }

        NonNullList<Ingredient> itemList = NonNullList.withSize(j * k, Ingredient.EMPTY);

        for (int l = 0; l < j * k; ++l) {
            char c0 = s.charAt(l);

            if (map.containsKey(valueOf(c0))) {
                itemList.set(l, Ingredient.fromStacks((map.get(valueOf(c0))).copy()));
            }
        }

        return new ShapedRecipes(group, j, k, itemList, stack);
    }
}
