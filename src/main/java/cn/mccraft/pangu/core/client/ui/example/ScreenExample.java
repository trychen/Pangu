package cn.mccraft.pangu.core.client.ui.example;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.*;
import cn.mccraft.pangu.core.client.ui.builtin.DialogModal;
import cn.mccraft.pangu.core.client.ui.builtin.Label;
import cn.mccraft.pangu.core.client.ui.builtin.SelectionBox;
import cn.mccraft.pangu.core.client.ui.builtin.TextButton;
import cn.mccraft.pangu.core.client.ui.meta.Line;
import cn.mccraft.pangu.core.network.Remote;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@DevOnly
@SideOnly(Side.CLIENT)
public class ScreenExample extends Screen {
    @Override
    public void init() {
        drawDefaultBackground = false;
        TabContainer tabContainer = new TabContainer(150, 150);
        tabContainer.setCenteredPosition(width/2, height/2);
//        addComponent(new EntityShow(Minecraft.getMinecraft().player).setShowBack(true).setScale(100).setPosition(width / 2 - 120, height / 2 + 100));
        addComponent(new SelectionBox("Hello").setSelectEvent(((isSelected, mouseButton, mouseX, mouseY) -> {
            tabContainer.select(isSelected?0:1);
            return isSelected;
        })).setColor(0xFFFFFFFF).setPosition(0, 0));

        Container container1 = new Container(150, 150);
        container1.addComponent(new Label("Hello Container 1").setCentered(true).setCenteredPosition(150 / 2, 150 / 2));

        Container container2 = new Container(150, 150);
        container2.addComponent(new Label("Hello Container 2").setCentered(true).setCenteredPosition(150 / 2, 150 / 2));
        container2.addComponent(new TextButton("Hello", TextButton.PRIMARY).setCenteredPosition(150 / 2, 10));
        addComponent(new TextButton("Hello", TextButton.PRIMARY).onButtonClick(buttonClickEvent -> {
            setModal(new DialogModal(this).addText("Hello", "World").addButton(UI.ofNoButton()).addButton(UI.ofYesButton()));
        }).setCenteredPosition(150 / 2, 10));
        addComponent(tabContainer.addTabs(container1, container2));
        addComponent(focus(new TextField(70, 20).setCenteredPosition( 200, 120)));
        addComponent(focus(new TextField(70, 20).setCenteredPosition( 200, 160)));
    }

    @Override
    public void draw() {
//        Rect.drawGradient(0, 0, width * 1.7F, height * 1.7F, 0xCB000000, 0, 0xCB000000, 0);
    }

    @DevOnly
    @BindKeyPress(description = "key.test.ScrenClothes", keyCode = Keyboard.KEY_K)
    public static void test() {
        new ScreenExample().open();
    }

    @Remote(0)
    public void test(EntityPlayer player, NBTTagCompound tag) {
        // do something
    }
}
