package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.meta.Icon;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.client.PGClient.PG_ICONS_TEXTURE;

@SideOnly(Side.CLIENT)
public interface Icons {
    Icon NONE = Icon.builder().texture(PG_ICONS_TEXTURE).width(0).height(0).build();
    Icon DOT_GREEN = Icon.builder().texture(PG_ICONS_TEXTURE).build();
    Icon POINTER_RIGHT = Icon.builder().texture(PG_ICONS_TEXTURE).v(11).build();
}
