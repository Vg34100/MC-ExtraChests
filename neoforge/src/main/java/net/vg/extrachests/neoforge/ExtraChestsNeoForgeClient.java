package net.vg.extrachests.neoforge;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.client.ExtraChestsClient;
import net.vg.extrachests.client.renderer.ModChestBoatRenderer;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.util.Identifier;

@EventBusSubscriber(modid = Extrachests.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ExtraChestsNeoForgeClient {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // BOATS & RAFTS
        ModChestBoatRegistries.entities().forEach((combo, regSup) -> {
            EntityType<? extends AbstractBoat> type = (EntityType<? extends AbstractBoat>) regSup.get();
            var hull    = ExtraChestsClient.HULL_LAYER.get(combo.boatWood);      // uses ModelLayers.*
            var water   = ModelLayers.BOAT_WATER_PATCH;
            var texture = Identifier.of(combo.texturePath()); // "textures/entity/chest_boat/<boat>_<chest>.png"

            event.registerEntityRenderer(type, ctx -> new ModChestBoatRenderer(ctx, hull, water, texture));
            System.out.println("[JV-NF] bound renderer: " + BuiltInRegistries.ENTITY_TYPE.getKey(type) + " -> " + texture);
        });

        // BLOCK ENTITY renderers are fine to keep via Architectury helper:
        ExtraChestsClient.registerBlockEntityRenderers();
    }
}
