package cn.mccraft.pangu.core.util.function.basic;

@FunctionalInterface
public interface ThrowableRunnable {
    void run() throws Exception;
}
