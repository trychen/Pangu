package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.util.ResourceLocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * use to storage cached item which need register later
 *
 * @since .3
 * @author trychen
 */
public class RegisteringItem<T, A extends Annotation> {
    /**
     * the field that storage this item
     */
    private final Field field;

    /**
     * the item's instance
     */
    private final T key;

    /**
     * the registering item's domain, empty or null if you modified in name or needed
     */
    private final String domain;

    /**
     * the annotation used to register
     */
    private final A annotation;

    public RegisteringItem(Field field, T key, String domain, A annotation) {
        this.field = field;
        this.key = key;
        this.domain = domain;
        this.annotation = annotation;
    }

    public T getItem() {
        return key;
    }

    public String getDomain() {
        return domain;
    }

    public A getAnnotation() {
        return annotation;
    }

    public Field getField() {
        return field;
    }

    /**
     * build resource location
     * @param name registry name like "example:example_item"
     * @return notnull ResourceLocation
     */
    public ResourceLocation buildRegistryName(String name) {
        if (domain == null || domain.isEmpty()){
            return new ResourceLocation(name);
        }
        return new ResourceLocation(domain, name);
    }

    /**
     * build with string array
     * @param name name array like {"example", "item"}
     * @return
     */
    public ResourceLocation buildRegistryName(String... name) {
        return buildRegistryName(NameBuilder.buildRegistryName(name));
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public String buildUnlocalizedName(String[] name) {
        return NameBuilder.buildUnlocalizedName(name);
    }
}
