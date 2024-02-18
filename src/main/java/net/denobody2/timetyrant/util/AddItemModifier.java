package net.denobody2.timetyrant.util;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.denobody2.timetyrant.common.enchantment.ExplosionEnchantment;
import net.denobody2.timetyrant.common.enchantment.StrikerEnchantment;
import net.denobody2.timetyrant.common.enchantment.TimeGodEnchantment;
import net.denobody2.timetyrant.registry.ModEnchantments;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ENCHANTMENTS.getCodec().fieldOf("enchantment").forGetter(m -> m.enchantment)).apply(inst, AddItemModifier::new)));
    private final Enchantment enchantment;
    public AddItemModifier(LootItemCondition[] conditionsIn, Enchantment enchantment) {
        super(conditionsIn);
        this.enchantment = enchantment;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if(!condition.test(context)) {
                return generatedLoot;
            }
        }
        if (context.getRandom().nextFloat() < getChance()){
            ItemStack toreturn = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, 1));
            if(enchantment instanceof StrikerEnchantment){
                toreturn = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, 2));
            } else if(enchantment instanceof ExplosionEnchantment){
                toreturn = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, 3));
            } else if(enchantment instanceof TimeGodEnchantment){
                toreturn = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, 1));
            }
            generatedLoot.add(toreturn);
        }
        return generatedLoot;
    }
    private float getChance() {
        return 0.3f;
    }
    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
