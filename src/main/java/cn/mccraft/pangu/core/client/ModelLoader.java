package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.capability.FoodStats;
import cn.mccraft.pangu.core.item.FoodManager;
import cn.mccraft.pangu.core.item.PGFoodMeshDefinition;
import cn.mccraft.pangu.core.item.PanguItems;
import cn.mccraft.pangu.core.loader.Load;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelLoader {
    @Load(value = LoaderState.INITIALIZATION,side = Side.CLIENT)
    public static void loadModelMeshDefinitions() {
        net.minecraftforge.client.model.ModelLoader.setCustomMeshDefinition(PanguItems.PANGU_FOOD, new PGFoodMeshDefinition());
    }

    @Load(value = LoaderState.INITIALIZATION,side = Side.CLIENT)
    public static void addVariantNames() {
        FoodManager.INSTANCE.getFoods().stream().map(FoodStats::getModel).forEach(model -> ModelBakery.registerItemVariants(PanguItems.PANGU_FOOD, model));
    }
}
