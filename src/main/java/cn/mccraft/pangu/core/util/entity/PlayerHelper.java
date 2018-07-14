package cn.mccraft.pangu.core.util.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

/**
 * @author mouse
 * @since 1.0.4
 */
public interface PlayerHelper {
    static EntityPlayer getPlayerByUUID(UUID uuid) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
    }

    static EntityPlayer getPlayerByName(String name) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(name);
    }

    static GameProfile getOfflinePlayerByName(String name) {
        for(GameProfile player:FMLCommonHandler.instance().getMinecraftServerInstance().getServerStatusResponse().getPlayers().getPlayers())
            if(player.getName().equalsIgnoreCase(name))
                return player;
        return null;
    }

    static GameProfile getOfflinePlayerByUUID(UUID uuid) {
        for(GameProfile player:FMLCommonHandler.instance().getMinecraftServerInstance().getServerStatusResponse().getPlayers().getPlayers())
            if(player.getId().equals(uuid))
                return player;
        return null;
    }
}
