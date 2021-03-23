package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.util.Games;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;

public class ScreenPlayerBackpack extends ScreenContainer {
//    @BindKeyPress(value = Keyboard.KEY_N, devOnly = true)
//    public static void openFromKey() {
//        new ScreenPlayerBackpack().open();
//    }
    
    public ScreenPlayerBackpack() {
        super(Games.player().inventoryContainer);
    }

    @Override
    public void init() {
        super.init();
        addBackpack(halfWidth, halfHeight);
    }
}
