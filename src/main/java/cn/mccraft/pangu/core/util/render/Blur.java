package cn.mccraft.pangu.core.util.render;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.FieldAccessor;
import lombok.AllArgsConstructor;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
@SideOnly(Side.CLIENT)
public enum Blur {
    INSTANCE;

    public static final ResourceLocation SHADER_LOCATION = PanguResLoc.of("shaders/post/blur.json");
    public static final int DEFAULT_START_COLOR = -1072689136, DEFAULT_END_COLOR = -804253680;
    private Map<String, BlurData> blurDataMap = new HashMap<>();
    private boolean isActived = false;
    private FieldAccessor listShadersAccessor;
    private BlurData blurData;

    Blur() {
    }

    /**
     * @param guiScreen
     * @param second
     * @return
     */
    public static int getGuiBackgroundColor(GuiScreen guiScreen, boolean second) {
        if (!INSTANCE.isBlurGui(guiScreen)) return second ? DEFAULT_END_COLOR : DEFAULT_START_COLOR;
        return second ? INSTANCE.blurData.endColor : INSTANCE.blurData.startColor;
    }

    public boolean isBlurGui(GuiScreen guiScreen) {
        return guiScreen != null && (guiScreen.getClass().isAnnotationPresent(Gui.class) || blurDataMap.containsKey(guiScreen.getClass().getName()));
    }

    @SubscribeEvent
    public void onGuiChange(GuiOpenEvent event) throws Exception {
        if (listShadersAccessor == null) {
            listShadersAccessor = FastReflection.create(ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders"));
        }
        if (Minecraft.getMinecraft().world == null) {
            isActived = false;
            return;
        }
        EntityRenderer er = Minecraft.getMinecraft().entityRenderer;

        boolean include = false;

        BlurData blurData;
        if (event.getGui() != null) {
            if (event.getGui().getClass().isAnnotationPresent(Gui.class)) {
                include = true;
                Gui gui = event.getGui().getClass().getAnnotation(Gui.class);
                this.blurData = BlurData.fromGui(gui);
            } else if ((blurData = blurDataMap.get(event.getGui().getClass().getName())) != null) {
                include = true;
                this.blurData = blurData;
            }
        }

        if (!include) isActived = false;

        if (!er.isShaderActive() && include) {
            er.loadShader(SHADER_LOCATION);
            isActived = true;
            PanguCore.getLogger().debug("Enabled blur shader for gui " + event.getGui());
        } else if (er.isShaderActive() && !include) {
            PanguCore.getLogger().debug("Disabled blur shader for gui " + event.getGui());
            er.stopUseShader();
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
                        radius.set(this.blurData.radius);
                    }
                }
            } catch (Exception e) {
                PanguCore.getLogger().error("Couldn't solve updata shader.", e);
            }
        }
    }

    @SubscribeEvent
    public void onHUDRendering(RenderGameOverlayEvent.Pre event) {
        if (!isBlurGui(Minecraft.getMinecraft().currentScreen)) return;
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

    @AllArgsConstructor
    public static class BlurData {
        public static final BlurData DEFAULT = new BlurData(16, 0, 0);

        public final int radius, startColor, endColor;

        public static BlurData fromGui(Gui gui) {
            return new BlurData(gui.radius(), gui.startColor(), gui.endColor());
        }
    }
}
