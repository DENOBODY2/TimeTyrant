package net.denobody2.timetyrant.util;

import net.denobody2.timetyrant.TimeTyrant;
import net.denobody2.timetyrant.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier TYRANT = TierSortingRegistry.registerTier(
            new ForgeTier(6, 3000, 8.5f, 5f, 30,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(ModItems.TIME_SHARD.get())),
            new ResourceLocation(TimeTyrant.MOD_ID, "tyrant"), List.of(Tiers.NETHERITE), List.of());
}
