package cn.mccraft.pangu.core.loader.annotation;

import net.minecraftforge.common.capabilities.Capability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RegCapability {
    Class<? extends Capability.IStorage> storageClass();
    Class<?> implClass();
}
