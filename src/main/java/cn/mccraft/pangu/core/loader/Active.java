package cn.mccraft.pangu.core.loader;

import java.lang.annotation.*;

/**
 * 在游戏加载时，初始化该类。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Active {
    @AutoWired
    class Handler {
        @AnnotationInjector.StaticInvoke
        public void injectAnnotation(AnnotationStream<Active> anno) {
            anno.classStream();
        }
    }
}
