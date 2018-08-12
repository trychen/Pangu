package cn.mccraft.pangu.core.client.input;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.network.KeyMessage;
import com.github.mouse0w0.fastreflection.FastReflection;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
@AutoWired(registerCommonEventBus = true)
public class KeyBindingInjector {
    private List<CachedKeyBinder> keyBindedList = new ArrayList<>();

    /**
     * Cache all key binding method for
     */
    @AnnotationInjector.StaticInvoke
    public void bind(AnnotationStream<BindKeyPress> stream) {
        stream.methodStream()
                .filter(method -> method.getParameterCount() == 0)
                .forEach(method -> {
                    // check if there is an instance to invoke
                    Object instance = null;
                    if (!Modifier.isStatic(method.getModifiers()) && (instance = InstanceHolder.getCachedInstance(method.getDeclaringClass())) == null) {
                        PanguCore.getLogger().error("Unable to find any instance to bind key for method " + method.toString(), new NullPointerException());
                        return;
                    }
                    // get annotation info
                    final BindKeyPress bindKeyPress = method.getAnnotation(BindKeyPress.class);
                    // register key binding
                    final KeyBinding key = KeyBindingHelper.of(bindKeyPress.description(), bindKeyPress.keyCode(), bindKeyPress.category());
                    // put into cache
                    try {
                        keyBindedList.add(new CachedKeyBinder(key , FastReflection.create(method), instance, bindKeyPress));
                    } catch (Exception e) {
                        PanguCore.getLogger().error("Unexpected error while creating method reflection", e);
                    }
                });
    }

    /**
     * Content pressed in GUI
     */
    @SubscribeEvent
    public void handleKey(GuiScreenEvent.KeyboardInputEvent.Pre e) {
        keyBindedList.stream().filter(CachedKeyBinder::enableInGUI).forEach(CachedKeyBinder::solve);
    }

    /**
     * Content pressed in game
     */
    @SubscribeEvent
    public void handleKey(InputEvent.KeyInputEvent e) {
        keyBindedList.stream().filter(CachedKeyBinder::enableInGame).forEach(CachedKeyBinder::solve);
    }
}
