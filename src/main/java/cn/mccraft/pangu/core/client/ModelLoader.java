package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.capability.color.CapabilityColor;
import cn.mccraft.pangu.core.capability.food.CapabilityFood;
import cn.mccraft.pangu.core.capability.food.FoodStats;
import cn.mccraft.pangu.core.item.FoodManager;
import cn.mccraft.pangu.core.item.PanguItems;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ModelLoader {
//    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    static void loadModelMeshDefinitions() {
        net.minecraftforge.client.model.ModelLoader.setCustomMeshDefinition(PanguItems.FOOD, (stack) -> {
            if (stack.hasCapability(CapabilityFood.FOOD_STATS, null)) {
                final ModelResourceLocation model = stack.getCapability(CapabilityFood.FOOD_STATS, null).getModel();
                if (model != null)
                    return model;
            }
            return new ModelResourceLocation("");
        });
    }

//    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
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

//    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    static void addVariantNames() {
        FoodManager.INSTANCE.getFoods().stream().map(FoodStats::getModel).forEach(model -> ModelBakery.registerItemVariants(PanguItems.FOOD, model));
    }
}
