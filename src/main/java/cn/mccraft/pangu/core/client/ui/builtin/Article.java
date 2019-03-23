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
    public int getContentHeight() {
        return lines.size() * 12;
    }

    @Override
    public void onContentClick(float mouseListX, float mouseListY) {

    }

    public Article addLine(Line... lines) {
        Collections.addAll(this.lines, lines);
        return this;
    }

    @Override
    public void onContentDraw(float baseY) {
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.draw(getX(), getY() + i * 12, getContentWidth());
        }
    }
}
