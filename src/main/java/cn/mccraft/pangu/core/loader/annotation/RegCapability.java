package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.CapabilityRegister;
import net.minecraftforge.common.capabilities.Capability;

import java.lang.annotation.*;

/**
 * @author trychen
 * @since 1.0.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(value = CapabilityRegister.class)
public @interface RegCapability {
    Class<? extends Capability.IStorage> storageClass();
    Class<?> implClass();
}