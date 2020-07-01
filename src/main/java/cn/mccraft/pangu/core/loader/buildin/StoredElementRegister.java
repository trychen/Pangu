package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.AnnotationRegister;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * A base achievement of IRegister with base caching registering item
 *
 * @param <T> the registering item
 * @param <A> the annotation for registering
 */
public abstract class StoredElementRegister<T, A extends Annotation> implements AnnotationRegister<A, T> {
    @Getter
    protected Set<FieldElement> items = Sets.newHashSet();

    /**
     * A simple field storage
     */
    public class FieldElement {
        private final Field field;
        private final T instance;
        private final A annotation;
        private final String domain;

        public FieldElement(Field field, T instance, A annotation, String domain) {
            this.field = field;
            this.instance = instance;
            this.annotation = annotation;
            this.domain = domain;
        }

        public Field getField() {
            return field;
        }

        public T getInstance() {
            return instance;
        }

        public A getAnnotation() {
            return annotation;
        }

        public String getDomain() {
            return domain;
        }
        public ResourceLocation getResLoc(String path) {
            return PanguResLoc.of(getDomain(), path);
        }
    }

    @Override
    public void registerField(Field field, T instance, A annotation, String domain) {
        if (!acceptInstance(field, instance, annotation, domain)) return;
        items.add(new FieldElement(field, instance, annotation, domain));
    }

    public boolean acceptInstance(Field field, T instance, A annotation, String domain) {
        return instance != null;
    }

    public String[] getAnnotationRegistryName() {
        return new String[0];
    }
}
