package cn.mccraft.pangu.core.client.setting;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GuiOptionsRowList extends GuiListExtended {
    private final List<Row> options = Lists.newArrayList();

    public GuiOptionsRowList(Minecraft minecraft, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn, Option... options) {
        super(minecraft, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.centerListVertically = false;
        for (int i = 0; i < options.length; i += 2) {
            Option firstOption = options[i];
            Option secondOption = i < options.length - 1 ? options[i + 1] : null;
            GuiButton firstButton = this.createButton(widthIn / 2 - 155, 0, firstOption);
            GuiButton secondButton = this.createButton(widthIn / 2 - 155 + 160, 0, secondOption);
            this.options.add(new GuiOptionsRowList.Row(firstOption, firstButton, secondOption, secondButton));
        }
    }

    private GuiButton createButton(int x, int y, Option option) {
        if (option == null) return null;
        return option.createButton(x, y);
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiOptionsRowList.Row getListEntry(int index) {
        return this.options.get(index);
    }

    protected int getSize() {
        return this.options.size();
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return 400;
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        if (this.visible) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            int i = this.getScrollBarX();
            int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int l = this.top + 4 - (int) this.amountScrolled;

            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }

            this.drawSelectionBox(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();

            int j1 = this.getMaxScroll();

            if (j1 > 0) {
                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k1 = MathHelper.clamp(k1, 32, this.bottom - this.top - 8);
                int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

                if (l1 < this.top) {
                    l1 = this.top;
                }

                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }

            this.renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Row implements GuiListExtended.IGuiListEntry {
        private final Minecraft client = Minecraft.getMinecraft();
        private final Option firstOption, secondOption;
        private final GuiButton buttonA;
        private final GuiButton buttonB;

        public Row(Option firstOption, GuiButton buttonAIn, Option secondOption, GuiButton buttonBIn) {
            this.firstOption = firstOption;
            this.secondOption = secondOption;
            this.buttonA = buttonAIn;
            this.buttonB = buttonBIn;
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            if (this.buttonA != null) {
                this.buttonA.y = y;
                this.buttonA.drawButton(this.client, mouseX, mouseY, partialTicks);
            }

            if (this.buttonB != null) {
                this.buttonB.y = y;
                this.buttonB.drawButton(this.client, mouseX, mouseY, partialTicks);
            }
        }

        /**
         * Called when the mouse is clicked within this entry. Returning true means that something within this entry
         * was clicked and the list should not be dragged.
         */
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (this.buttonA.mousePressed(this.client, mouseX, mouseY)) {
                if (this.buttonA instanceof GuiOptionButton) {
                    firstOption.setValue(1);
                    this.buttonA.displayString = firstOption.getDisplayString();
                }

                return true;
            } else if (this.buttonB != null && this.buttonB.mousePressed(this.client, mouseX, mouseY)) {
                if (this.buttonB instanceof GuiOptionButton) {
                    secondOption.setValue(1);
                    this.buttonB.displayString = secondOption.getDisplayString();
                }
                return true;
            } else {
                return false;
            }
        }

        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.buttonA != null) {
                this.buttonA.mouseReleased(x, y);
            }

            if (this.buttonB != null) {
                this.buttonB.mouseReleased(x, y);
            }
        }

        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
        }
    }
}