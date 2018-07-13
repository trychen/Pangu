package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.ModFinder;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.util.Map;

public enum ElementInjector {
    INSTANCE;

    private Map<Class<? extends Annotation>, IRegister> annotations = Maps.newHashMap();

    /**
     * auto inject all @RegisteringHandler class
     */
    @SuppressWarnings("unchecked")
    @AnnotationInjector.StaticInvoke
    public void injectAnnotation(AnnotationStream<RegisteringHandler> anno) {
        anno.typeStream()
                // clean non-annotation class
                .filter(Annotation.class::isAssignableFrom)
                .forEach(it -> ElementInjector.INSTANCE.annotations
                        .put((Class<? extends Annotation>) it, (IRegister) InstanceHolder.getInstance(it.getAnnotation(RegisteringHandler.class).value())));
    }

    @Load
    public void start() {
        annotations.forEach((annoClass, register) -> {
            final AnnotationStream<? extends Annotation> anno = AnnotationStream.of(annoClass);

            anno.fieldStream().forEach(field -> {
                try {
                    //noinspection unchecked
                    register.registerField(field, InstanceHolder.getObject(field), field.getAnnotation(annoClass), ModFinder.getDomain(field).orElse(PanguCore.ID));
                } catch (Exception e) {
                    PanguCore.getLogger().error(
                            String.format("Unable to register %s annotation for %s", annoClass.getName(), field.toGenericString())
                            , e
                    );
                }
            });

            // TODO: inject Method

            anno.typeStream().forEach(clazz -> {
                try {
                    //noinspection unchecked
                    register.registerClass(clazz, clazz.getAnnotation(annoClass), ModFinder.getDomain(clazz).orElse(PanguCore.ID));
                } catch (Exception e) {
                    PanguCore.getLogger().error(
                            String.format("Unable to register %s annotation for %s", annoClass.getName(), clazz.toGenericString())
                            , e
                    );
                }
            });

        });
    }

    public IRegister get(Class<? extends Annotation> clazz) {
        return annotations.get(clazz);
    }
}
