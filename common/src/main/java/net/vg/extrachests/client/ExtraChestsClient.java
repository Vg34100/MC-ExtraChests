package net.vg.extrachests.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.vg.extrachests.client.renderer.ModChestBoatRenderer;
import net.vg.extrachests.client.renderer.ModChestRenderer;
import net.vg.extrachests.client.renderer.ModTrappedChestRenderer;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.registry.ModChestRegistries;
import net.vg.extrachests.util.Identifier;
import net.vg.extrachests.util.WoodType;

import java.util.Map;
import java.util.function.Supplier;

public class ExtraChestsClient {
    
    public static void init() {
        registerChestBoatEntityRenderers();
        registerBlockEntityRenderers();
    }

    public static void registerBlockEntityRenderers() {
        ModChestRegistries.chestTypes().forEach((wood, typeSup) ->
                BlockEntityRendererRegistry.register(typeSup.get(), ctx -> new ModChestRenderer(ctx, false))
        );
        ModChestRegistries.trappedTypes().forEach((wood, typeSup) ->
                BlockEntityRendererRegistry.register(typeSup.get(), ModTrappedChestRenderer::new)
        );
    }

    public static final Map<WoodType, ModelLayerLocation> HULL_LAYER = Map.of(
            WoodType.OAK,      ModelLayers.OAK_CHEST_BOAT,
            WoodType.BIRCH,    ModelLayers.BIRCH_CHEST_BOAT,
            WoodType.SPRUCE,   ModelLayers.SPRUCE_CHEST_BOAT,
            WoodType.JUNGLE,   ModelLayers.JUNGLE_CHEST_BOAT,
            WoodType.ACACIA,   ModelLayers.ACACIA_CHEST_BOAT,
            WoodType.DARK_OAK, ModelLayers.DARK_OAK_CHEST_BOAT,
            WoodType.MANGROVE, ModelLayers.MANGROVE_CHEST_BOAT,
            WoodType.CHERRY,   ModelLayers.CHERRY_CHEST_BOAT,
            WoodType.PALE_OAK, ModelLayers.PALE_OAK_CHEST_BOAT,
            WoodType.BAMBOO,   ModelLayers.BAMBOO_CHEST_RAFT // raft
    );

    public static void registerChestBoatEntityRenderers() {
        var water = ModelLayers.BOAT_WATER_PATCH;

        // every combo your common registry created
        ModChestBoatRegistries.entities().forEach((combo, typeSup) -> {
            ModelLayerLocation hull = HULL_LAYER.get(combo.boatWood);
            ResourceLocation texture = Identifier.of(combo.texturePath());
            registerBoatRenderer(typeSup, hull, water, texture);
        });
    }

    /** small generic helper so generics line up without casts */
    private static <T extends AbstractBoat> void registerBoatRenderer(
            Supplier<? extends EntityType<? extends T>> type,
            ModelLayerLocation hull,
            ModelLayerLocation water,
            ResourceLocation texture
    ) {
        EntityRendererRegistry.register(type, ctx ->
                new ModChestBoatRenderer<>(ctx, hull, water, texture)
        );
    }

}