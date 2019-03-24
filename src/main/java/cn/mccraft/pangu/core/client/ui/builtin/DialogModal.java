package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.*;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Accessors(chain = true)
public class DialogModal extends Modal {

    @Getter
    @Setter
    protected List<String> text = new ArrayList<>();

    @Getter
    @Setter
    protected List<Button> buttons = new ArrayList<>();

    public DialogModal(Screen screen) {
        super(screen);
    }

    @Override
    public void init() {
        text = UI.resizeStringList(text, 224);

        float contentWidth = getMinWidth();
        float wholeHeight = 16 + text.size() * 12;
        if (buttons.isEmpty()) return; else wholeHeight += 24;

        float allY = (height - wholeHeight) / 2 + wholeHeight - 26;
        float startX = (width + contentWidth) / 2 - 5;
        for (int i = buttons.size() - 1; i >= 0; i--) {
            Button button = buttons.get(i);
            startX -= button.getWidth();
            addComponent(button.setPosition(startX, allY));
            startX -= 5;
        }
    }

    public DialogModal addText(String... lines) {
        Collections.addAll(this.text, lines);
        return this;
    }

    public DialogModal addButton(Button... buttons) {
        Collections.addAll(this.buttons, buttons);
        return this;
    }

    @Override
    public void drawBackground() {
        float contentWidth = getMinWidth();
        float wholeHeight = 16 + text.size() * 12;
        if (!buttons.isEmpty()) wholeHeight += 24;

        float xStart = (width - contentWidth) / 2;

        Rect.draw(0, 0, width, height, 0xBB000000);

        Rect.drawClassicalBackground(xStart,(height - wholeHeight) / 2, contentWidth, wholeHeight);

        float startY = height / 2 - wholeHeight / 2 + 8;
        for (int i = 0; i < text.size(); i++) {
            DefaultFontProvider.INSTANCE.drawString(text.get(i), xStart + 8, startY + i * 12, 0x000000, false);
        }
    }

    public float getMinWidth() {
        int textMax = text.stream().mapToInt(DefaultFontProvider.INSTANCE::getStringWidth).max().getAsInt();
        double buttonMax = buttons.stream().mapToDouble(Component::getWidth).sum() + (buttons.size() - 1) * 5;
        return (float) Math.max(Math.max(textMax, buttonMax) + 16, 120);
    }

    @Override
    public void close() {
        playPressSound();
    }
}
