package cn.mccraft.pangu.core.client.setting;

import net.minecraft.client.gui.GuiButton;

public class BooleanOption extends Option<Boolean> {
    public BooleanOption(String translation) {
        super(translation);
    }

    @Override
    public String getDisplayString() {
        return null;
    }

    @Override
    public GuiButton createButton(int x, int y) {
        return null;
    }

    @Override
    public Boolean getValue() {
        return true;
    }

    @Override
    public void setValue(Boolean object) {
    }
}
