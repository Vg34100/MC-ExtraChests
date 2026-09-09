package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.util.WoodType;

public final class LegacyChestItemRenderer {
    private static ModelPart root;

    public static void render(WoodType wood, boolean trapped, PoseStack poseStack,
                              MultiBufferSource buffers, int light, int overlay) {
        if (root == null) root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.CHEST);
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                Extrachests.MOD_ID, "textures/entity/chest/" + wood.getTextureLocation(trapped, null) + ".png");
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityCutout(texture));

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        root.getChild("bottom").render(poseStack, consumer, light, overlay);
        root.getChild("lid").render(poseStack, consumer, light, overlay);
        root.getChild("lock").render(poseStack, consumer, light, overlay);
        poseStack.popPose();
    }

    private LegacyChestItemRenderer() {}
}
