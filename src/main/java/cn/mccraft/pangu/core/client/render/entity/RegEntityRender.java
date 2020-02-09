package cn.mccraft.pangu.core.client.render.entity;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.annotation.*;

/**
 * Register model/render for entity.
 * The model should implement {@link ITextureProvider} to provide
 * any {@code ResourceLocation} for entity.
 *
 * @since 1.0.5
 * @author trychen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RegisteringHandler(EntityRenderRegister.class)
@SideOnly(Side.CLIENT)
public @interface RegEntityRender {
    Class<? extends Entity> value();

    /**
     *
     * @return
     */
    float shadowSize() default 1;
}
