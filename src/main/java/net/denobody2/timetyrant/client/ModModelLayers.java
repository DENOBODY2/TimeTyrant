package net.denobody2.timetyrant.client;

import net.denobody2.timetyrant.TimeTyrant;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {

    public static final ModelLayerLocation TYRANT_BOLT = create("tyrant_bolt");


    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(new ResourceLocation(TimeTyrant.MOD_ID, name), "main");
    }
}
