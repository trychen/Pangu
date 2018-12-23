package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.loader.AutoWired;
import net.minecraft.entity.player.EntityPlayer;

@AutoWired
public class RemoteTester {
    @Remote(value = 5, sync = false)
    public static void test(EntityPlayer player, String hello) {
    }
}
