package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.CapabilityRegister;
import net.minecraftforge.common.capabilities.Capability;

import java.lang.annotation.*;

/**
 * Register {@code Capability} automatically.
 *
 * @author Lasm_Gratel
 * @since 1.0.3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(value = CapabilityRegister.class)
@Deprecated
public @interface RegCapability {
    Class<? extends Capability.IStorage> storageClass();
    Class<?> implClass();
}
