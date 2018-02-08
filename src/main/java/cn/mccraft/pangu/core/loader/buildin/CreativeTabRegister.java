package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.GeneralCreativeTab;
import cn.mccraft.pangu.core.loader.annotation.SetCreativeTab;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoWired
public class CreativeTabRegister {
    private Map<String, CreativeTabs> tabs = new HashMap<>();

    /**
     * the impl of {@link GeneralCreativeTab}
     * @param event
     */
    @Load(LoaderState.CONSTRUCTING)
    public void wireCreativeTab(FMLConstructionEvent event) {
        for (ASMDataTable.ASMData asmData : event.getASMHarvestedData().getAll(GeneralCreativeTab.class.getName())) {
            try {
                String tabKey = (String) asmData.getAnnotationInfo().get("value");
                if (tabKey == null) continue;

                Class clazz = Class.forName(asmData.getClassName());
                Field field = clazz.getField(asmData.getObjectName());

                InstanceHolder.setObject(field, getTab(tabKey));
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                PanguCore.getLogger().error("Unable to wire CreativeTab of " + asmData.getObjectName() + " from " + asmData.getClassName(), e);
            }
        }
        PanguCore.getLogger().info("Processed " + tabs.size() + " PanguCreativeTab");
    }

    /**
     * the impl of {@link SetCreativeTab}
     * @param event
     */
    @Load(LoaderState.CONSTRUCTING)
    public void setCreativeTab(FMLConstructionEvent event) {
        int setFields = 0;
        for (ASMDataTable.ASMData asmData : event.getASMHarvestedData().getAll(SetCreativeTab.class.getName())) {
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
                    Field field = clazz.getDeclaredField(asmData.getObjectName());
                    if (isCreateTabSetable(field)) {
                        setCreativeTab(field, tabKey);
                        setFields++;
                    } else {
                        PanguCore.getLogger().error(String.format("Unsupported type to setCreativeTab. fieldname=%s, class=%s, tabKey=%s.", field.getName(), field.getDeclaringClass().toString(), tabKey));
                    }
                }
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        PanguCore.getLogger().info("Processed " + setFields + " SetCreativeTab annotations");
    }

    /**
     * setCreativeTab of field using {@link InstanceHolder#getInstance(Object)} and {@link CreativeTabRegister#getTab(String)}
     * @param field
     * @param tabKey
     */
    public void setCreativeTab(Field field, String tabKey) {
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
        return Item.class.isAssignableFrom(field.getType()) || Block.class.isAssignableFrom(field.getType());
    }

    /**
     * get or create {@link PanguCreativeTab}
     */
    @Nonnull
    public CreativeTabs getTab(String key) {
        CreativeTabs creativeTabs = tabs.get(key);
        if (creativeTabs == null) {
            creativeTabs = new PanguCreativeTab(key);
            tabs.put(key, creativeTabs);
        }
        return creativeTabs;
    }

    /**
     * a simple impl {@link CreativeTabs}
     */
    static class PanguCreativeTab extends CreativeTabs {
        private ItemStack tabIconItem;

        public PanguCreativeTab(String label) {
            super(label);
            tabIconItem = new ItemStack(Items.AIR);
        }

        public PanguCreativeTab(String label, ItemStack tabIconItem) {
            super(label);
            setTabIconItem(tabIconItem);
        }

        public void setTabIconItem(@Nonnull ItemStack tabIconItem) {
            this.tabIconItem = tabIconItem;
        }

        /**
         * set tab icon if not exist
         */
        public void setTabIconItemIfNull(@Nonnull ItemStack tabIconItem) {
            if (this.tabIconItem == null || this.tabIconItem.getItem() == Items.AIR) this.tabIconItem = tabIconItem;
        }

        @Override
        @Nonnull
        public ItemStack getTabIconItem() {
            return tabIconItem;
        }
    }
}
