package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.network.Remote;
import cn.mccraft.pangu.core.util.Sides;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

@Load
public interface RemoteTest {
    @Remote(value = 10, also = true)
    static void hello(Map<String, NBTTagCompound> q) {
        System.out.println(q.size());
        System.out.println(q.get("message").getInteger("Hello"));
    }

    @BindKeyPress(keyCode = Keyboard.KEY_O)
    static void hello() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Hello", 165);
        hello(new HashMap<String, NBTTagCompound>() {{
            put("message", nbt);
        }});
    }
}
