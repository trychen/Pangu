package cn.mccraft.pangu.core.util;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

/**
 * @author trychen
 * @since 1.0.3
 */
public interface ModFinder {
    /**
     * Find ModContainer that the class belongs to.
     *
     * @param target the class to find
     * @return may return empty if target is belongs to native lib
     */
    static Optional<ModContainer> getModContainer(@Nonnull Class<?> target) {
        URI source;
        try {
            // get source uri
            source = target.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            return getModContainerFromClassByPackageName(target);
        }

        if (source.toString().endsWith(".class")) {
            return getModContainerFromClassByPackageName(target);
        }

        return getModContainerFromFile(source);
    }

    /**
     * Find ModContainer that the file belongs to by checking source location.
     *
     * @param target the source to find
     * @return may return empty if target doesn't belongs to any mod
     */
    static Optional<ModContainer> getModContainerFromFile(@Nonnull URI target) {
        return Loader.instance().getModList().stream().filter(mod ->
                target.equals(mod.getSource().toURI()) // check whether the same source
        ).findAny();
    }

    /**
     * Find ModContainer that the class belongs to by checking package name.
     *
     * @param target the class to find
     */
    static Optional<ModContainer> getModContainerFromClassByPackageName(@Nonnull Class<?> target) {
        return Loader.instance().getModList().stream().filter(mod -> mod.getOwnedPackages().contains(target.getPackage().getName())).findAny();
    }

    Map<Class, Optional<String>> CACHED_MOD_ID = Maps.newHashMap();

    /**
     * Find domain that the class belongs to.
     */
    static Optional<String> getDomain(@Nonnull Class<?> target) {
        Optional<String> domain = CACHED_MOD_ID.get(target);
        if (domain == null) {
            domain = getModContainer(target).map(ModContainer::getModId);
            CACHED_MOD_ID.put(target, domain);
        }
        return domain;
    }

    /**
     * Find domain that the field belongs to.
     */
    static Optional<String> getDomain(@Nonnull Field target) {
        return getDomain(target.getDeclaringClass());
    }

    /**
     * Find mod container by modid
     */
    static Optional<ModContainer> getModContainer(@Nonnull String modID) {
        return Loader.instance().getModList().stream().filter(mod -> mod.getModId().equals(modID)).findAny();
    }
}
