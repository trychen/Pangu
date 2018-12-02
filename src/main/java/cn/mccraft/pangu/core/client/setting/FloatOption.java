package cn.mccraft.pangu.core.client.setting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;

public class FloatOption extends Option<Float> {
    private final float valueStep;
    private float valueMin;
    private float valueMax;


    @Override
    public String getDisplayString() {
        return getValue() == 0.0F ? getTranslation() + I18n.format("options.off") : getTranslation() + getValue();
    }

    @Override
    public GuiButton createButton(int x, int y) {
        return new GuiOptionSlider(getOrdinal(), x, y, this);
    }

    public FloatOption(String translation) {
        this(translation, 0.0F, 1.0F, 0.0F);
    }

    public FloatOption(String translation, float valMin, float valMax, float valStep) {
        super(translation);
        this.valueMin = valMin;
        this.valueMax = valMax;
        this.valueStep = valStep;
    }

    public float getValueMin() {
        return this.valueMin;
    }

    public float getValueMax() {
        return this.valueMax;
    }

    public void setValueMax(float value) {
        this.valueMax = value;
    }

    public float normalizeValue(float value) {
        return MathHelper.clamp((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
    }

    public float denormalizeValue(float value) {
        return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(value, 0.0F, 1.0F));
    }

    public float snapToStepClamp(float value) {
        value = this.snapToStep(value);
        return MathHelper.clamp(value, this.valueMin, this.valueMax);
    }

    private float snapToStep(float value) {
        if (this.valueStep > 0.0F) {
            value = this.valueStep * (float) Math.round(value / this.valueStep);
        }

        return value;
    }

    public Float getValue() {
        return 0F;
    }

    public void setValue(Float valueMin) {

    }
}