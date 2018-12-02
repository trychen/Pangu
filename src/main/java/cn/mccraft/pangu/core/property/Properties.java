package cn.mccraft.pangu.core.property;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

/**
 * Some common properties
 *
 * @since 1.3
 */
public interface Properties {
    /**
     * For blocks with double-structure like door
     */
    PropertyEnum<EnumHalf> HALF = PropertyEnum.create("half", EnumHalf.class);

    /**
     * enum for {@link Properties#HALF}
     */
    enum EnumHalf implements IStringSerializable {
        UPPER,
        LOWER;

        public String toString() {
            return this.getName();
        }

        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }

    /**
     * For blocks to indicate if powered
     */
    PropertyBool POWERED = PropertyBool.create("powered");

    /**
     * For block having specify facing
     */
    PropertyDirection FACING = BlockHorizontal.FACING;
}
