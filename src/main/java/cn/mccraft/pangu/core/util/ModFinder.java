package cn.mccraft.pangu.core.util;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

        final Optional<ModContainer> fromFile = getModContainerFromFile(source);

        if (!fromFile.isPresent()) {
            return getModContainerFromClassByPackageName(target);
        }

        return fromFile;
    }

    /**
     * Find ModContainer that the file belongs to by checking source location.
     *
     * @param target the source to find
     * @return may return empty if target doesn't belongs to any mod
     */
    @Nullable
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
    @Nonnull
    static Optional<ModContainer> getModContainerFromClassByPackageName(@Nonnull Class<?> target) {
        return Loader.instance().getModList().stream().filter(mod -> mod.getOwnedPackages().contains(target.getPackage().getName())).findAny();
    }

    Map<Class, String> CACHED_MOD_ID = Maps.newHashMap();

    /**
     * Find domain that the class belongs to.
     */
    @Nonnull
    static Optional<String> getDomain(@Nonnull final Class<?> target) {
        if (CACHED_MOD_ID.containsKey(target)) {
            return Optional.ofNullable(CACHED_MOD_ID.get(target));
        }

        Optional<ModContainer> container = getModContainer(target);
        if (!container.isPresent()) {
            CACHED_MOD_ID.put(target, null);
            return Optional.empty();
        }
        CACHED_MOD_ID.put(target, container.get().getModId());
        return Optional.of(container.get().getModId());
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
