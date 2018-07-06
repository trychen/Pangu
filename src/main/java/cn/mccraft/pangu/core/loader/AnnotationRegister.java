package cn.mccraft.pangu.core.loader;

import com.google.common.collect.Sets;

import java.lang.annotation.Annotation;
import java.util.Set;

public enum AnnotationRegister {
    INSTANCE;

    private Set<Class<? extends Annotation>> regAnnos = Sets.newHashSet();

    /**
     * auto inject all @RegisteringHandler class
     */
    @SuppressWarnings("unchecked")
    @AnnotationInjector.StaticInvoke
    public static void injectAnnotation(AnnotationStream<RegisteringHandler> anno) {
        anno.typeStream()
                // clean non-annotation class
                .filter(Annotation.class::isAssignableFrom)
                .forEach(it -> AnnotationRegister.INSTANCE.regAnnos.add((Class<? extends Annotation>) it));
    }
}
