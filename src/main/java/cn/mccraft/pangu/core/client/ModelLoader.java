package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.capability.color.CapabilityColor;
import cn.mccraft.pangu.core.capability.food.FoodStats;
import cn.mccraft.pangu.core.item.FoodManager;
import cn.mccraft.pangu.core.item.PGFoodMeshDefinition;
import cn.mccraft.pangu.core.item.PanguItems;
import cn.mccraft.pangu.core.loader.Load;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;

public interface ModelLoader {
    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    static void loadModelMeshDefinitions() {
        net.minecraftforge.client.model.ModelLoader.setCustomMeshDefinition(PanguItems.FOOD, new PGFoodMeshDefinition());
    }

    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    static void loadColors() {
        final ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        itemColors.registerItemColorHandler((stack, tintIndex) -> {
            if (tintIndex == 0) {
                if (stack.hasCapability(CapabilityColor.COLOR_STATS, null)) {
                    return stack.getCapability(CapabilityColor.COLOR_STATS, null).getColor();
                }
            }
            return -1;
        }, PanguItems.SILK);
    }

    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    static void addVariantNames() {
        FoodManager.INSTANCE.getFoods().stream().map(FoodStats::getModel).forEach(model -> ModelBakery.registerItemVariants(PanguItems.FOOD, model));
    }
}
