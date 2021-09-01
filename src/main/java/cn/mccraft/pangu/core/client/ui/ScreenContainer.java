package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.Games;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Set;

@Getter
@Setter
public class ScreenContainer extends Screen {
    protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
    /**
     * Used when touchscreen is enabled
     */
    protected ItemStack draggedStack = ItemStack.EMPTY;
    /**
     * Used when touchscreen is enabled
     */
    protected ItemStack returningStack = ItemStack.EMPTY;
    protected boolean ignoreMouseUp;
    protected ItemStack shiftClickedSlot = ItemStack.EMPTY;
    /**
     * A list of the players inventory slots
     */
    protected Container inventorySlots;
    protected boolean dragSplitting;
    /**
     * holds the slot currently hovered
     */
    protected Slot hoveredSlot;
    /**
     * Used when touchscreen is enabled.
     */
    protected Slot clickedSlot;
    /**
     * Used when touchscreen is enabled.
     */
    protected boolean isRightMouseClick;
    protected int touchUpX;
    protected int touchUpY;
    protected Slot returningStackDestSlot;
    protected long returningStackTime;
    protected Slot currentDragTargetSlot;
    protected long dragItemDropDelay;
    protected int dragSplittingLimit;
    protected int dragSplittingButton;
    protected int dragSplittingRemnant;
    protected long lastClickTime;
    protected Slot lastClickSlot;
    protected int lastClickButton;
    protected boolean doubleClick;

    public ScreenContainer(Container inventorySlotsIn) {
        this.inventorySlots = inventorySlotsIn;
        this.ignoreMouseUp = true;
    }

    /**
     * Compute the new stack size, Returns the stack with the new size. Args : dragSlots, dragMode, dragStack,
     * slotStackSize
     */
    public static void computeStackSize(Set<Slot> dragSlotsIn, int dragModeIn, ItemStack stack, int slotStackSize) {
        switch (dragModeIn) {
            case 0:
                stack.setCount(MathHelper.floor((float) stack.getCount() / (float) dragSlotsIn.size()));
                break;
            case 1:
                stack.setCount(1);
                break;
            case 2:
                stack.setCount(stack.getMaxStackSize());
        }

        stack.grow(slotStackSize);
    }

    @Override
    public void initGui() {
        this.mc.player.openContainer = this.inventorySlots;
        super.initGui();
    }

    @Override
    public void drawForeground(float partialTicks, int mouseX, int mouseY) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();

        InventoryPlayer inventoryplayer = this.mc.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;

        if (!itemstack.isEmpty()) {
            String altText = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float)itemstack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);

                if (itemstack.isEmpty()) {
                    altText = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, mouseX - 8, mouseY - (this.draggedStack.isEmpty() ? 8 : 16), altText);
        }

        if (!this.returningStack.isEmpty()) {
            float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F)
            {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = (int) (this.returningStackDestSlot.getX() - this.touchUpX);
            int i3 = (int) (this.returningStackDestSlot.getY() - this.touchUpY);
            int l1 = this.touchUpX + (int)((float)l2 * f);
            int i2 = this.touchUpY + (int)((float)i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, (String)null);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void init() {

    }

    public void updateDragSplitting() {
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (!itemstack.isEmpty() && this.dragSplitting) {
            if (this.dragSplittingLimit == 2) {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            } else {
                this.dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : this.dragSplittingSlots) {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

                    if (itemstack1.getCount() > j) {
                        itemstack1.setCount(j);
                    }

                    this.dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }

    @Override
    public void typed(char typedChar, int keyCode) {
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
            this.mc.player.closeScreen();
        }

        this.checkHotbarKeys(keyCode);

        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode)) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            } else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode)) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            }
        }
    }

    /**
     * Checks whether a hotbar key (to swap the hovered item with an item in the hotbar) has been pressed. If so, it
     * swaps the given items.
     * Returns true if a hotbar key was pressed.
     */
    public boolean checkHotbarKeys(int keyCode) {
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
            for (int i = 0; i < 9; ++i) {
                if (this.mc.gameSettings.keyBindsHotbar[i].isActiveAndMatches(keyCode)) {
                    this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if it's possible to add the given itemstack to the given slot.
     */
    public static boolean canAddItemToSlot(@Nullable Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slotIn == null || !slotIn.getHasStack();

        if (!flag && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)) {
            return slotIn.getStack().getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= stack.getMaxStackSize();
        } else {
            return flag;
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    public void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if (slotIn != null) {
            slotId = slotIn.slotNumber;
        }

        this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, mouseButton, type, this.mc.player);
    }
    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    public void handleMouseClick(int slotId, int mouseButton, ClickType type) {
        this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, mouseButton, type, this.mc.player);
    }

    /**
     * Draws an ItemStack.
     *
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    public void drawItemStack(ItemStack stack, int x, int y, String altText) {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        if (this.mc.player != null) {
            this.inventorySlots.onContainerClosed(this.mc.player);
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();

        if (!this.mc.player.isEntityAlive() || this.mc.player.isDead) {
            this.mc.player.closeScreen();
        }
    }

    public void addBackpack(float x, float y) {
        InventoryPlayer playerInventory = Games.player().inventory;

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                addComponent(new Slot(inventorySlots, playerInventory, j1 + (l + 1) * 9).setPosition(x + 8 + j1 * 18, y + 84 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            addComponent(new Slot(inventorySlots, playerInventory, i1).setPosition( x + 8 + i1 * 18,  y + 142));
        }
    }
}
