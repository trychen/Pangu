package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.GeneralCreativeTab;
import net.minecraft.creativetab.CreativeTabs;

@AutoWired
public class Test {
    @GeneralCreativeTab("hello")
    public CreativeTabs tab;
}
