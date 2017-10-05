package cn.mccraft.pangu.core.loader;


import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A loader which can make auto invoke the annotated function (must be visible to core) with this class.
 * Your method's parent class will be auto added into {@link Proxy}, and the method must be
 *
 * @author LasmGratel
 * @since .2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Load {
    /**
     * Select which state we should invoke this method.
     * 选择你需要执行该函数的Forge加载状态。
     */
    LoaderState value() default LoaderState.PREINITIALIZATION;

    /**
     * Select which side we should invoke this method.
     * 选择你需要执行该函数所需的平台，服务器或是客户端。
     */
    Side side() default Side.SERVER;
}