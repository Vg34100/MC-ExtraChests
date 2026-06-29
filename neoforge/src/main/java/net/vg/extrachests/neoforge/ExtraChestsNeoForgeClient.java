package net.vg.extrachests.neoforge;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.vg.extrachests.client.ExtraChestsClient;
import net.vg.extrachests.client.renderer.ModChestBoatRenderer;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.util.Identifier;

public class ExtraChestsNeoForgeClient {

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModChestBoatRegistries.entities().forEach((combo, regSup) -> {
            EntityType<? extends AbstractBoat> type = (EntityType<? extends AbstractBoat>) regSup.get();
            var hull    = ExtraChestsClient.HULL_LAYER.get(combo.boatWood);
            var water   = ModelLayers.BOAT_WATER_PATCH;
            var texture = Identifier.of(combo.texturePath());

            event.registerEntityRenderer(type, ctx -> new ModChestBoatRenderer(ctx, hull, water, texture));
        });

        ExtraChestsClient.registerBlockEntityRenderers();
    }
}
