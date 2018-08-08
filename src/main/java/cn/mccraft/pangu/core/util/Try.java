package cn.mccraft.pangu.core.util;

import cn.mccraft.pangu.core.PanguCore;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author trychen
 * @since 1.0.6
 */
public interface Try {
    static <T, R> Function<T, R> safe(Function<T, R> mapper) {
        return safe(mapper, "");
    }
    static <T, R> Function<T, R> safe(Function<T, R> mapper, String errorMessage) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Exception ex) {
                PanguCore.getLogger().error(errorMessage, ex);
            }
            return null;
        };
    }

    static <T> Consumer<? super T> safe(Consumer<? super T> action) {
        return safe(action, "");
    }
    static <T> Consumer<? super T> safe(Consumer<? super T> action, String errorMessage) {
        Objects.requireNonNull(action);
        return t -> {
            try {
                action.accept(t);
            } catch (Exception ex) {
                PanguCore.getLogger().error(errorMessage, ex);
            }
        };
    }
}
