package net.denobody2.timetyrant.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.denobody2.timetyrant.common.entity.ThrownTyrantBolt;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TyrantBoltItem extends Item implements Vanishable {
    public static final int THROW_THRESHOLD_TIME = 9;
    public static final float BASE_DAMAGE = 8.0F;
    public static final float SHOOT_POWER = 2.5F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public TyrantBoltItem(Item.Properties pProperties) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 9.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.8F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 64800;
    }
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            if (i >= 10) {
                if (!pLevel.isClientSide) {
                    pStack.hurtAndBreak(1, player, (p_43388_) -> {
                        p_43388_.broadcastBreakEvent(pEntityLiving.getUsedItemHand());
                    });
                    ThrownTyrantBolt throwntrident = new ThrownTyrantBolt(pLevel, player, pStack);
                    throwntrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F * 0.6F, 0.6F);
                    if (player.getAbilities().instabuild) {
                        throwntrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    pLevel.addFreshEntity(throwntrident);
                    pLevel.playSound((Player)null, throwntrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(pStack);
                    }
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (p_43414_) -> {
            p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    /**
     * Called when a {@link net.minecraft.world.level.block.Block} is destroyed using this Item. Return {@code true} to
     * trigger the "Use Item" statistic.
     */
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if ((double)pState.getDestroySpeed(pLevel, pPos) != 0.0D) {
            pStack.hurtAndBreak(2, pEntityLiving, (p_43385_) -> {
                p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.getCooldowns().isOnCooldown(this)){
            fireBolt(player, player.level(), entity);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
    protected void fireBolt(Player player, Level level, Entity entity){
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        BlockPos blockpos = entity.blockPosition();
        Float f1 = 1.0F;
        Entity entity1 = player;
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
        if (lightningbolt != null) {
            lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
            lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer)entity1 : null);
            level.addFreshEntity(lightningbolt);
            soundevent = SoundEvents.TRIDENT_THUNDER;
            player.getCooldowns().addCooldown(this, 20);
        }
        player.playSound(soundevent, f1, 1.0F);
    }
}
