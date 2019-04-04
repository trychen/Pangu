package cn.mccraft.pangu.core.client.toast;

import cn.mccraft.pangu.core.network.Bridge;
import cn.mccraft.pangu.core.util.data.ByteStreamPersistence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Toasts {
    @SideOnly(Side.CLIENT)
    static void add(IToast iToast) {
        Minecraft.getMinecraft().getToastGui().add(iToast);
    }

    @SideOnly(Side.CLIENT)
    static PanguToast get(String key) {
        return Minecraft.getMinecraft().getToastGui().getToast(PanguToast.class, key);
    }

    @Bridge(value = "PanguToast.Add", side = Side.CLIENT, persistence = ByteStreamPersistence.class)
    static void add(@Nullable EntityPlayer player, @Nonnull ToastData info) {
        add(new PanguToast(info));
    }

    @Bridge(value = "PanguToast.Update", side = Side.CLIENT, persistence = ByteStreamPersistence.class)
    static void update(@Nullable EntityPlayer player, ToastData info, boolean addIfNotExist) {
        PanguToast toast = Minecraft.getMinecraft().getToastGui().getToast(PanguToast.class, info.getKey());
        if (toast != null) {
            toast.setInfo(info);
        } else if (addIfNotExist) {
            add(player, info);
        }
    }

    @Bridge(value = "PanguToast.Remove", side = Side.CLIENT)
    static void remove(@Nullable EntityPlayer player, String key) {
        PanguToast toast = Minecraft.getMinecraft().getToastGui().getToast(PanguToast.class, key);
        if (toast != null) toast.hide();
    }

    @Bridge(value = "PanguToast.UpdateProgress", side = Side.CLIENT)
    static void updateProgress(@Nullable EntityPlayer player, String key, float progress) {
        PanguToast toast = Minecraft.getMinecraft().getToastGui().getToast(PanguToast.class, key);
        if (toast != null) toast.setCurrentProgress(progress);
    }

    @Bridge(value = "Toast.Clear", side = Side.CLIENT)
    static void clear(@Nullable EntityPlayer player, String key) {
        PanguToast toast = Minecraft.getMinecraft().getToastGui().getToast(PanguToast.class, key);
        if (toast != null) toast.hide();
    }
}
