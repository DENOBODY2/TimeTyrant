package net.denobody2.timetyrant.registry;

import net.denobody2.timetyrant.TimeTyrant;
import net.denobody2.timetyrant.common.entity.ThrownTyrantBolt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TimeTyrant.MOD_ID);
    public static final RegistryObject<EntityType<ThrownTyrantBolt>> THROWN_TYRANT_BOLT =
            registerEntity(EntityType.Builder.<ThrownTyrantBolt>of(ThrownTyrantBolt::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20), "thrown_tyrant_bolt");
    public static final <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName){
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}
