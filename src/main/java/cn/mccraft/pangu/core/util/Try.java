package cn.mccraft.pangu.core.util;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.function.basic.ThrowableRunnable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author trychen
 * @since 1.0.6
 */
public interface Try {
    static Runnable safe(@Nonnull ThrowableRunnable mapper) {
        return safe(mapper, "");
    }

    static Runnable safe(@Nonnull ThrowableRunnable mapper, @Nonnull String errorMessage) {
        return () -> {
            try {
                mapper.run();
            } catch (Throwable ex) {
                PanguCore.getLogger().error(errorMessage, ex);
            }
        };
    }

    static <T, R> Function<T, R> safe(@Nonnull Function<T, R> mapper) {
        return safe(mapper, "");
    }
    static <T, R> Function<T, R> safe(@Nonnull Function<T, R> mapper, @Nonnull String errorMessage) {
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Throwable ex) {
                PanguCore.getLogger().error(errorMessage, ex);
            }
            return null;
        };
    }

    static <T> Consumer<? super T> safe(@Nonnull Consumer<? super T> action) {
        return safe(action, "");
    }
    static <T> Consumer<? super T> safe(@Nonnull Consumer<? super T> action, @Nonnull String errorMessage) {
        return t -> {
            try {
                action.accept(t);
            } catch (Throwable ex) {
                PanguCore.getLogger().error(errorMessage, ex);
            }
        };
    }
}
