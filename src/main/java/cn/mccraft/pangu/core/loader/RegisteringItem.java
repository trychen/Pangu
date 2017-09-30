package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.util.ReflectUtils;
import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.util.ResourceLocation;

import java.lang.annotation.Annotation;

/**
 * use to storage cached item which need register later
 *
 * @since .3
 * @author trychen
 */
public class RegisteringItem<T, A extends Annotation> {
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

    public RegisteringItem(T key, String domain, A annotation) {
        this.key = key;
        this.annotation = annotation;

        // special domain
        String parentDomain = ReflectUtils.invokeMethod(annotation, "domain", String.class);
        if (parentDomain != null) this.domain = parentDomain;
        else this.domain = domain;
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
}
