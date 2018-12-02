package cn.mccraft.pangu.core.client.setting;

import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    protected List<Option> options = new ArrayList<>();

    public Settings() {
    }

    public Settings add(Option option) {
        options.add(option.setOrdinal(options.size()).setSettings(this));
        return this;
    }

    public Option getOption(int id) {
        return options.get(id);
    }

    public int size() {
        return options.size();
    }

    public List<Option> getOptions() {
        return options;
    }

    /**
     * Get the name of settings
     */
    public String getTranslateKey() {
        return "option.Settings";
    }

    /**
     * Returns the translation of the given index in the given String array. If the index is smaller than 0 or greater
     * than/equal to the length of the String array, it is changed to 0.
     */
    public static String getTranslation(String[] strArray, int index) {
        if (index < 0 || index >= strArray.length) {
            index = 0;
        }
        return I18n.format(strArray[index]);
    }
}
