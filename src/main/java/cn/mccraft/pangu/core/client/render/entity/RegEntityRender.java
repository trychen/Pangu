package cn.mccraft.pangu.core.client.render.entity;

import net.minecraft.entity.Entity;

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
public @interface RegEntityRender {
    Class<? extends Entity> value();

    /**
     *
     * @return
     */
    float shadowSize() default 1;
}
