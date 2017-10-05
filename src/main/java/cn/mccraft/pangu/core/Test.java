package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.item.PGItem;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.loader.annotation.RegRecipe;
import cn.mccraft.pangu.core.loader.buildin.RecipeRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class Test {
    @RegRecipe("APPLE2DIAMOND")
    public static IRecipe recipe = RecipeRegister.buildShapedRecipe("hello", new ItemStack(Items.DIAMOND), "# #", " # ", "# #", '#', Items.APPLE);

    @RegItem("hello")
    public static Item item = new PGItem();

    @AutoWired
    private static Hello hello;

    @AutoWired
    public static class Hello{
        public Hello() {
            System.out.println("Hello World!");
        }
    }
}
