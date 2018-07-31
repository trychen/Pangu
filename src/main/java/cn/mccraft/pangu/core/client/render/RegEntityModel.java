package cn.mccraft.pangu.core.client.render;

import net.minecraft.entity.Entity;

import java.lang.annotation.*;

/**
 * Register model for entity with simple {@code Render<T>}.
 * The model should implement {@link ITextureProvider} to provide
 * any {@code ResourceLocation} for entity.
 *
 * @since 1.0.5
 * @author trychen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegEntityModel {
    Class<? extends Entity> value();
    float shadowSize() default 1;
}
