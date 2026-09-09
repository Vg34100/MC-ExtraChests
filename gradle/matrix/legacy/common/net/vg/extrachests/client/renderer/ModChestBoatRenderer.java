package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.RaftModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.joml.Quaternionf;

public final class ModChestBoatRenderer extends EntityRenderer<Boat> {
    private final ResourceLocation texture;
    private final ListModel<Boat> model;

    public ModChestBoatRenderer(EntityRendererProvider.Context context, Boat.Type type,
                                boolean raft, ResourceLocation texture) {
        super(context);
        this.texture = texture;
        var root = context.bakeLayer(ModelLayers.createChestBoatModelName(type));
        this.model = raft ? new RaftModel(root) : new BoatModel(root);
        this.shadowRadius = 0.8F;
    }

    @Override
    public void render(Boat boat, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        float hurt = boat.getHurtTime() - partialTick;
        float damage = Math.max(boat.getDamage() - partialTick, 0.0F);
        if (hurt > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * damage / 10.0F * boat.getHurtDir()));
        }
        float bubble = boat.getBubbleAngle(partialTick);
        if (!Mth.equal(bubble, 0.0F)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(bubble * ((float) Math.PI / 180.0F), 1.0F, 0.0F, 1.0F));
        }
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        model.setupAnim(boat, partialTick, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer consumer = buffers.getBuffer(model.renderType(texture));
        model.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        if (!boat.isUnderWater() && model instanceof WaterPatchModel waterPatch) {
            waterPatch.waterPatch().render(poseStack, buffers.getBuffer(RenderType.waterMask()),
                    light, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
        super.render(boat, yaw, partialTick, poseStack, buffers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(Boat boat) {
        return texture;
    }
}
