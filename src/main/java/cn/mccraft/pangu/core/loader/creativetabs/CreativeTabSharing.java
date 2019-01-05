package cn.mccraft.pangu.core.loader.creativetabs;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.IRegister;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 1.0.1
 * @author trychen
 */
@AutoWired
public class CreativeTabSharing implements IRegister<SharedCreativeTab, Object> {
    /**
     * shared creative tabs
     */
    @Getter
    private Map<String, CreativeTabs> tabs = new HashMap<>();

    private int creativeTabsLength = -1;

    @Override
    public void registerField(Field field, Object o, SharedCreativeTab annotation, String domain) {
        if (isCreateTabSetable(field))
            setCreativeTab(field, annotation.value(), annotation.asTabIcon());
    }

    @Override
    public void registerClass(Class clazz, SharedCreativeTab annotation, String domain) {
        Arrays.stream(clazz.getDeclaredFields())
                // clean special annotated field
                .filter(field -> !field.isAnnotationPresent(SharedCreativeTab.class))
                // clean unsetable field
                .filter(this::isCreateTabSetable)
                .forEach(field -> setCreativeTab(field, annotation.value(), annotation.asTabIcon()));
    }

    /**
     * setCreativeTab of field using {@link InstanceHolder#getInstance(Object)} and {@link CreativeTabSharing#getTab(String)}
     * @param field the field to set value
     * @param tabKey the unlocalized name of the group
     * @param setIcon If set tab icon for CustomIconCreativeTab
     */
    public void setCreativeTab(@Nonnull Field field, @Nonnull String tabKey, boolean setIcon) {
        CreativeTabs tab = getTab(tabKey);

        if (CreativeTabs.class.isAssignableFrom(field.getType())) {
            // for CreativeTabs field
            try {
                InstanceHolder.setObject(field, tab);
            } catch (IllegalAccessException e) {
                PanguCore.getLogger().error("Unable to find any instance to set value of " + field.getName() + " from " + field.getDeclaringClass(), e);
            }
        }

        Object object = null;
        try {
            object = InstanceHolder.getObject(field);
        } catch (IllegalAccessException e) {
            PanguCore.getLogger().error("Unable to get the instance of the item/block to set CreativeTab of " + field.getName() + " from " + field.getDeclaringClass(), e);
            return;
        }
        if (object instanceof Item) {
            ((Item) object).setCreativeTab(tab);

            if (setIcon && tab instanceof CustomIconCreativeTab) {
                ((CustomIconCreativeTab) tab).setTabIconItemIfNull(new ItemStack((Item) object));
            }
        } else if (object instanceof Block) {
            ((Block) object).setCreativeTab(tab);

            if (setIcon && tab instanceof CustomIconCreativeTab) {
                ((CustomIconCreativeTab) tab).setTabIconItemIfNull(new ItemStack((Block) object));
            }
        }
    }

    /**
     * @return if the field is a block or a item
     */
    public boolean isCreateTabSetable(@Nonnull Field field) {
        return CreativeTabs.class.isAssignableFrom(field.getType())
                || Item.class.isAssignableFrom(field.getType())
                || Block.class.isAssignableFrom(field.getType());
    }

    /**
     * get or create {@link CustomIconCreativeTab}
     */
    @Nonnull
    public CreativeTabs getTab(@Nonnull String key) {
        // Reflesh creative tab.
        int currentLength = CreativeTabs.CREATIVE_TAB_ARRAY.length;
        if (creativeTabsLength != currentLength) {
            creativeTabsLength = currentLength;
            for(CreativeTabs creativeTabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
                tabs.put(creativeTabs.getTabLabel(), creativeTabs);
            }
        }

        CreativeTabs creativeTabs = tabs.get(key);
        if (creativeTabs == null) {
            //creativeTabs = new CustomIconCreativeTab("pangu" + Character.toUpperCase(key.charAt(0)) + key.substring(1));
            creativeTabs = new CustomIconCreativeTab(key);
            tabs.put(key, creativeTabs);
            creativeTabsLength++;
        }
        return creativeTabs;
    }
}
