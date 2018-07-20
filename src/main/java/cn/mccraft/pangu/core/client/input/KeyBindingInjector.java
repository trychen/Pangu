package cn.mccraft.pangu.core.client.input;

import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.AutoWired;
import com.google.common.collect.Maps;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Map;

@SideOnly(Side.CLIENT)
@AutoWired(registerCommonEventBus = true)
public class KeyBindingInjector {
    private Map<String, KeyBinding> bindingKeys = Maps.newHashMap();

    @AnnotationInjector.StaticInvoke
    public void bind(AnnotationStream<BindKeyPress> stream) {
        System.out.println("Bind");
        stream.methodStream().forEach(System.out::println);

    }

    @BindKeyPress(description = "key.test", keyCode = Keyboard.KEY_O, category = KeyBindingHelper.CATEGORY_MISC)
    public void test(AnnotationStream<BindKeyPress> stream) {

    }

    @SubscribeEvent
    public void onEvent(InputEvent.KeyInputEvent event) {
        bindingKeys.values().stream().filter(KeyBinding::isPressed).forEach(key -> {

        });
    }
}
