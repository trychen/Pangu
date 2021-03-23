package cn.mccraft.pangu.core.client.ui;

import javax.annotation.Nonnull;

public class CursorHolder {
    protected String type;
    protected Object object;

    public boolean isPresent() {
        return object != null;
    }

    public String getType() {
        return type;
    }

    public boolean check(String type) {
        return isPresent() && type.equals(this.type);
    }

    public Object peek(String type) {
        return isPresent() && type.equals(this.type) ? object : null;
    }

    public Object peek() {
        return object;
    }

    public Object pop() {
        Object obj = object;
        clear();
        return obj;
    }

    public Object push(@Nonnull String type, @Nonnull Object object) {
        Object old = this.object;
        this.type = type;
        this.object = object;
        return old;
    }

    public void clear() {
        object = null;
        type = null;
    }
}
