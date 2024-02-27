package net.denobody2.timetyrant.registry;

import net.denobody2.timetyrant.TimeTyrant;
import net.denobody2.timetyrant.common.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TimeTyrant.MOD_ID);
    public static final RegistryObject<Item> DEBUG = ITEMS.register("debug",
            () -> new DebugItem(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> CELESTIAL_TIME_REMOTE = ITEMS.register("celestial_time_remote",
            () -> new CelestialTimeRemoteItem(new Item.Properties().fireResistant().stacksTo(1)));
    public static final RegistryObject<Item> UNIVERSAL_TIME_REMOTE = ITEMS.register("universal_time_remote",
            () -> new UniversalTimeRemoteItem(new Item.Properties().fireResistant().stacksTo(1)));
    public static final RegistryObject<Item> TIME_SHARD = ITEMS.register("time_shard",
            () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> TYRANT_BOLT = ITEMS.register("tyrant_bolt", ()-> new TyrantBoltItem(new Item.Properties().stacksTo(1).fireResistant().durability(3000)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
