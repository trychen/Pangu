package cn.mccraft.pangu.core.client.render;

import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@SuppressWarnings("unchecked")
public interface EntityModelRegister {

    /**
     * Registering custom model for entity
     *
     * @param entityClass Entity base class
     * @param modelClass model class
     * @param shadowSize shadow size
     */
    static void register(Class<? extends Entity> entityClass, Class<? extends ModelBase> modelClass, float shadowSize) {
        ModelBase model = ReflectUtils.forInstance(modelClass);

        if (EntityLiving.class.isAssignableFrom(entityClass))
            RenderingRegistry.registerEntityRenderingHandler(entityClass, renderManager -> new RenderEntityLivingModel(renderManager, model, shadowSize, (ITextureProvider) model));
        else
            RenderingRegistry.registerEntityRenderingHandler(entityClass, renderManager -> new RenderEntityModel(renderManager, model, (ITextureProvider) model));
    }

    @AnnotationInjector.StaticInvoke
    static void injectAnnotation(AnnotationStream<RegEntityModel> stream) {
        stream
                .typeStream()
                .filter(ITextureProvider.class::isAssignableFrom)
                .forEach(modelClass -> {
                    modelClass.getAnnotation(RegEntityModel.class);
                });
    }
}
