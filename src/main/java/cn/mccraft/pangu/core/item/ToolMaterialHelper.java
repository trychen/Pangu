package cn.mccraft.pangu.core.item;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ToolMaterialHelper {
    @Nullable
    static Item.ToolMaterial of(@Nonnull String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability) {
        return EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damage, enchantability);
    }

    @Nonnull
    static Builder builder(@Nonnull String name) {
        return new Builder(name).init(Item.ToolMaterial.STONE);
    }


    @Nonnull
    static Builder builder(@Nonnull String name, Item.ToolMaterial material) {
        return new Builder(name).init(material);
    }

    /**
     * A chain builder to add ToolMaterial simply
     */
    @Accessors(chain = true)
    @Data
    class Builder {
        @NonNull
        private String name;
        /**
         * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = WOOD/GOLD)
         */
        private int harvestLevel;
        /**
         * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
         */
        private int maxUses;
        /**
         * The strength of this tool material against blocks which it is effective against.
         */
        private float efficiency;
        /**
         * Damage versus entities.
         */
        private float attackDamage;
        /**
         * Defines the natural enchantability factor of the material.
         */
        private int enchantability;

        public Item.ToolMaterial build() {
            return ToolMaterialHelper.of(name, harvestLevel, maxUses, efficiency, attackDamage, enchantability);
        }

        public String getName() {
            return name;
        }

        public Builder init(Item.ToolMaterial material) {
            this.harvestLevel = material.getHarvestLevel();
            this.maxUses = material.getMaxUses();
            this.efficiency = material.getEfficiency();
            this.attackDamage = material.getAttackDamage();
            this.enchantability = material.getEnchantability();
            return this;
        }
    }

}
