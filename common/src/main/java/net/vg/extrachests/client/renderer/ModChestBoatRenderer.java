package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

public class ModChestBoatRenderer extends AbstractBoatRenderer {
    private final Model.Simple waterPatchModel;
    private final EntityModel<BoatRenderState> model;

    public ModChestBoatRenderer(EntityRendererProvider.Context ctx,
                                ModelLayerLocation hullLayer,
                                ModelLayerLocation waterPatchLayer,
                                Identifier texture) {
        super(ctx, texture);
        this.model = new BoatModel(ctx.bakeLayer(hullLayer));
        this.waterPatchModel = new Model.Simple(ctx.bakeLayer(waterPatchLayer), rl -> RenderTypes.waterMask());
    }

    @Override
    protected EntityModel<BoatRenderState> model() {
        return this.model;
    }

    @Override
    protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector nodeCollector, int light) {
        if (!state.isUnderWater) {
            nodeCollector.submitModel(this.waterPatchModel, Unit.INSTANCE, poseStack,
                    this.texture, light, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }
    }
}
