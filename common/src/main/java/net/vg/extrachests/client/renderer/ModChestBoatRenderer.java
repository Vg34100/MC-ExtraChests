package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.AbstractBoat;

public class ModChestBoatRenderer<T extends AbstractBoat> extends AbstractBoatRenderer{
    private final Model waterPatchModel;
    private final EntityModel<BoatRenderState> model;
    private final ResourceLocation texture; // your custom texture

    public ModChestBoatRenderer(EntityRendererProvider.Context ctx,
                                ModelLayerLocation hullLayer,
                                ModelLayerLocation waterPatchLayer,
                                ResourceLocation texture) {
        super(ctx);
        this.model = new BoatModel(ctx.bakeLayer(hullLayer));
        this.waterPatchModel = new Model.Simple(
                ctx.bakeLayer(waterPatchLayer),
                rl -> RenderType.waterMask()
        );
        this.texture = texture;
    }

    @Override
    protected EntityModel<BoatRenderState> model() {
        return this.model;
    }

    @Override
    protected RenderType renderType() {
        return this.model.renderType(this.texture);
    }

    @Override
    protected void renderTypeAdditions(BoatRenderState state, PoseStack pose, MultiBufferSource buffers, int light) {
        if (!state.isUnderWater) {
            this.waterPatchModel.renderToBuffer(
                    pose, buffers.getBuffer(this.waterPatchModel.renderType(this.texture)), light, OverlayTexture.NO_OVERLAY
            );
        }
    }
}
