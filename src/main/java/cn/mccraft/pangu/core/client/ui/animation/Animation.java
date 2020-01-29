package cn.mccraft.pangu.core.client.ui.animation;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@RequiredArgsConstructor(staticName = "of")
@Accessors(chain = true, fluent = true)
public class Animation {
    @NonNull @Getter @Setter
    protected int duration;

    @Getter @Setter
    protected long startTime = -1;

    @Getter @Setter
    protected int delta;

    @Getter
    protected float progress;

    @Getter
    protected boolean ended = true;

    public void start() {
        if (this.startTime >= 0) return;
        this.ended = false;
        this.startTime = Minecraft.getSystemTime();
    }

    public void tick() {
        if (ended) return;
        if (startTime <= 0) return;
        this.delta = (int) (Minecraft.getSystemTime() - startTime);
        if (this.delta > duration) end();
        this.progress = MathHelper.clamp(delta / (float)duration, 0F, 1F);
    }

    public void end() {
        this.ended = true;
        this.startTime = -1;
    }

    public float climb(float start, float end) {
        if (start == end) return start;
        else if (end > start) return start + (end - start) * progress;
        else return end - (start - end) * progress;
    }

    public float climb(float value) {
        return value * progress;
    }

    public float climbWithSqrt(float value) {
        return (float) (value * Math.sqrt(progress));
    }

    public float climbWithSquare(float value) {
        return value * progress * progress;
    }

    public boolean started() {
        return this.startTime >= 0;
    }

    public void reset() {
        startTime = -1;
        progress = 0;
        ended = true;
    }
}
