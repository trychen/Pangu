package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.client.ui.Modal;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Accessors(chain = true)
public class DialogModal extends Modal {

    @Getter
    @Setter
    protected List<String> text;

    @Getter
    @Setter
    protected List<Button> buttons;

    public DialogModal(Screen screen) {
        super(screen);
    }

    public DialogModal addButton(Button... buttons) {
        Collections.addAll(this.buttons, buttons);
        return this;
    }

    @Override
    public void drawBackground() {
        Rect.draw(0, 0, width, height, 0xBB000000);
        Rect.drawClassicalBackground(width / 2 - 128,height / 2 - 50, 256,100);
    }
}
