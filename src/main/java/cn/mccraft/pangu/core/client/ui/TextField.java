package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;

@Accessors(chain = true)
public class TextField extends Component implements Focusable {
    @Getter
    @Setter
    protected FontProvider font = DefaultFontProvider.INSTANCE;

    @Getter
    @Setter
    protected String text = "";

    @Getter
    @Setter
    protected int maxStringLength = 32;

    @Getter
    @Setter
    protected int cursorCounter;

    /**
     * The current character index that should be used as start of the rendered text.
     */
    @Getter
    @Setter
    protected int lineScrollOffset;

    @Getter
    protected int cursorPosition;

    @Getter
    @Setter
    protected int selectionEnd;

    @Getter
    @Setter
    /** Called to check if the text is valid */
    protected Predicate<String> validator = Predicates.alwaysTrue();

    @Getter
    @Setter
    protected TextChangeEvent textChangeEvent;

    @Getter
    @Setter
    protected boolean enableBackgroundDrawing = true;


    public TextField(float width, float height) {
        super();
        setSize(width, height);
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        if (isFocused() && mouseButton == 0)
        {
            int i = (int) (mouseX - getX());

            if (this.enableBackgroundDrawing)
            {
                i -= 4;
            }

            String s = font.trimStringToWidth(this.text.substring(this.lineScrollOffset), (int) this.getWidth(), false);
            this.setCursorPosition(font.trimStringToWidth(s, i, false).length() + this.lineScrollOffset);
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (!isFocused()) return;

        if (GuiScreen.isKeyComboCtrlA(keyCode)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return;
        }

        if (GuiScreen.isKeyComboCtrlC(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return;
        }

        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            if (!isDisabled()) {
                this.writeText(GuiScreen.getClipboardString());
            }
            return;
        }

        if (GuiScreen.isKeyComboCtrlX(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());

            if (!isDisabled()) {
                this.writeText("");
            }
            return;
        }

        switch (keyCode) {
            case 14:

                if (GuiScreen.isCtrlKeyDown()) {
                    if (!isDisabled()) {
                        this.deleteWords(-1);
                    }
                } else if (!isDisabled()) {
                    this.deleteFromCursor(-1);
                }
                break;
            case 199:

                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(0);
                } else {
                    this.setCursorPositionZero();
                }
                break;
            case 203:

                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() - 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(-1));
                } else {
                    this.moveCursorBy(-1);
                }

                break;
            case 205:

                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() + 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(1));
                } else {
                    this.moveCursorBy(1);
                }
                break;
            case 207:

                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(this.text.length());
                } else {
                    this.setCursorPositionEnd();
                }

                break;
            case 211:

                if (GuiScreen.isCtrlKeyDown()) {
                    if (!isDisabled()) {
                        this.deleteWords(1);
                    }
                } else if (!isDisabled()) {
                    this.deleteFromCursor(1);
                }

                break;
            default:

                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    if (!isDisabled()) {
                        this.writeText(Character.toString(typedChar));
                    }
                }
        }
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        drawBackground();
        int fontColor = this.isDisabled() ? 7368816 : 14737632;
        int j = this.cursorPosition - this.lineScrollOffset;
        int k = this.selectionEnd - this.lineScrollOffset;
        String text2Render = font.trimStringToWidth(this.text.substring(this.lineScrollOffset), (int) this.getWidth(), false);
        boolean flag = j >= 0 && j <= text2Render.length();
        boolean flag1 = this.isFocused() && this.cursorCounter / 6 % 2 == 0 && flag;
        float l = this.enableBackgroundDrawing ? getX() + 4 : getX();
        float i1 = this.enableBackgroundDrawing ? getY() + (this.height - 8) / 2 : getY();
        float j1 = l;

        if (k > text2Render.length()) {
            k = text2Render.length();
        }

        if (!text2Render.isEmpty()) {
            String s1 = flag ? text2Render.substring(0, j) : text2Render;
            j1 = font.drawStringWithShadow(s1, l, i1, fontColor);
        }

        boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
        float k1 = j1;

        if (!flag) {
            k1 = j > 0 ? l + this.width : l;
        } else if (flag2) {
            k1 = j1 - 1;
            --j1;
        }

        if (!text2Render.isEmpty() && flag && j < text2Render.length()) {
            j1 = font.drawStringWithShadow(text2Render.substring(j), j1, i1, fontColor);
        }

        if (flag1) {
            if (flag2) {
                Rect.draw(k1, i1 - 1, k1 + 1, i1 + 1 + font.getFontHeight(), -3092272);
            } else {
                font.drawStringWithShadow("_", k1, i1, fontColor);
            }
        }

        if (k != j) {
            float l1 = l + font.getStringWidth(text2Render.substring(0, k));
            this.drawSelectionBox(k1, i1 - 1, l1 - 1, i1 + 1 + font.getFontHeight());
        }
    }

    protected void drawBackground() {
        if (enableBackgroundDrawing) {
            Rect.draw(getX() - 1, getY() - 1, getX() + this.width + 1, getY() + this.height + 1, -6250336);
            Rect.draw(getX(), getY(), getX() + this.width, getY() + this.height, -16777216);
        }
    }

    /**
     * Draws the blue selection box.
     */
    protected void drawSelectionBox(float startX, float startY, float endX, float endY) {
        if (startX < endX) {
            float i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY) {
            float j = startY;
            startY = endY;
            endY = j;
        }

        if (endX > getX() + this.width) {
            endX = getX() + this.width;
        }

        if (startX > getX() + this.width) {
            startX = getX() + this.width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
        bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }


    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText() {
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }

    /**
     * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
     */
    public void writeText(String textToWrite) {
        String s = "";
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.text.length() - (i - j);

        if (!this.text.isEmpty()) {
            s = s + this.text.substring(0, i);
        }

        int l;

        if (k < s1.length()) {
            s = s + s1.substring(0, k);
            l = k;
        } else {
            s = s + s1;
            l = s1.length();
        }

        if (!this.text.isEmpty() && j < this.text.length()) {
            s = s + this.text.substring(j);
        }

        if (this.validator.apply(s)) {
            if (textChangeEvent == null || textChangeEvent.onTextChange(s, this.text)) {
                this.text = s;
                this.moveCursorBy(i - this.selectionEnd + l);
            }
        }
    }


    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int num) {
        this.setCursorPosition(this.selectionEnd + num);
    }

    /**
     * Sets the current position of the cursor.
     */
    public void setCursorPosition(int pos) {
        this.cursorPosition = pos;
        int i = this.text.length();
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
        this.setSelectionPos(this.cursorPosition);
    }

    /**
     * Moves the cursor to the very start of this text box.
     */
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    /**
     * Moves the cursor to the very end of this text box.
     */
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    /**
     * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
     * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
     */
    public void setSelectionPos(int position) {
        int i = this.text.length();

        if (position > i) {
            position = i;
        }

        if (position < 0) {
            position = 0;
        }

        this.selectionEnd = position;

        if (this.font != null) {
            if (this.lineScrollOffset > i) {
                this.lineScrollOffset = i;
            }

            int j = (int) this.getWidth();
            String s = font.trimStringToWidth(this.text.substring(this.lineScrollOffset), j, false);
            int k = s.length() + this.lineScrollOffset;

            if (position == this.lineScrollOffset) {
                this.lineScrollOffset -= font.trimStringToWidth(this.text, j, true).length();
            }

            if (position > k) {
                this.lineScrollOffset += position - k;
            } else if (position <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - position;
            }

            this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
        }
    }

    /**
     * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
     * which case the selection is deleted instead.
     */
    public void deleteWords(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
            }
        }
    }

    /**
     * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
     * in which case the selection is deleted instead.
     */
    public void deleteFromCursor(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean flag = num < 0;
                int i = flag ? this.cursorPosition + num : this.cursorPosition;
                int j = flag ? this.cursorPosition : this.cursorPosition + num;
                String s = "";

                if (i >= 0) {
                    s = this.text.substring(0, i);
                }

                if (j < this.text.length()) {
                    s = s + this.text.substring(j);
                }

                if (this.validator.apply(s)) {
                    this.text = s;

                    if (flag) {
                        this.moveCursorBy(num);
                    }

                }
            }
        }
    }

    /**
     * Gets the starting index of the word at the specified number of words away from the cursor position.
     */
    public int getNthWordFromCursor(int numWords) {
        return this.getNthWordFromPos(numWords, this.getCursorPosition());
    }

    /**
     * Gets the starting index of the word at a distance of the specified number of words away from the given position.
     */
    public int getNthWordFromPos(int n, int pos) {
        return this.getNthWordFromPosWS(n, pos, true);
    }

    /**
     * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
     */
    public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
        int i = pos;
        boolean flag = n < 0;
        int j = Math.abs(n);

        for (int k = 0; k < j; ++k) {
            if (!flag) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);

                if (i == -1) {
                    i = l;
                } else {
                    while (skipWs && i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }
    @FunctionalInterface
    public interface TextChangeEvent {
        boolean onTextChange(String newText, String oldText);
    }
}
