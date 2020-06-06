package cn.mccraft.pangu.core.client.ui.style;

import cn.mccraft.pangu.core.client.ui.Component;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Getter
@Setter
public abstract class Style implements Cloneable {
    protected String unique;

    @SideOnly(Side.CLIENT)
    public void onPreDraw(Component c, float partialTicks, int mouseX, int mouseY) {
    }

    @SideOnly(Side.CLIENT)
    public void onPostDraw(Component c, float partialTicks, int mouseX, int mouseY) {
    }

    public void onMousePressed(Component c, int mouseButton, int mouseX, int mouseY) {
    }

    public void onMouseReleased(Component c, int mouseButton, int mouseX, int mouseY) {
    }

    @Override
    public Style clone() throws CloneNotSupportedException {
        return (Style) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (unique != null && obj instanceof Style && ((Style) obj).unique != null) return unique.equals(((Style) obj).unique);
        return getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        if (unique != null) return unique.hashCode();
        return getClass().hashCode();
    }
}
