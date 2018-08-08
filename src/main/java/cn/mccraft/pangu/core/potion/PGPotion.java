package cn.mccraft.pangu.core.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

/**
 * @since 1.0.6
 * @author trychen
 */
public class PGPotion extends Potion {
    private ResourceLocation iconTexture;
    public PGPotion(boolean isBadEffect, int liquidColor) {
        super(isBadEffect, liquidColor);
    }

    public PGPotion setIcon(ResourceLocation iconTexture, int x, int y) {
        this.iconTexture = iconTexture;
        this.setIconIndex(x , y);
        return this;
    }

    @Override
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(iconTexture);
        return super.getStatusIconIndex();
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return false;
    }
}
