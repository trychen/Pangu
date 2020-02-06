package cn.mccraft.pangu.core.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface Ticking extends Runnable {
    default boolean isRegistered() {
        return Manager.TICKS_LIST.contains(this);
    }

    default void registerTick() {
        Manager.TICKS_LIST.removeIf(e -> e == this);
        Manager.TICKS_LIST.add(this);
    }

    default void unregisterTick() {
        Manager.TICKS_LIST.remove(this);
    }

    class Manager {
        public static final List<Ticking> TICKS_LIST;
        private static final ScheduledExecutorService EXECUTOR_SERVICE;

        static {
            TICKS_LIST = Lists.newCopyOnWriteArrayList();
            (EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor()).scheduleAtFixedRate(() -> {
                TICKS_LIST.removeIf(Objects::isNull);
                TICKS_LIST.iterator().forEachRemaining(Runnable::run);
            }, 0L, 16L, TimeUnit.MILLISECONDS);
        }
    }
}
