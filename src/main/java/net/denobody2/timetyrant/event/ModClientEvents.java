package net.denobody2.timetyrant.event;

import com.google.common.collect.ImmutableList;
import net.denobody2.timetyrant.TimeTyrant;
import net.denobody2.timetyrant.client.ModModelLayers;
import net.denobody2.timetyrant.client.model.TyrantBoltModel;
import net.denobody2.timetyrant.client.renderer.TyrantBoltRenderer;
import net.denobody2.timetyrant.registry.ModEntities;
import net.denobody2.timetyrant.util.BakedModelShadeLayerFullbright;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = TimeTyrant.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {
    private static final List<String> FULLBRIGHTS = ImmutableList.of("timetyrant:deepslate_time_shard_ore#");

    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers e) {
        // "Vanilla" format models
        e.registerEntityRenderer(ModEntities.THROWN_TYRANT_BOLT.get(), TyrantBoltRenderer::new);
        // Block entity models
        // Geckolib models
    }
    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(ModModelLayers.TYRANT_BOLT, TyrantBoltModel::createBodyLayer);
    }




}
