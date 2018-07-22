package cn.mccraft.pangu.core.client.input;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.gui.GuiTest;
import cn.mccraft.pangu.core.client.gui.PanguToast;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

@SideOnly(Side.CLIENT)
@AutoWired(registerCommonEventBus = true)
public class KeyBindingInjector {
    private Map<KeyBinding, Method> bindingInGameKeys = Maps.newHashMap();
    private Map<KeyBinding, Method> bindingInGuiKeys = Maps.newHashMap();

    @AnnotationInjector.StaticInvoke
    public void bind(AnnotationStream<BindKeyPress> stream) {
        stream.methodStream()
                .filter(method -> method.getParameterCount() == 0)
                .forEach(method -> {
                    // check if there is an instance to invoke
                    if (!Modifier.isStatic(method.getModifiers()) && InstanceHolder.getCachedInstance(method.getDeclaringClass()) == null) {
                        PanguCore.getLogger().error("Unable to find any instance to bind key for method " + method.toString(), new NullPointerException());
                    }
                    // get annotation info
                    BindKeyPress bindKeyPress = method.getAnnotation(BindKeyPress.class);
                    // register key binding
                    KeyBinding key = KeyBindingHelper.of(bindKeyPress.description(), bindKeyPress.keyCode(), bindKeyPress.category());
                    // put into cache
                    if (bindKeyPress.enableInGame())
                        bindingInGameKeys.put(key, method);

                    if (bindKeyPress.enableInGUI())
                        bindingInGuiKeys.put(key, method);
                });

    }

    /**
     * Key pressed in GUI
     */
    @SubscribeEvent
    public void handleKey(GuiScreenEvent.KeyboardInputEvent.Pre e) {
        handleKeyPress(bindingInGuiKeys);
    }

    /**
     * Key pressed in game
     */
    @SubscribeEvent
    public void handleKey(InputEvent.KeyInputEvent e) {
        handleKeyPress(bindingInGuiKeys);
    }

    public void handleKeyPress(Map<KeyBinding, Method> keyMap) {
        for (Map.Entry<KeyBinding, Method> entry : keyMap.entrySet())
            // check if pressed
            if (entry.getKey().isPressed() || Keyboard.isKeyDown(entry.getKey().getKeyCode())) try {
                entry.getValue().invoke(InstanceHolder.getCachedInstance(entry.getValue().getDeclaringClass()));
            } catch (Exception e) {
                // catch all exception
                PanguCore.getLogger().error("Unable to bind key input for " + entry.getKey().getKeyDescription(), e);
            }
    }

    @DevOnly
    @BindKeyPress(description = "key.example", keyCode = Keyboard.KEY_O, category = KeyBindingHelper.CATEGORY_CREATIVE)
    public void onKeyLPressed() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTest());
        System.out.println("Key L Pressed");
    }
}
