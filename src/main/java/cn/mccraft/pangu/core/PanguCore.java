package cn.mccraft.pangu.core;

import net.minecraftforge.fml.common.Mod;

/**
 * The mod class of Pangu Core. this class can only use to
 * pack some static method/field, or to description mod info.
 *
 * Created by trychen on 17/8/7.
 */
@Mod(modid = PanguCore.MODID, name = PanguCore.NAME, version = PanguCore.VERSION)
public class PanguCore {
    /**
     * The mod id for child mod needn't any prefix like "pangu-", "pangu_" or "pangu".
     * but if you want so, you can go you way
     */
    public static final String MODID = "pangu",
    /**
     * Every Mod based on Pangu Core should has the prefix "Pangu ", such as,
     * <ul>
     *     <li>"Pangu Workshop" for chinese building, block</li>
     *     <li>"Pangu Tools" for chinese weapon, armor</li>
     *     <li>"Pangu Creative" for chinese craft and some way of playing sth</li>
     *     <li>so on....</li>
     * </ul>
     */
    NAME = "Pangu Core",
    /**
     * The version number means BIG_VERSION.SMALL_VERSION.BUILD_VERSION.COMMIT_VERSION,
     * the COMMIT_VERSION should be hidden while the version is appearing to player.
     * <p>
     * For child mod, it can be whatever you want. But you'd better make sure you mod can run in this version of pangu
     */
    VERSION = "1.0.0.1";
}
