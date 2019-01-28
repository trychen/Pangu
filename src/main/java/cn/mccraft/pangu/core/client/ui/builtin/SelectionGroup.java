package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SelectionGroup {
    private String name;
    private Type type;
    private Set<Component> components = new HashSet<>();

    public enum Type {
        RADIO,
        CHECKBOX
    }
}
