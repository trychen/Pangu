package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.meta.Line;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Accessors(chain = true)
public class Article extends Scrolling {
    @Getter
    @Setter
    protected List<Line> lines;

    public Article(int width, int height) {
        super(width, height);
    }

    @Override
    public float getContentHeight() {
        return lines.size() * 12;
    }

    public Article addLine(Line... lines) {
        Collections.addAll(this.lines, lines);
        return this;
    }

    @Override
    public void onContentDraw(float ticks, float baseY, float mouseListX, float mouseListY) {
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.draw(getX(), getY() + i * 12, getContentWidth());
        }
    }
}
