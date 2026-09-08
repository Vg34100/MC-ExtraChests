package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.blockentity.ModChestBlockEntity;
import net.vg.extrachests.util.WoodType;

public class ModChestRenderer implements BlockEntityRenderer<ModChestBlockEntity> {
    private final Models models;
    private final boolean trapped;

    public ModChestRenderer(BlockEntityRendererProvider.Context context, boolean trapped) {
        models = new Models(context);
        this.trapped = trapped;
    }

    @Override
    public void render(ModChestBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int light, int overlay) {
        renderChest(blockEntity, blockEntity.getWoodType(), trapped, partialTick, poseStack, buffers, light, overlay, models);
    }

    static void renderChest(ChestBlockEntity blockEntity, WoodType wood, boolean trapped, float partialTick,
                            PoseStack poseStack, MultiBufferSource buffers, int light, int overlay, Models models) {
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.hasProperty(ChestBlock.FACING) ? state.getValue(ChestBlock.FACING) : Direction.SOUTH;
        ChestType type = state.hasProperty(ChestBlock.TYPE) ? state.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        ModelParts parts = switch (type) {
            case LEFT -> models.left;
            case RIGHT -> models.right;
            default -> models.single;
        };
        String side = type == ChestType.LEFT ? "left" : type == ChestType.RIGHT ? "right" : null;
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                Extrachests.MOD_ID, "entity/chest/" + wood.getTextureLocation(trapped, side));
        VertexConsumer consumer = new Material(Sheets.CHEST_SHEET, texture).buffer(buffers, RenderType::entityCutout);

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        float open = blockEntity.getOpenNess(partialTick);
        open = 1.0F - open;
        open = 1.0F - open * open * open;
        parts.lid.xRot = -(open * ((float) Math.PI / 2.0F));
        parts.lock.xRot = parts.lid.xRot;
        parts.bottom.render(poseStack, consumer, light, overlay);
        parts.lid.render(poseStack, consumer, light, overlay);
        parts.lock.render(poseStack, consumer, light, overlay);
        poseStack.popPose();
    }

    static final class Models {
        final ModelParts single;
        final ModelParts left;
        final ModelParts right;

        Models(BlockEntityRendererProvider.Context context) {
            single = new ModelParts(context.bakeLayer(ModelLayers.CHEST));
            left = new ModelParts(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
            right = new ModelParts(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
        }
    }

    static final class ModelParts {
        final ModelPart lid;
        final ModelPart bottom;
        final ModelPart lock;

        ModelParts(ModelPart root) {
            lid = root.getChild("lid");
            bottom = root.getChild("bottom");
            lock = root.getChild("lock");
        }
    }
}
