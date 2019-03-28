package cn.mccraft.pangu.core.asm.util;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.HashMap;
import java.util.Map;

public interface ASM {
    static Map<String, Object> mapAnnotationValues(AnnotationNode ann) {
        Map<String, Object> map = new HashMap<>();
        if (ann.values == null) return map;
        for (int x = 0; x < ann.values.size() - 1; x += 2) {
            Object key = ann.values.get(x);
            Object value = ann.values.get(x + 1);
            map.put((String) key, value);
        }
        return map;
    }

    static Side mapAnnotationSideValue(String key, Map<String, Object> map) {
        Object sideObj = map.get(key);
        if (sideObj == null) {
            return Side.SERVER;
        } else {
            return Side.valueOf(((String[]) map.get(key))[1]);
        }
    }

    static void modifyAnnotation(AnnotationNode ann, String metaKey, Object metaData) {
        if (ann.values == null) {
            ann.values = Lists.newArrayList(metaKey, metaData);
            return;
        }

        for (int i = 0; i < ann.values.size() - 1; i += 2) {
            Object key = ann.values.get(i);
            Object value = ann.values.get(i + 1);
            if (key.equals(metaKey)) {
                ann.values.set(i + 1, metaData);
                return;
            }
        }

        ann.values.add(metaKey);
        ann.values.add(metaData);
    }
}
