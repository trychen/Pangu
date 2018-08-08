package cn.mccraft.pangu.core.loader.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * a simple impl {@link CreativeTabs}
 *
 * @since 1.0.1
 * @author trychen
 */
class CustomIconCreativeTab extends CreativeTabs {
    private ItemStack tabIconItem;

    public CustomIconCreativeTab(String label) {
        super(label);
        tabIconItem = new ItemStack(Items.AIR);
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return tabIconItem;
    }

    public CustomIconCreativeTab(@Nonnull String label, @Nonnull ItemStack tabIconItem) {
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
}
