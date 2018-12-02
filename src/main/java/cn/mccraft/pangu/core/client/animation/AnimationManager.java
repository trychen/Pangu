package cn.mccraft.pangu.core.client.animation;

import cn.mccraft.pangu.core.loader.AutoWired;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
@AutoWired(registerCommonEventBus = true)
public enum AnimationManager {
    INSTANCE;

    private Set<Animation> animations;

    public void start(Animation animation) {
        animation.setRunning(true);
        animations.add(animation);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {

    }
}
