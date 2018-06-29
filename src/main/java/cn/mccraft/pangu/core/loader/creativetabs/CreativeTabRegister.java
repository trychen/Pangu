package cn.mccraft.pangu.core.loader.creativetabs;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 1.0.1
 * @author trychen
 */
@AutoWired
public class CreativeTabRegister {
    /**
     * shared creative tabs
     */
    private Map<String, CreativeTabs> tabs = new HashMap<>();

    /**
     * the impl of {@link SharedCreativeTab}
     *
     * @param event
     */
    @AnnotationInjector.StaticInvoke
    public void wireCreativeTab(ASMDataTable asmDataTable) {
        int setFields = 0;
        for (ASMDataTable.ASMData asmData : asmDataTable.getAll(SharedCreativeTab.class.getName())) {
            try {
                String tabKey = (String) asmData.getAnnotationInfo().get("value");
                if (tabKey == null) continue;

                Class clazz = Class.forName(asmData.getClassName());
                if (asmData.getClassName().equals(asmData.getObjectName())) {
                    // class
                    for (Field field : clazz.getDeclaredFields()) {
                        if (isCreateTabSetable(field)) {
                            setCreativeTab(field, tabKey);
                            setFields++;
                        } else {
                            PanguCore.getLogger().error(String.format("Unsupported type to setCreativeTab. fieldname=%s, class=%s, tabKey=%s.", field.getName(), field.getDeclaringClass().toString(), tabKey));
                        }
                    }
                } else {
                    // field
                    Field field = clazz.getField(asmData.getObjectName());

                    if (isCreateTabSetable(field)) {
                        setCreativeTab(field, tabKey);
                        setFields++;
                    }
                }


            } catch (ClassNotFoundException | NoSuchFieldException e) {
                PanguCore.getLogger().error("Unable to wire CreativeTab of " + asmData.getObjectName() + " from " + asmData.getClassName(), e);
            }
        }
        PanguCore.getLogger().info("Created " + tabs.size() + " creative tabs and processed " + setFields + " field.");
    }

//
//    /**
//     * the impl of {@link SetCreativeTab}
//     * @param event
//     */
//    @Load(LoaderState.CONSTRUCTING)
//    public void setCreativeTab(FMLConstructionEvent event) {
//        int setFields = 0;
//        for (ASMDataTable.ASMData asmData : event.getASMHarvestedData().getAll(SetCreativeTab.class.getName())) {
//            try {
//                String tabKey = (String) asmData.getAnnotationInfo().get("value");
//                if (tabKey == null) continue;
//
//                Class clazz = Class.forName(asmData.getClassName());
//                if (asmData.getClassName().equals(asmData.getObjectName())) {
//                    // class
//                    for (Field field : clazz.getDeclaredFields()) {
//                        if (isCreateTabSetable(field)) {
//                            setCreativeTab(field, tabKey);
//                            setFields++;
//                        } else {
//                            PanguCore.getLogger().error(String.format("Unsupported type to setCreativeTab. fieldname=%s, class=%s, tabKey=%s.", field.getName(), field.getDeclaringClass().toString(), tabKey));
//                        }
//                    }
//                } else {
//                    // field
//                    Field field = clazz.getDeclaredField(asmData.getObjectName());
//                    if (isCreateTabSetable(field)) {
//                        setCreativeTab(field, tabKey);
//                        setFields++;
//                    } else {
//                        PanguCore.getLogger().error(String.format("Unsupported type to setCreativeTab. fieldname=%s, class=%s, tabKey=%s.", field.getName(), field.getDeclaringClass().toString(), tabKey));
//                    }
//                }
//            } catch (ClassNotFoundException | NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//        }
//        PanguCore.getLogger().info("Processed " + setFields + " SetCreativeTab annotations");
//    }

    /**
     * setCreativeTab of field using {@link InstanceHolder#getInstance(Object)} and {@link CreativeTabRegister#getTab(String)}
     * @param field
     * @param tabKey
     */
    public void setCreativeTab(Field field, String tabKey) {
        if (CreativeTabs.class.isAssignableFrom(field.getType())) {
            // for CreativeTabs field
            try {
                InstanceHolder.setObject(field, getTab(tabKey));
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
            ((Item) object).setCreativeTab(getTab(tabKey));
        } else if (object instanceof Block) {
            ((Block) object).setCreativeTab(getTab(tabKey));
        }
    }

    /**
     * @return if the field is a block or a item
     */
    public boolean isCreateTabSetable(Field field) {
        return CreativeTabs.class.isAssignableFrom(field.getType())
                || Item.class.isAssignableFrom(field.getType())
                || Block.class.isAssignableFrom(field.getType());
    }

    /**
     * get or create {@link CustomIconCreativeTab}
     */
    @Nonnull
    public CreativeTabs getTab(String key) {
        CreativeTabs creativeTabs = tabs.get(key);
        if (creativeTabs == null) {
            creativeTabs = new CustomIconCreativeTab(key);
            tabs.put(key, creativeTabs);
        }
        return creativeTabs;
    }

}
