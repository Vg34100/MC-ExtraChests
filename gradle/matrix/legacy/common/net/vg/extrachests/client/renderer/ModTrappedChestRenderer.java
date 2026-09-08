package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.vg.extrachests.blockentity.ModTrappedChestBlockEntity;

public final class ModTrappedChestRenderer implements BlockEntityRenderer<ModTrappedChestBlockEntity> {
    private final ModChestRenderer.Models models;

    public ModTrappedChestRenderer(BlockEntityRendererProvider.Context context) {
        models = new ModChestRenderer.Models(context);
    }

    @Override
    public void render(ModTrappedChestBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int light, int overlay) {
        ModChestRenderer.renderChest(blockEntity, blockEntity.getWoodType(), true,
                partialTick, poseStack, buffers, light, overlay, models);
    }
}
