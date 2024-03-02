package net.denobody2.timetyrant.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.denobody2.timetyrant.TimeTyrant;
import net.denobody2.timetyrant.client.ModModelLayers;
import net.denobody2.timetyrant.client.model.TyrantBoltModel;
import net.denobody2.timetyrant.common.entity.ThrownTyrantBolt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TyrantBoltRenderer<T extends ThrownTyrantBolt> extends EntityRenderer<T> {

    private static final ResourceLocation LOC = new ResourceLocation(TimeTyrant.MOD_ID, "textures/entity/tyrant_bolt.png");
    protected final TyrantBoltModel<T> model;

    public TyrantBoltRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.shadowRadius = 0.7F;
        this.model = new TyrantBoltModel<>(pContext.bakeLayer(ModModelLayers.TYRANT_BOLT));
    }
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pMatrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        pMatrixStack.scale(1.0F, 1.0F, 1.0F);
        this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return LOC;
    }
}
