package net.denobody2.timetyrant;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import net.denobody2.timetyrant.event.ModClientEvents;
import net.denobody2.timetyrant.registry.*;
import net.denobody2.timetyrant.util.BakedModelShadeLayerFullbright;
import net.denobody2.timetyrant.util.ModPlayerCapes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TimeTyrant.MOD_ID)
public class TimeTyrant
{
    //Todo
    //textures, look for bugs
    //model and texture for bolt


    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "timetyrant";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public TimeTyrant()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModEnchantments.ENCHANTMENTS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(this::bakeModels);
    }
    private static final List<String> FULLBRIGHTS = ImmutableList.of("timetyrant:deepslate_time_shard_ore#");
    public void bakeModels(final ModelEvent.ModifyBakingResult e) {
        long time = System.currentTimeMillis();
        for (ResourceLocation id : e.getModels().keySet()) {
            if (FULLBRIGHTS.stream().anyMatch(str -> id.toString().startsWith(str))) {
                e.getModels().put(id, new BakedModelShadeLayerFullbright(e.getModels().get(id)));
            }
        }
        System.out.println("called");
        TimeTyrant.LOGGER.info("Loaded emissive block models in {} ms", System.currentTimeMillis() - time);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //the
        event.enqueueWork(ModPlayerCapes::setup);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
