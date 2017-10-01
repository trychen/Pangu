package cn.mccraft.pangu.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface AnnotationReflect {
    static <T> T getField(Annotation annotation, String name, Class<T> typeCheck) {
        for (Field field : annotation.getClass().getFields()) {
            System.out.println(field.getName());
        }
        return null;
    }
}
