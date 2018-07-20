package cn.mccraft.pangu.core.item;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nullable;

public interface ToolMaterialHelper {
    @Nullable
    static Item.ToolMaterial of(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability) {
        return EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damage, enchantability);
    }

    /**
     * A chain builder to add ToolMaterial simply
     */
    class Builder {
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

        /**
         * @param name the name of ToolMaterial ()
         */
        public Builder(String name) {
            this(name, Item.ToolMaterial.STONE);
        }

        public Builder(String name, Item.ToolMaterial material) {
            this.name = name;
            this.harvestLevel = material.getHarvestLevel();
            this.maxUses = material.getMaxUses();
            this.efficiency = material.getEfficiency();
            this.attackDamage = material.getAttackDamage();
            this.enchantability = material.getEnchantability();
        }

        public Item.ToolMaterial build() {
            return ToolMaterialHelper.of(name, harvestLevel, maxUses, efficiency, attackDamage, enchantability);
        }

        public String getName() {
            return name;
        }

        /**
         * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
         */
        public Builder setMaxUses(int maxUses) {
            this.maxUses = maxUses;
            return this;
        }

        /**
         * The strength of this tool material against blocks which it is effective against.
         */
        public Builder setEfficiency(float efficiency) {
            this.efficiency = efficiency;
            return this;
        }

        /**
         * The damage against a given entity.
         */
        public Builder setAttackDamage(float attackDamage) {
            this.attackDamage = attackDamage;
            return this;
        }

        /**
         * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = IRON/GOLD)
         */
        public Builder setHarvestLevel(int harvestLevel) {
            this.harvestLevel = harvestLevel;
            return this;
        }

        /**
         * Return the natural enchantability factor of the material.
         */
        public Builder setEnchantability(int enchantability) {
            this.enchantability = enchantability;
            return this;
        }
    }

}
