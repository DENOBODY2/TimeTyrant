package net.denobody2.timetyrant.common.enchantment;

import net.denobody2.timetyrant.registry.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ExplosionEnchantment extends Enchantment {

    public ExplosionEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return super.canEnchant(pStack);
    }
    @Override
    public int getMinCost(int pLevel) {
        return super.getMinCost(pLevel);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.WRATH_OF_THE_TIME_GOD.get();
    }
}
