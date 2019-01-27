package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.AnnotationRegister;
import cn.mccraft.pangu.core.loader.annotation.RegCapability;
import net.minecraftforge.common.capabilities.Capability;

import java.lang.reflect.Field;

/**
 * @author trychen
 * @since 1.0.4
 */
@Deprecated
public class CapabilityRegister implements AnnotationRegister<RegCapability, Capability> {
    @Override
    public void registerField(Field field, Capability capability, RegCapability regCapability, String domain) {
        // TODO:
    }
}
