package cn.mccraft.pangu.core.client.input;

import net.minecraft.client.settings.KeyBinding;
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
}
