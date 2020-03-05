package cn.mccraft.pangu.core;

import lombok.Getter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The mod class of Pangu Core. this class can only use to
 * pack some static method/field/description or fml event.
 *
 * @author trychen
 * @since 1.0.0.0
 */
@Mod(
        modid = PanguCore.ID,
        name = "Pangu Core",
        version = "1.19.8",
        useMetadata = true,
        acceptedMinecraftVersions = "[1.12.2,1.13)"
)
public class PanguCore {
    /*
       ==============================
                 Base Info
       ==============================
     */

    /**
     * The mod id for child mod needn't any prefix or suffix like "pangu-", "pangu_" or "pangu".
     * but if you want so, you can go you way
     *
     * 子Mod的ID不需要有任何特殊的前缀或后缀，但是你也可以加上 "pangu-", "pangu_" 或 "pangu"。
     *
     * @since 1.0.0.1
     */
    public static final String ID = "pangu";

    /**
     * "Pan Gu created heaven and earth" is a Chinese fairy tale which means that the creation of china history,
     * so we use "Pangu' as the name of this mod, to represent the chinese history which is long as 5k years
     *
     * Every Mod based on Pangu Core should has the prefix "Pangu ", such as,
     * <ul>
     *     <li>"Pangu Workshop" for chinese building, block</li>
     *     <li>"Pangu Tools" for chinese weapon, armor</li>
     *     <li>"Pangu Creative" for chinese craft and some way of playing sth</li>
     *     <li>so on....</li>
     * </ul>
     *
     * 每一个子Mod的名字都应该包含前缀 "Pangu "
     *
     * @since 1.0.0.1
     */
    public static final String NAME = "Pangu Core";

    /**
     * The version number means BIG_VERSION.SMALL_VERSION.BUILD_VERSION.COMMIT_VERSION,
     * the COMMIT_VERSION should be hidden while the version is appearing to player.
     * <p>
     * For child mod, it can be whatever you want. But you'd better make sure you mod can run in this version of pangu
     *
     * 版本号构成： "大版本号.小版本号.构建版本.Commit版本"， 对于玩家可见的的版本号只不需要包含Commit版本
     *
     * @since 1.0.0.1
     */
    public static final String VERSION = "@MOD_VERSION@";

    /*
       ==============================
                  Instance
       ==============================
     */

    /**
     * The instance of Pangu Core, it must be private to prevent others mod change it!
     *
     * @since 1.0.0.2
     */
    @Mod.Instance(PanguCore.ID) @Getter
    private static PanguCore instance;

    /**
     *  ==============================
     *              Logger
     *  ==============================
     */

    /**
     * Log4j's logger of "Pangu Core",
     */
    private static final Logger LOGGER = LogManager.getLogger("Pangu");

    /**
     * get the logger of "Pangu Core"
     *
     * @return log4j's logger
     * @since 1.0.0.2
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /*
       ==============================
                   Proxy
       ==============================
     */

    /**
     * Proxy should be only visible to mod main class
     *
     * @since 1.0.0.2
     */
    @SidedProxy(
            clientSide = "cn.mccraft.pangu.core.client.ClientProxy",
            serverSide = "cn.mccraft.pangu.core.CommonProxy"
    )
    @Getter
    private static CommonProxy proxy;
    /*
       ==============================
                   Network
       ==============================
     */

    /**
     * This SimpleNetworkWrapper is shared to all child mod,
     * and it will be init while {@link SimpleNetworkWrapper}.
     * The name of this channel is "pangu" which is the mod id of Pangu Core
     *
     * @since 1.0.0.2
     */
    @Getter
    private static SimpleNetworkWrapper network;

    /**
     *  ==============================
     *        FML Initialization
     *  ==============================
     */

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        proxy.construction(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Init the network
        network = NetworkRegistry.INSTANCE.newSimpleChannel(ID);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        proxy.loadComplete(event);
    }


    /*
       ==========================================
          Configuration (Not exist currently)
       ==========================================

       Pangu Core won't provide a configuration file
     */
}
