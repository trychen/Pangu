package cn.mccraft.pangu.core.client.setting;

import net.minecraft.client.gui.GuiButton;

public class GuiOptionButton extends GuiButton {
    private Option option;
    public GuiOptionButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, null, buttonText);
    }

    public GuiOptionButton(int buttonId, int x, int y, Option option, String buttonText) {
        super(buttonId, x, y, 150, 20, buttonText);
        this.option = option;
    }

    public Option getOption() {
        return option;
    }
}