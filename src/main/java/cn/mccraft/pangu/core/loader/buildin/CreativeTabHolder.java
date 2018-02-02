package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.GeneralCreativeTab;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@AutoWired
public class CreativeTabHolder {
    private Map<String, CreativeTabs> tabs = new HashMap<>();

    @Load(LoaderState.CONSTRUCTING)
    public void wireCreativeTab(FMLConstructionEvent event) {
        for (ASMDataTable.ASMData asmData : event.getASMHarvestedData().getAll(GeneralCreativeTab.class.getName())) {
            try {
                String tabKey = (String) asmData.getAnnotationInfo().get("value");
                if (tabKey == null) continue;

                Class clazz = Class.forName(asmData.getClassName());
                Field field = clazz.getField(asmData.getObjectName());
                if (Modifier.isStatic(field.getModifiers())) {
                    field.set(null, getTab(tabKey));
                } else {
                     Object owner = InstanceHolder.getInstance(clazz);
                     if (owner == null) {
                         continue;
                     }
                    field.set(owner, getTab(tabKey));
                }
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        PanguCore.getLogger().info("Processed " + tabs.size() + " CreativeTabs");
    }

    @Nonnull
    public CreativeTabs getTab(String key) {
        CreativeTabs creativeTabs = tabs.get(key);
        if (creativeTabs == null) {
            creativeTabs = new PanguCreativeTab(key);
            tabs.put(key, creativeTabs);
        }
        return creativeTabs;
    }

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
