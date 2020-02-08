package cn.mccraft.pangu.core.client.ui.animation;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
@Accessors(chain = true)
public class ConfirmProgressBar {
    private boolean started = false;
    @Getter
    private float progress;
    @Getter
    private long pressedTime, unpressedTime;
    @Getter
    private int unpressedDelta;

    private boolean done = false;

    @Getter
    @Setter
    @NonNull
    private int key, duration;

    @Getter
    @Setter
    private Runnable confirm;

    @Getter
    @Setter
    private Callable<Boolean> shouldGo;

    public void tick() {
        if (done) return;
        if (unpressedTime != 0){
            long delta = Minecraft.getSystemTime() - unpressedTime;
            float progress = MathHelper.clamp(delta / (float) unpressedDelta, 0, 1);
            float whole = MathHelper.clamp(unpressedDelta / (float) duration, 0, 1);
            if (progress == 1) {
                pressedTime = 0;
                unpressedTime = 0;
                this.progress = 0;
            } else {
                this.progress = (1 - progress) * whole;
                if (progress == 1 && confirm != null) {
                    done = true;
                    confirm.run();
                }
            }
        } else if (pressedTime != 0) {
            try {
                if (!Keyboard.isKeyDown(key) || !shouldGo.call()) {
                    started = false;
                    unpressedTime = Minecraft.getSystemTime();
                    unpressedDelta = (int) (unpressedTime - pressedTime);
                } else {
                    long delta = Minecraft.getSystemTime() - pressedTime;
                    this.progress = MathHelper.clamp(delta / (float) duration, 0, 1);
                    if (progress == 1 && confirm != null) {
                        done = true;
                        confirm.run();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void key() {
        try {
            if (Keyboard.isKeyDown(key) && shouldGo.call()) {
                if (started) return;
                started = true;
                if (pressedTime != 0) {
                    long delta = Minecraft.getSystemTime() - unpressedTime;
                    float progress = MathHelper.clamp(delta / (float) unpressedDelta, 0, 1);
                    pressedTime = Minecraft.getSystemTime() - (long) (unpressedDelta * (1 - progress));
                    unpressedTime = 0;
                } else {
                    pressedTime = Minecraft.getSystemTime();
                    progress = 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        started = false;
        pressedTime = 0;
        unpressedDelta = 0;
        unpressedTime = 0;
        done = false;
        progress = 0;
    }

}
