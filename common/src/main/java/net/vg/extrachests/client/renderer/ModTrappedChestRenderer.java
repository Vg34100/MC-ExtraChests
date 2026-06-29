package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import net.vg.extrachests.blockentity.ModTrappedChestBlockEntity;

public class ModTrappedChestRenderer implements BlockEntityRenderer<ModTrappedChestBlockEntity, ModChestRenderer.State> {

    private final ModChestRenderer inner;

    public ModTrappedChestRenderer(BlockEntityRendererProvider.Context ctx) {
        this.inner = new ModChestRenderer(ctx, true);
    }

    @Override
    public ModChestRenderer.State createRenderState() {
        return new ModChestRenderer.State();
    }

    @Override
    public void extractRenderState(ModTrappedChestBlockEntity be, ModChestRenderer.State state,
                                   float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay overlay) {
        ModChestRenderer.extractChestState(be, be.getWoodType(), true, state, partialTick, overlay);
    }

    @Override
    public void submit(ModChestRenderer.State state, PoseStack poseStack,
                       SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        inner.submit(state, poseStack, nodeCollector, cameraRenderState);
    }
}
