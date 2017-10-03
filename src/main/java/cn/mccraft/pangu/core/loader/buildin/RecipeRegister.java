package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.item.IRecipeProvider;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.RegRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.registries.GameData;

import java.util.Arrays;

/**
 * the recipes register
 *
 * @author trychen
 */
public class RecipeRegister extends BaseRegister<Object, RegRecipe>{

    @Load(LoaderState.PREINITIALIZATION)
    public void registerRecipe(){
        itemSet.forEach(item -> {
            // check is a provider
            if (item.getItem() instanceof IRecipeProvider) {

                IRecipeProvider provider = (IRecipeProvider) item.getItem();

                Arrays.stream(provider.createRecipes()).forEach(GameData::register_impl);
            } else if (item.getItem() instanceof IRecipe){
                GameData.register_impl((IRecipe) item.getItem());
            }
        });
    }
}
