package cn.mccraft.pangu.core.client.input;

import cn.mccraft.pangu.core.PanguCore;
import com.github.mouse0w0.fastreflection.MethodAccessor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * A data class for bound key
 */
@SideOnly(Side.CLIENT)
public class CachedKeyBinder {
    private final KeyBinding keyBinding;
    private final MethodAccessor method;
    private final Object instance;
    private final BindKeyPress meta;

    public CachedKeyBinder(KeyBinding keyBinding, MethodAccessor method, Object instance, BindKeyPress meta) {
        this.keyBinding = keyBinding;
        this.method = method;
        this.instance = instance;
        this.meta = meta;
    }

    /**
     * @return whether responding in game
     */
    public boolean enableInGame() {
        return meta.enableInGame();
    }

    /**
     * @return whether responding in GUI
     */
    public boolean enableInGUI() {
        return meta.enableInGUI();
    }

    /**
     * Check key if pressed and then invoke method
     */
    public void solve() {
        if ((keyBinding.isPressed() || Keyboard.isKeyDown(keyBinding.getKeyCode())) && keyBinding.getKeyModifier().isActive()) try {
            method.invoke(instance);
        } catch (Exception e) {
            // catch all exception
            PanguCore.getLogger().error("Unable to bind key input for " + keyBinding.getKeyDescription(), e);
        }
    }
}
