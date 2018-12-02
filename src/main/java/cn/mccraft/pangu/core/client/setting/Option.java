package cn.mccraft.pangu.core.client.setting;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Option<T> {
    private Settings settings;
    private int ordinal;
    private final String translation;

    public Option(String translation) {
        this.translation = translation;
    }

    public int getOrdinal() {
        return this.ordinal;
    }

    public Option setOrdinal(int ordinal) {
        this.ordinal = ordinal;
        return this;
    }

    public Option setSettings(Settings settings) {
        this.settings = settings;
        return this;
    }

    public Settings getSettings() {
        return settings;
    }

    public String getTranslation() {
        return I18n.format(this.translation);
    }

    private Supplier<T> lamdbaGet;
    private Consumer<T> lamdbaSet;

    public abstract String getDisplayString();
    public abstract GuiButton createButton(int x, int y);
    public T getValue() {
        return lamdbaGet.get();
    }
    public void setValue(T object) {
        lamdbaSet.accept(object);
    }
}
