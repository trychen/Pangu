package cn.mccraft.pangu.core.client.render.entity;

import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface EntityModelRegister {

    @AnnotationInjector.StaticInvoke
    static void injectAnnotation(AnnotationStream<RegEntityRender> stream) {
        stream
                .typeStream()
                .forEach(clazz -> {
                    RegEntityRender meta = clazz.getAnnotation(RegEntityRender.class);
                    if (ModelBase.class.isAssignableFrom(clazz) && ITextureProvider.class.isAssignableFrom(clazz)) {
                        ModelBase model = (ModelBase) ReflectUtils.forInstance(clazz);

                        if (EntityLiving.class.isAssignableFrom(meta.value()))
                            //noinspection unchecked
                            RenderingRegistry.registerEntityRenderingHandler((Class<? extends EntityLiving>) meta.value(), renderManager -> new RenderEntityLivingModel(renderManager, model, meta.shadowSize(), (ITextureProvider) model));
                        else
                            RenderingRegistry.registerEntityRenderingHandler(meta.value(), renderManager -> new RenderEntityModel(renderManager, model, (ITextureProvider) model));
                    } else if (Render.class.isAssignableFrom(clazz)) {
                        //noinspection unchecked
                        RenderingRegistry.registerEntityRenderingHandler(meta.value(), it -> (Render) ReflectUtils.forInstance(clazz, it));
                    }
                });
    }
}
