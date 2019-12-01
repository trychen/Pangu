package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.Stack;

public class ScrollingStack extends Scrolling {
    private Stack stack;

    public ScrollingStack(Stack stack) {
        super(stack.getWidth(), stack.getHeight());
        stack.setParent(this);
        setShowScrollBar(false);
        this.stack = stack;
    }

    @Override
    public float getContentHeight() {
        return stack.getHeight();
    }


    @Override
    public void onContentPressed(int mouseButton, float mouseListX, float mouseListY) {
        stack.onMousePressed(mouseButton, (int) (mouseListX), (int) (mouseListY));
    }

    @Override
    public void onContentReleased(float mouseListX, float mouseListY) {
        stack.onMouseReleased((int) (mouseListX), (int) (mouseListY));
    }

    @Override
    public void onContentDraw(float ticks, float baseY, float mouseListX, float mouseListY) {
        stack.setOffsetX(getX());
        stack.setOffsetY(baseY);
        stack.onUpdate((int) mouseListX, (int) mouseListY);
        stack.onDraw(ticks,  (int) mouseListX,  (int) mouseListY);
    }

    @Override
    public Component setPosition(float x, float y) {
        stack.setPosition(x, y);
        return super.setPosition(x, y);
    }
}
