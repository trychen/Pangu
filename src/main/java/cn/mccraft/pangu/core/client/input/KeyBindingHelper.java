package cn.mccraft.pangu.core.client.input;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface KeyBindingHelper {
    String CATEGORY_MOVEMENT = "key.categories.movement";
    String CATEGORY_INVENTORY = "key.categories.inventory";
    String CATEGORY_GAMEPLAY = "key.categories.gameplay";
    String CATEGORY_MULTIPLAYER = "key.categories.multiplayer";
    String CATEGORY_MISC = "key.categories.misc";
    String CATEGORY_CREATIVE = "key.categories.creative";

    static KeyBinding of(String description, int keyCode, String category) {
        final KeyBinding keyBinding = new KeyBinding(description, keyCode, category);
        ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }
    static KeyBinding of(String description, int keyCode, String category, KeyModifier keyModifier) {
        final KeyBinding keyBinding = new KeyBinding(description, new IKeyConflictContext() {
            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public boolean conflicts(IKeyConflictContext other) {
                return false;
            }
        }, keyModifier, keyCode, category);
        ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }
}
