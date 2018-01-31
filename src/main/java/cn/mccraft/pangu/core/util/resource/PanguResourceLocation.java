package cn.mccraft.pangu.core.util.resource;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraft.util.ResourceLocation;

/**
 * A resource location with the domain of {@link PanguCore#MODID}
 *
 * @since 1.0.0.2
 */
public class PanguResourceLocation extends ResourceLocation {
    public PanguResourceLocation(String resourcePathIn) {
        super(PanguCore.MODID, resourcePathIn);
    }
}
