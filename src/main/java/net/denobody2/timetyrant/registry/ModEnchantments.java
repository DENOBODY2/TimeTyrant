package net.denobody2.timetyrant.registry;

import net.denobody2.timetyrant.TimeTyrant;
import net.denobody2.timetyrant.common.enchantment.ExplosionEnchantment;
import net.denobody2.timetyrant.common.enchantment.StrikerEnchantment;
import net.denobody2.timetyrant.common.enchantment.TimeGodEnchantment;
import net.denobody2.timetyrant.common.item.TyrantBoltItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TimeTyrant.MOD_ID);

    public static final EnchantmentCategory BOLT = EnchantmentCategory.create("bolt", (item -> item instanceof TyrantBoltItem));

    public static final RegistryObject<Enchantment> STRIKING = ENCHANTMENTS.register("striking",
            () -> new StrikerEnchantment(Enchantment.Rarity.COMMON, BOLT, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> EXPLOSION = ENCHANTMENTS.register("explosion",
            () -> new ExplosionEnchantment(Enchantment.Rarity.UNCOMMON, BOLT, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> WRATH_OF_THE_TIME_GOD = ENCHANTMENTS.register("wrath_of_the_time_god",
            () -> new TimeGodEnchantment(Enchantment.Rarity.VERY_RARE, BOLT, EquipmentSlot.MAINHAND));
}
