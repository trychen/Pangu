package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.capability.FoodStats;
import cn.mccraft.pangu.core.item.FoodManager;
import cn.mccraft.pangu.core.item.ItemPanguFoodMeshDefinition;
import cn.mccraft.pangu.core.item.PanguItems;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.Proxy;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelLoader {
    static {
        Proxy.INSTANCE.addLoader(ModelLoader.class);
    }

    @Load(side = Side.CLIENT)
    @SideOnly(Side.CLIENT)
    public void loadModelMeshDefinitions() {
        PanguCore.getLogger().info("Registering model mesh definition");
        net.minecraftforge.client.model.ModelLoader.setCustomMeshDefinition(PanguItems.PANGU_FOOD, new ItemPanguFoodMeshDefinition());
    }

    @Load(side = Side.CLIENT)
    @SideOnly(Side.CLIENT)
    public void addVariantNames() {
        PanguCore.getLogger().info("Registering variant names");
        FoodManager.INSTANCE.getFoods().stream().map(FoodStats::getModel).forEach(model -> ModelBakery.registerItemVariants(PanguItems.PANGU_FOOD, model));
    }
}
