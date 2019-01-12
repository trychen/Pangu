package cn.mccraft.pangu.core.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface AnnotationRegister<ANNOTATION extends Annotation, INSTANCE> {
    default void registerField(Field field, INSTANCE instance, ANNOTATION annotation, String domain) {
    }

    default void registerClass(Class<? extends INSTANCE> clazz, ANNOTATION annotation, String domain) {
    }
}
