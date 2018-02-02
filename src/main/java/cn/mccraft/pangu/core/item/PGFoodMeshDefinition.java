package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.capability.CapabilityFood;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class PGFoodMeshDefinition implements ItemMeshDefinition {
    @Nonnull
    @Override
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        if (stack.hasCapability(CapabilityFood.CAPABILITY_FOOD_STATS, null)) {
            final ModelResourceLocation model = stack.getCapability(CapabilityFood.CAPABILITY_FOOD_STATS, null).getModel();
            if (model != null)
                return model;
        }
        return new ModelResourceLocation("");
    }
}
