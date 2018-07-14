package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.IRegister;
import cn.mccraft.pangu.core.loader.annotation.RegCapability;
import net.minecraftforge.common.capabilities.Capability;

import java.lang.reflect.Field;

/**
 * @author trychen
 * @since 1.0.4
 */
public class CapabilityRegister implements IRegister<RegCapability, Capability> {
    @Override
    public void registerField(Field field, Capability capability, RegCapability regCapability, String domain) {
        // TODO:
    }
}
