package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Label extends Component {
    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private FontProvider fontProvider;

    public Label(String text) {
        this(text, DefaultFontProvider.INSTANCE);
    }

    public Label(String text, FontProvider fontProvider) {
        this.text = text;
        this.fontProvider = fontProvider;
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {

    }
}