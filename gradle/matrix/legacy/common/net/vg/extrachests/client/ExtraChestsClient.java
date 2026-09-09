package net.vg.extrachests.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.resources.ResourceLocation;
import net.vg.extrachests.client.renderer.ModChestBoatRenderer;
import net.vg.extrachests.client.renderer.ModChestRenderer;
import net.vg.extrachests.client.renderer.ModTrappedChestRenderer;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.registry.ModChestRegistries;

public final class ExtraChestsClient {
    public static void init() {
        ModChestBoatRegistries.entities().forEach((combo, type) ->
                EntityRendererRegistry.register(type, context -> new ModChestBoatRenderer(context,
                        ModChestBoatRegistries.boatType(combo.boatWood), combo.raft,
                        ResourceLocation.fromNamespaceAndPath("extrachests", combo.texturePath()))));
        registerBlockEntityRenderers();
    }

    public static void registerBlockEntityRenderers() {
        ModChestRegistries.chestTypes().forEach((wood, type) ->
                BlockEntityRendererRegistry.register(type.get(), context -> new ModChestRenderer(context, false)));
        ModChestRegistries.trappedTypes().forEach((wood, type) ->
                BlockEntityRendererRegistry.register(type.get(), ModTrappedChestRenderer::new));
    }

    private ExtraChestsClient() {}
}
