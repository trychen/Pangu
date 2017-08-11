package cn.mccraft.pangu.core.annotation;


import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A annotation which can make auto invoke the annotated function with this class.
 * You should add your custom loader in {@link cn.mccraft.pangu.core.CommonProxy#loaders}.
 *
 * 被该注解注释的函数将被自动执行。
 * 你需要把你的自定义加载器放入{@link cn.mccraft.pangu.core.CommonProxy#loaders}
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