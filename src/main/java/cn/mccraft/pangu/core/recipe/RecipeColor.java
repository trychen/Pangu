package cn.mccraft.pangu.core.recipe;

import cn.mccraft.pangu.core.capability.color.CapabilityColor;
import cn.mccraft.pangu.core.capability.color.ColorStats;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class RecipeColor extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && !stack.hasCapability(CapabilityColor.COLOR_STATS, null) && !DyeUtils.isDye(stack))
                return false;
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        // TODO Copied from RecipesArmorDyes, rewrite needed
        ItemStack itemStack = ItemStack.EMPTY;
        int[] colorComponents = new int[3];
        int i = 0;
        int j = 0;
        ColorStats colorStats = null;

        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack itemStack1 = inv.getStackInSlot(k);

            if (!itemStack1.isEmpty()) {
                if (itemStack1.hasCapability(CapabilityColor.COLOR_STATS, null)) {
                    if (!itemStack.isEmpty()) return ItemStack.EMPTY;

                    itemStack = itemStack1.copy();
                    colorStats = itemStack.getCapability(CapabilityColor.COLOR_STATS, null);
                    itemStack.setCount(1);

                    int l = colorStats.getColor();
                    float f = (float) (l >> 16 & 255) / 255.0F;
                    float f1 = (float) (l >> 8 & 255) / 255.0F;
                    float f2 = (float) (l & 255) / 255.0F;
                    i = (int) ((float) i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                    colorComponents[0] = (int) ((float) colorComponents[0] + f * 255.0F);
                    colorComponents[1] = (int) ((float) colorComponents[1] + f1 * 255.0F);
                    colorComponents[2] = (int) ((float) colorComponents[2] + f2 * 255.0F);
                    ++j;
                } else {
                    if (!DyeUtils.isDye(itemStack1))
                        return ItemStack.EMPTY;

                    float[] afloat = DyeUtils.colorFromStack(itemStack1).get().getColorComponentValues();
                    int l1 = (int) (afloat[0] * 255.0F);
                    int i2 = (int) (afloat[1] * 255.0F);
                    int j2 = (int) (afloat[2] * 255.0F);
                    i += Math.max(l1, Math.max(i2, j2));
                    colorComponents[0] += l1;
                    colorComponents[1] += i2;
                    colorComponents[2] += j2;
                    ++j;
                }
            }
        }

        if (colorStats == null)
            return ItemStack.EMPTY;

        int i1 = colorComponents[0] / j;
        int j1 = colorComponents[1] / j;
        int k1 = colorComponents[2] / j;
        float f3 = (float) i / (float) j;
        float f4 = (float) Math.max(i1, Math.max(j1, k1));
        i1 = (int) ((float) i1 * f3 / f4);
        j1 = (int) ((float) j1 * f3 / f4);
        k1 = (int) ((float) k1 * f3 / f4);
        int k2 = (i1 << 8) + j1;
        k2 = (k2 << 8) + k1;
        colorStats.setColor(k2);
        return itemStack;
    }

    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }

    public boolean isDynamic() {
        return true;
    }

    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }
}
