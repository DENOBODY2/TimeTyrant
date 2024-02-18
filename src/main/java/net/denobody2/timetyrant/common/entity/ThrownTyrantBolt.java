package net.denobody2.timetyrant.common.entity;

import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import com.github.alexthe666.citadel.server.tick.modifier.LocalPositionTickRateModifier;
import net.denobody2.timetyrant.registry.ModEnchantments;
import net.denobody2.timetyrant.registry.ModEntities;
import net.denobody2.timetyrant.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThrownTyrantBolt extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTyrantBolt.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ID_STRIKER = SynchedEntityData.defineId(ThrownTyrantBolt.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ID_EXPLOSION = SynchedEntityData.defineId(ThrownTyrantBolt.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ID_TIME_GOD = SynchedEntityData.defineId(ThrownTyrantBolt.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTyrantBolt.class, EntityDataSerializers.BOOLEAN);
    private ItemStack boltItem = new ItemStack(ModItems.TYRANT_BOLT.get());
    private boolean triggered;
    private boolean activated;
    private int boltTickCount;
    private boolean dealtDamage;
    private boolean hitEntity;
    private BlockPos hitEntitypos;
    public int clientSideReturnTridentTickCount;
    public ThrownTyrantBolt(EntityType<? extends ThrownTyrantBolt> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownTyrantBolt(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(ModEntities.THROWN_TYRANT_BOLT.get(), pShooter, pLevel);
        this.boltItem = pStack.copy();
        this.entityData.set(ID_LOYALTY, (byte) 3);
        this.entityData.set(ID_EXPLOSION, (byte) EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.EXPLOSION.get(), boltItem));
        this.entityData.set(ID_STRIKER, (byte)  EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.STRIKING.get(), boltItem));
        this.entityData.set(ID_TIME_GOD, (byte)  EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.WRATH_OF_THE_TIME_GOD.get(), boltItem));
        this.entityData.set(ID_FOIL, pStack.hasFoil());
        this.triggered = false;
        this.activated = false;
        this.hitEntity = false;
        this.boltTickCount = 0;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte)3);
        this.entityData.define(ID_EXPLOSION, (byte)0);
        this.entityData.define(ID_STRIKER, (byte)0);
        this.entityData.define(ID_TIME_GOD, (byte)0);
        this.entityData.define(ID_FOIL, false);
    }
    public void tick() {
        if (this.inGroundTime > 18) {
            this.dealtDamage = true;
        }
        if(this.inGroundTime > 1){
            if(!dealtDamage){
                if(!hitEntity){
                    if(this.inGroundTime == 2){
                        fireBolt();
                    } else if(this.inGroundTime == 5 && this.entityData.get(ID_STRIKER) == 1){
                        fireBolt();
                    } else if(this.inGroundTime == 9 && this.entityData.get(ID_STRIKER) == 2){
                        fireBolt();
                    } else if(this.inGroundTime == 16 && this.entityData.get(ID_EXPLOSION) > 0){
                        level().explode(this.getOwner(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), this.entityData.get(ID_EXPLOSION) + 1.0F, Level.ExplosionInteraction.NONE);
                    }
                }
            }
        }

        if(activated){
            boltTickCount++;
            if(this.boltTickCount == 2){
                fireBoltEntity();
            } else if(this.boltTickCount == 5 && this.entityData.get(ID_STRIKER) == 1){
                fireBoltEntity();
            } else if(this.boltTickCount == 9 && this.entityData.get(ID_STRIKER) == 2){
                fireBoltEntity();
            } else if(this.boltTickCount == 16 && this.entityData.get(ID_EXPLOSION) > 0){
                level().explode(this.getOwner(), this.hitEntitypos.getX(), this.hitEntitypos.getY(), this.hitEntitypos.getZ(), this.entityData.get(ID_EXPLOSION), Level.ExplosionInteraction.NONE);
            }
            if(boltTickCount > 17){
                this.dealtDamage = true;
            }
        }
        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }
        super.tick();
    }
    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }
    protected void fireBolt(){
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        BlockPos blockpos = this.blockPosition();
        Float f1 = 1.0F;
        Entity entity1 = this.getOwner();
        for(int i = 0; i < 3; i++){
            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
            if (lightningbolt != null) {
                lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer)entity1 : null);
                this.level().addFreshEntity(lightningbolt);
                soundevent = SoundEvents.TRIDENT_THUNDER;
            }
        }
        this.playSound(soundevent, f1, 1.0F);
    }
    protected void fireBoltEntity(){
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        BlockPos blockpos = this.hitEntitypos;
        Float f1 = 1.0F;
        Entity entity1 = this.getOwner();
        for(int i = 0; i < 3; i++){
            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
            if (lightningbolt != null) {
                lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer)entity1 : null);
                this.level().addFreshEntity(lightningbolt);
                soundevent = SoundEvents.TRIDENT_THUNDER;
            }
        }
        this.playSound(soundevent, f1, 1.0F);
    }

    protected ItemStack getPickupItem() {
        return this.boltItem.copy();
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = 9.0F;
        Entity entity1 = this.getOwner();
        //DamageSource damagesource2 = this.damageSources().trident(this, (Entity)(entity1 == null ? this : entity1));
        DamageSource damagesource = this.damageSources().magic();
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
                }
                this.doPostHurtEffects(livingentity1);
            }
        }
        if (this.entityData.get(ID_TIME_GOD) > 0){
            slowTime(entity, this.level());
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 10, true, false), entity1);
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        hitEntity = true;
        activated = true;
        hitEntitypos = entity.blockPosition();
        this.playSound(soundevent, f1, 1.0F);
    }
    protected boolean tryPickup(Player pPlayer) {
        return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer) && pPlayer.getInventory().add(this.getPickupItem());
    }
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }
    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }
    }
    public void slowTime(Entity entity, Level level){
        ServerTickRateTracker.modifyTickRate(level, new LocalPositionTickRateModifier(new Vec3(entity.blockPosition().getX(), entity.blockPosition().getY(), entity.blockPosition().getZ()) ,3.0F, level.dimension() ,200, 10F));
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Bolt", 10)) {
            this.boltItem = ItemStack.of(pCompound.getCompound("Bolt"));
        }
        this.dealtDamage = pCompound.getBoolean("DealtDamage");
        this.activated = pCompound.getBoolean("Activated");
        this.boltTickCount = pCompound.getInt("Bolt Tick");
        this.triggered = pCompound.getBoolean("Triggered");
        this.hitEntity = pCompound.getBoolean("Hit Entity");
        this.entityData.set(ID_LOYALTY, (byte)3);
        this.entityData.set(ID_EXPLOSION, (byte) EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.EXPLOSION.get(), boltItem));
        this.entityData.set(ID_STRIKER, (byte)  EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.STRIKING.get(), boltItem));
        this.entityData.set(ID_TIME_GOD, (byte)  EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.WRATH_OF_THE_TIME_GOD.get(), boltItem));
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("Bolt", this.boltItem.save(new CompoundTag()));
        pCompound.putBoolean("DealtDamage", this.dealtDamage);
        pCompound.putBoolean("Activated", this.activated);
        pCompound.putInt("Bolt Tick", this.boltTickCount);
        pCompound.putBoolean("Triggered", this.triggered);
        pCompound.putBoolean("Hit Entity", this.hitEntity);
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
}
