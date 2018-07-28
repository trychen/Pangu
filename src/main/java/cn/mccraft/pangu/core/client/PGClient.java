package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;

public interface PGClient {
    ResourceLocation PG_BUTTONS_TEXTURE = PanguResLoc.ofGui("buttons.png");
    ResourceLocation PG_ICONS_TEXTURE = PanguResLoc.ofGui("icons.png");
    ResourceLocation PG_TOOLTIPS_TEXTURE = PanguResLoc.ofGui("tooltips.png");

    static IThreadListener getThread() {
        return Minecraft.getMinecraft();
    }
}
