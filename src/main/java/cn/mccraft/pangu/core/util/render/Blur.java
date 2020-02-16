package cn.mccraft.pangu.core.util.render;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供模糊效果
 *
 * @since 1.2.1.1
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class Blur {

    public static final ResourceLocation SHADER_LOCATION = PanguResLoc.of("shaders/post/fade_in_blur.json");
    public static final int DEFAULT_START_COLOR = 0xC0101010, DEFAULT_END_COLOR = 0xD0101010;

    @Getter
    private static Map<String, BlurData> blurDataMap = new HashMap<>();

    static {
        blurDataMap.put(GuiInventory.class.getName(), BlurData.DEFAULT);
    }

    @Getter
    private static BlurData currentBlurData;

    @Getter
    private static boolean active = false;
    private static Field _listShaders;

    private static long start;


    /**
     * @param guiScreen
     * @param second
     * @return
     */
    public static int getGuiBackgroundColor(GuiScreen guiScreen, boolean second) {
        if (!active || currentBlurData == null) return second ? DEFAULT_END_COLOR : DEFAULT_START_COLOR;
        return second ? currentBlurData.endColor : currentBlurData.startColor;
    }

    public static boolean isBlurGui(GuiScreen guiScreen) {
        return guiScreen != null && (guiScreen.getClass().isAnnotationPresent(Gui.class) || blurDataMap.containsKey(guiScreen.getClass().getName()));
    }

    @SubscribeEvent
    public static void onGuiChange(GuiOpenEvent event) throws Exception {
        if (_listShaders == null) {
            _listShaders = ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders");
        }
        EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
        if (er == null) return;
        if (Minecraft.getMinecraft().world == null) {
            if (active && er.isShaderActive()) {
                er.stopUseShader();
            }
            active = false;
            return;
        }

        boolean include = false;

        BlurData blurData;
        if (event.getGui() != null) {
            if (event.getGui().getClass().isAnnotationPresent(Gui.class)) {
                include = true;
                Gui gui = event.getGui().getClass().getAnnotation(Gui.class);
                currentBlurData = BlurData.fromGui(gui);
            } else if ((blurData = blurDataMap.get(event.getGui().getClass().getName())) != null) {
                include = true;
                currentBlurData = blurData;
            }
        }

        if (!include) active = false;

        if (!er.isShaderActive() && include) {
            active = true;
            start = System.currentTimeMillis();
            er.loadShader(SHADER_LOCATION);
            PanguCore.getLogger().debug("Enabled blur shader for gui " + event.getGui());
        } else if (er.isShaderActive() && !include) {
            er.stopUseShader();
            PanguCore.getLogger().debug("Disabled blur shader for gui " + event.getGui());
        }
    }

    private static float getProgress() {
        return Math.min((System.currentTimeMillis() - start) / (float) currentBlurData.fadeTime, 1);
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().currentScreen != null && active) {
            EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
//            if (!er.isShaderActive()) {
//                er.loadShader(SHADER_LOCATION);
//            }
            if (er.getShaderGroup() == null) return;
            try {
                float progress = getProgress();
                if (progress == 1) return;
                @SuppressWarnings("unchecked")
                List<Shader> shaders = (List<Shader>) _listShaders.get(er.getShaderGroup());
                for (Shader s : shaders) {
                    ShaderUniform su = s.getShaderManager().getShaderUniform("Progress");
                    if (su != null) {
                        su.set(progress);
                    }
                }
            } catch (Exception e) {
                PanguCore.getLogger().error("Error while init Blur", e);
                active = false;
                er.stopUseShader();
            }
        }
    }

//    @SubscribeEvent(receiveCanceled = true)
//    public static void render(GuiScreenEvent.DrawScreenEvent.Post event) {
//        if (active) {
//            EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
//            er.stopUseShader();
//        }
//    }

    public static void register(Class<? extends GuiScreen> gui, BlurData data) {
        blurDataMap.put(gui.getName(), data);
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Gui {
        @Deprecated
        int radius() default 0;

        int startColor() default 0x2CFFFFFF;
        int endColor() default 0x2CFFFFFF;

        int fadeTime() default 200;
    }

    @Data
    @Accessors(chain = true)
    public static class BlurData {
        public static final BlurData DEFAULT = new BlurData(0x1C000000, 0x1C000000).setFadeTime(200);

        @Deprecated
        public int radius;
        public final int startColor, endColor;
        public int fadeTime = 200;

        public BlurData(int startColor, int endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
        }

        public BlurData(int radius, int startColor, int endColor) {
            this.radius = radius;
            this.startColor = startColor;
            this.endColor = endColor;
        }


        public static BlurData fromGui(Gui gui) {
            return new BlurData(0, gui.startColor(), gui.endColor()).setFadeTime(gui.fadeTime());
        }
    }
}
