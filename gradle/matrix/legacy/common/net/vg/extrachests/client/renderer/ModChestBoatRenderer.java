package net.vg.extrachests.client.renderer;

import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public final class ModChestBoatRenderer extends BoatRenderer {
    private final ResourceLocation texture;

    public ModChestBoatRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
        super(context, true);
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(Boat boat) {
        return texture;
    }
}
