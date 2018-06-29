package cn.mccraft.pangu.core.util.resource;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraft.util.ResourceLocation;

/**
 * A resource location with the domain of {@link PanguCore#ID}
 *
 * @since 1.0.1
 */
public class PanguResourceLocation extends ResourceLocation {
    private PanguResourceLocation(String resourcePathIn) {
        super(PanguCore.ID, resourcePathIn);
    }

    public static PanguResourceLocation of(String resourcePathIn) {
        return new PanguResourceLocation(resourcePathIn);
    }
}
