package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.Games;
import cn.mccraft.pangu.core.util.image.TextureProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import cn.mccraft.pangu.core.util.render.RenderUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class Slot extends Component {
    /**
     * The inventory we want to extract a slot from.
     */
    protected final IInventory inventory;
    protected net.minecraft.inventory.Slot nativeSlot;
    /**
     * The index of the slot in the inventory.
     */
    protected final int index;
    /**
     * the id of the slot(also the index in the inventory arraylist)
     */
    public int slotNumber;
    protected ScreenContainer screenContainer;

    @Getter
    @Setter
    protected String slotName;

    @Getter
    @Setter
    protected TextureProvider background;

    public Slot(Container container, IInventory inventory, int index) {
        setSize(16, 16);
        this.inventory = inventory;
        this.index = index;
        for (net.minecraft.inventory.Slot inventorySlot : container.inventorySlots) {
            if (inventorySlot.inventory == inventory && inventorySlot.getSlotIndex() == index) {
                this.slotNumber = inventorySlot.slotNumber;
                this.nativeSlot = inventorySlot;
            }
        }
    }

    @Override
    public Component setScreen(Screen screen) {
        if (!(screen instanceof ScreenContainer)) {
            throw new RuntimeException("Slot only support with ScreenContainer");
        }
        this.screenContainer = (ScreenContainer) screen;
        return super.setScreen(screen);
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        if (isHovered()) {
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.colorMask(true, true, true, false);

            Rect.drawBox(getX(), getY(), 16, 16, 0x80ffffff);

            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }

        ItemStack itemstack = getStack();
        boolean flag = false;
        boolean flag1 = this == screenContainer.getClickedSlot() && !screenContainer.getDraggedStack().isEmpty() && !screenContainer.isRightMouseClick();
        ItemStack itemstack1 = Games.player().inventory.getItemStack();
        String s = null;

        if (this == screenContainer.getClickedSlot() && !screenContainer.getDraggedStack().isEmpty() && screenContainer.isRightMouseClick() && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (screenContainer.isDragSplitting() && screenContainer.getDragSplittingSlots().contains(this) && !itemstack1.isEmpty()) {
            if (screenContainer.getDragSplittingSlots().size() == 1) {
                return;
            }

            if (ScreenContainer.canAddItemToSlot(this, itemstack1, true) && screenContainer.getInventorySlots().canDragIntoSlot(nativeSlot)) {
                itemstack = itemstack1.copy();
                flag = true;
                ScreenContainer.computeStackSize(screenContainer.getDragSplittingSlots(), screenContainer.getDragSplittingLimit(), itemstack, getStack().isEmpty() ? 0 : getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), getItemStackLimit(itemstack));

                if (itemstack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                screenContainer.getDragSplittingSlots().remove(this);
                screenContainer.updateDragSplitting();
            }
        }


        RenderHelper.enableGUIStandardItemLighting();
        Rect.zLevel(100);
        RenderUtils.getRenderItem().zLevel = 100.0F;

        onDrawItemBackground(itemstack);

        onDrawItem(itemstack, flag, flag1, s);

        RenderUtils.getRenderItem().zLevel = 0.0F;
        Rect.zLevel();
    }

    public void onDrawItemBackground(ItemStack itemStack) {
        if (itemStack.isEmpty() && !isDisabled()) {
            if (slotName != null) {
                TextureAtlasSprite texture = Games.minecraft().getTextureMapBlocks().getAtlasSprite(slotName);
                GlStateManager.disableLighting();
                Rect.bind(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Rect.drawTextureAtlasSprite(getX(), getY(), texture, getWidth(), getHeight());
                GlStateManager.enableLighting();
            } else if (background != null) {
                GlStateManager.disableLighting();
                Rect.bind(background);
                Rect.drawFullTexTextured(getX(), getY(), getWidth(), getHeight());
                GlStateManager.enableLighting();
            }
        }
    }

    public void onDrawItem(ItemStack itemstack, boolean valid, boolean isDragged, String altText) {
        if (!isDragged) {
            if (valid) {
                Rect.drawBox(getX(), getY(), getWidth(), getHeight(), 0x80ffffff);
            }

            GlStateManager.enableDepth();
            RenderUtils.getRenderItem().renderItemAndEffectIntoGUI(Games.player(), itemstack, (int) getX(), (int) getY());
            RenderUtils.getRenderItem().renderItemOverlayIntoGUI(Games.minecraft().fontRenderer, itemstack, (int) getX(), (int) getY(), altText);
        }
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        boolean flag = Games.minecraft.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100);
        long i = Minecraft.getSystemTime();
        screenContainer.setDoubleClick(screenContainer.getLastClickSlot() == this && i - screenContainer.getLastClickTime() < 250L && screenContainer.getLastClickButton() == mouseButton);
        screenContainer.setIgnoreMouseUp(false);

        if (mouseButton == 0 || mouseButton == 1 || flag) {
            if (slotNumber != -1) {
                if (Games.minecraft.gameSettings.touchscreen) {
                    if (getHasStack()) {
                        screenContainer.setClickedSlot(this);
                        screenContainer.setDraggedStack(ItemStack.EMPTY);
                        screenContainer.setRightMouseClick(mouseButton == 1);
                    } else {
                        screenContainer.setClickedSlot(null);
                    }
                } else if (!screenContainer.isDragSplitting()) {
                    if (Games.minecraft.player.inventory.getItemStack().isEmpty()) {
                        if (Games.minecraft.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100)) {
                            screenContainer.handleMouseClick(this, slotNumber, mouseButton, ClickType.CLONE);
                        } else {
                            boolean flag2 = slotNumber != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            ClickType clicktype = ClickType.PICKUP;

                            if (flag2) {
                                screenContainer.setShiftClickedSlot(getHasStack() ? getStack().copy() : ItemStack.EMPTY);
                                clicktype = ClickType.QUICK_MOVE;
                            } else if (slotNumber == -999) {
                                clicktype = ClickType.THROW;
                            }

                            screenContainer.handleMouseClick(this, slotNumber, mouseButton, clicktype);
                        }

                        screenContainer.setIgnoreMouseUp(true);
                    } else {
                        screenContainer.setDragSplitting(true);
                        screenContainer.setDragSplittingButton(mouseButton);
                        screenContainer.getDragSplittingSlots().clear();

                        if (mouseButton == 0) {
                            screenContainer.setDragSplittingLimit(0);
                        } else if (mouseButton == 1) {
                            screenContainer.setDragSplittingLimit(1);
                        } else if (Games.minecraft.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100)) {
                            screenContainer.setDragSplittingLimit(2);
                        }
                    }
                }
            }
        }

        screenContainer.setLastClickSlot(this);
        screenContainer.setLastClickTime(i);
        screenContainer.setLastClickButton(mouseButton);
    }

    @Override
    public void onMouseReleased(int state, int mouseX, int mouseY) {
        int k = slotNumber;

        if (screenContainer.isDoubleClick() && state == 0 && screenContainer.getInventorySlots().canMergeSlot(ItemStack.EMPTY, nativeSlot)) {
            if (ScreenContainer.isShiftKeyDown()) {
                if (!screenContainer.getShiftClickedSlot().isEmpty()) {
                    for (net.minecraft.inventory.Slot slot2 : screenContainer.getInventorySlots().inventorySlots) {
                        if (slot2 != null && slot2.canTakeStack(Games.minecraft().player) && slot2.getHasStack() && slot2.inventory == inventory && net.minecraft.inventory.Container.canAddItemToSlot(slot2, screenContainer.getShiftClickedSlot(), true)) {
                            screenContainer.handleMouseClick(slot2.slotNumber, state, ClickType.QUICK_MOVE);
                        }
                    }
                }
            } else {
                screenContainer.handleMouseClick(this, k, state, ClickType.PICKUP_ALL);
            }

            screenContainer.setDoubleClick(false);
            screenContainer.setLastClickTime(0);
        } else {
            if (screenContainer.isDragSplitting() && screenContainer.getDragSplittingButton() != state) {
                screenContainer.setDragSplitting(false);
                screenContainer.getDragSplittingSlots().clear();
                screenContainer.setIgnoreMouseUp(true);
                return;
            }

            if (screenContainer.isIgnoreMouseUp()) {
                screenContainer.setIgnoreMouseUp(false);
                return;
            }

            if (screenContainer.getClickedSlot() != null && Games.minecraft().gameSettings.touchscreen) {
                if (state == 0 || state == 1) {
                    if (screenContainer.getDraggedStack().isEmpty() && this != screenContainer.getClickedSlot()) {
                        screenContainer.setDraggedStack(screenContainer.getClickedSlot().getStack());
                    }

                    boolean flag2 = canAddItemToSlot(this, screenContainer.getDraggedStack(), false);

                    if (k != -1 && !screenContainer.getDraggedStack().isEmpty() && flag2) {
                        screenContainer.handleMouseClick(screenContainer.getClickedSlot(), screenContainer.getClickedSlot().slotNumber, state, ClickType.PICKUP);
                        screenContainer.handleMouseClick(this, k, 0, ClickType.PICKUP);

                        if (Games.minecraft().player.inventory.getItemStack().isEmpty()) {
                            screenContainer.setReturningStack(ItemStack.EMPTY);
                        } else {
                            screenContainer.handleMouseClick(screenContainer.getClickedSlot(), screenContainer.getClickedSlot().slotNumber, state, ClickType.PICKUP);
                            screenContainer.setTouchUpX((int) (mouseX - getX()));
                            screenContainer.setTouchUpX((int) (mouseY - getY()));
                            screenContainer.setReturningStackDestSlot(screenContainer.getClickedSlot());
                            screenContainer.setReturningStack(screenContainer.getDraggedStack());
                            screenContainer.setReturningStackTime(Minecraft.getSystemTime());
                        }
                    } else if (!screenContainer.getDraggedStack().isEmpty()) {
                        screenContainer.setTouchUpX((int) (mouseX - getX()));
                        screenContainer.setTouchUpX((int) (mouseY - getY()));
                        screenContainer.setReturningStackDestSlot(screenContainer.getClickedSlot());
                        screenContainer.setReturningStack(screenContainer.getDraggedStack());
                        screenContainer.setReturningStackTime(Minecraft.getSystemTime());
                    }

                    screenContainer.setDraggedStack(ItemStack.EMPTY);
                    screenContainer.setClickedSlot(null);
                }
            } else if (screenContainer.isDragSplitting() && !screenContainer.getDragSplittingSlots().isEmpty()) {
                screenContainer.handleMouseClick(null, -999, net.minecraft.inventory.Container.getQuickcraftMask(0, screenContainer.getDragSplittingLimit()), ClickType.QUICK_CRAFT);

                for (Slot slot1 : screenContainer.getDragSplittingSlots()) {
                    screenContainer.handleMouseClick(slot1, slot1.slotNumber, net.minecraft.inventory.Container.getQuickcraftMask(1, screenContainer.getDragSplittingLimit()), ClickType.QUICK_CRAFT);
                }

                screenContainer.handleMouseClick(null, -999, Container.getQuickcraftMask(2, screenContainer.getDragSplittingLimit()), ClickType.QUICK_CRAFT);
            } else if (!Games.minecraft().player.inventory.getItemStack().isEmpty()) {
                if (Games.minecraft().gameSettings.keyBindPickBlock.isActiveAndMatches(state - 100)) {
                    screenContainer.handleMouseClick(this, k, state, ClickType.CLONE);
                } else {
                    boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

                    if (flag1) {
                        screenContainer.setShiftClickedSlot(getHasStack() ? getStack().copy() : ItemStack.EMPTY);
                    }

                    screenContainer.handleMouseClick(this, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }

        if (Games.minecraft().player.inventory.getItemStack().isEmpty()) {
            screenContainer.setLastClickTime(0);
        }

        screenContainer.setDragSplitting(false);
    }

    /**
     * if par2 has more items than par1, onCrafting(item,countIncrease) is called
     */
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
        int i = p_75220_2_.getCount() - p_75220_1_.getCount();

        if (i > 0) {
            this.onCrafting(p_75220_2_, i);
        }
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack stack, int amount) {
    }

    protected void onSwapCraft(int p_190900_1_) {
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        this.onSlotChanged();
        return stack;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.index);
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack() {
        return !this.getStack().isEmpty();
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(ItemStack stack) {
        this.inventory.setInventorySlotContents(this.index, stack);
        this.onSlotChanged();
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged() {
        this.inventory.markDirty();
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }

    public int getItemStackLimit(ItemStack stack) {
        return this.getSlotStackLimit();
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int amount) {
        return this.inventory.decrStackSize(this.index, amount);
    }

    /**
     * returns true if the slot exists in the given inventory and location
     */
    public boolean isHere(IInventory inv, int slotIn) {
        return inv == this.inventory && slotIn == this.index;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public List<String> getToolTips() {
        ItemStack stack = getStack();
        if (stack == null || stack.isEmpty()) return null;
        return getItemToolTip(stack);
    }

    public List<String> getItemToolTip(ItemStack itemStack) {
        List<String> list = itemStack.getTooltip(Games.player(), Games.minecraft().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) list.set(i, itemStack.getRarity().color + list.get(i));
            else list.set(i, TextFormatting.GRAY + list.get(i));
        }

        return list;
    }

    public void setEquipmentSlotTexture(EntityEquipmentSlot slot) {
        setSlotName(ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()]);
    }

    public static boolean canAddItemToSlot(@Nullable Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slotIn == null || !slotIn.getHasStack();

        if (!flag && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)) {
            return slotIn.getStack().getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= stack.getMaxStackSize();
        } else {
            return flag;
        }
    }
}
