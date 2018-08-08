package cn.mccraft.pangu.core.client.render.entity;

import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public interface EntityModelRegister {

    @SuppressWarnings("unchecked")
    @AnnotationInjector.StaticInvoke
    static void injectAnnotation(AnnotationStream<RegEntityModel> stream) {
        stream
                .typeStream()
                .filter(ModelBase.class::isAssignableFrom)
                .filter(ITextureProvider.class::isAssignableFrom)
                .forEach(modelClass -> {
                    RegEntityModel meta = modelClass.getAnnotation(RegEntityModel.class);
                    ModelBase model = (ModelBase) ReflectUtils.forInstance(modelClass);

                    if (EntityLiving.class.isAssignableFrom(meta.value()))
                        RenderingRegistry.registerEntityRenderingHandler(meta.value(), renderManager -> new RenderEntityLivingModel(renderManager, model, meta.shadowSize(), (ITextureProvider) model));
                    else
                        RenderingRegistry.registerEntityRenderingHandler(meta.value(), renderManager -> new RenderEntityModel(renderManager, model, (ITextureProvider) model));
                });
    }
    
}
