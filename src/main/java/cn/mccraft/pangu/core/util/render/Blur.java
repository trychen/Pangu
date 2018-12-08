package cn.mccraft.pangu.core.util.render;

import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.FieldAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供模糊效果
 *
 * @since 1.2.1.1
 */
@AutoWired(registerCommonEventBus = true)
public enum Blur {
    INSTANCE;

    public static final ResourceLocation SHADER_LOCATION = PanguResLoc.of("shaders/post/blur.json");

    private Map<String, BlurData> blurDataMap = new HashMap<>();
    private boolean isActived;
    private FieldAccessor listShadersAccessor;
    private int radius;
    private int startColor = -1072689136, endColor = -804253680;

    public static int getGuiBackgroundColor(GuiScreen guiScreen, boolean second) {
        if (INSTANCE.isBlurGui(guiScreen)) return second ? -804253680 : -1072689136;
        return second ? INSTANCE.endColor : INSTANCE.startColor;
    }

    public boolean isBlurGui(GuiScreen guiScreen) {
        return !guiScreen.getClass().isAnnotationPresent(Gui.class) && blurDataMap.get(guiScreen.getClass().getName()) == null;
    }

    @SubscribeEvent
    public void onGuiChange(GuiOpenEvent event) throws Exception {
        if (listShadersAccessor == null) {
            listShadersAccessor = FastReflection.create(ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders"));
        }
        if (Minecraft.getMinecraft().world == null) return;
        EntityRenderer er = Minecraft.getMinecraft().entityRenderer;

        boolean include = false;

        BlurData blurData;
        if (event.getGui() != null) {
            if (event.getGui().getClass().isAnnotationPresent(Gui.class)) {
                include = true;
                Gui gui = event.getGui().getClass().getAnnotation(Gui.class);
                radius = gui.radius();
                startColor = gui.startColor();
                endColor = gui.endColor();
            } else if ((blurData = blurDataMap.get(event.getGui().getClass().getName())) != null) {
                include = true;
                radius = blurData.getRadius();
                startColor = blurData.getStartColor();
                endColor = blurData.getEndColor();
            }
        }

        if (!er.isShaderActive() && include) {
            er.loadShader(SHADER_LOCATION);
            isActived = true;
        } else if (er.isShaderActive() && !include) {
            er.stopUseShader();
            isActived = false;
        }

    }

    public Map<String, BlurData> getBlurDataMap() {
        return blurDataMap;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
            ShaderGroup sg = Minecraft.getMinecraft().entityRenderer.getShaderGroup();
            try {
                List<Shader> shaders = (List<Shader>) listShadersAccessor.get(sg);
                for (Shader s : shaders) {
                    ShaderUniform radius = s.getShaderManager().getShaderUniform("Radius");
                    if (radius != null) {
                        radius.set(this.radius);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onHUDRendering(RenderGameOverlayEvent.Pre event) {
        if (!isActived) return;
        event.setCanceled(true);
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Gui {
        int radius() default 16;

        int startColor() default 0x00000000;

        int endColor() default 0x00000000;
    }

    public class BlurData {
        private int radius = 16, startColor = 0x00000000, endColor = 0x00000000;

        public BlurData(int radius, int startColor, int endColor) {
            this.radius = radius;
            this.startColor = startColor;
            this.endColor = endColor;
        }
        public int getRadius() {
            return radius;
        }

        public int getStartColor() {
            return startColor;
        }

        public int getEndColor() {
            return endColor;
        }
    }
}
