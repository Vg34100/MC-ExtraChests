package net.vg.justvariants_campfires.client;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.vg.justvariants_campfires.Justvariants_campfires;
import net.vg.justvariants_campfires.client.renderer.ModChestRenderer;
import net.vg.justvariants_campfires.client.renderer.ModTrappedChestRenderer;
import net.vg.justvariants_campfires.registry.ObjectRegistry;

public class Justvariants_campfiresClient {
    
    public static final Material SPRUCE_CHEST_MATERIAL = new Material(
        Sheets.CHEST_SHEET, 
        ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, "entity/chest/spruce")
    );
    
    public static final Material SPRUCE_CHEST_LEFT_MATERIAL = new Material(
        Sheets.CHEST_SHEET, 
        ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, "entity/chest/spruce_left")
    );
    
    public static final Material SPRUCE_CHEST_RIGHT_MATERIAL = new Material(
        Sheets.CHEST_SHEET, 
        ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, "entity/chest/spruce_right")
    );
    
    public static final Material SPRUCE_TRAPPED_CHEST_MATERIAL = new Material(
        Sheets.CHEST_SHEET, 
        ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, "entity/chest/spruce_trapped")
    );
    
    public static final Material SPRUCE_TRAPPED_CHEST_LEFT_MATERIAL = new Material(
        Sheets.CHEST_SHEET, 
        ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, "entity/chest/spruce_trapped_left")
    );
    
    public static final Material SPRUCE_TRAPPED_CHEST_RIGHT_MATERIAL = new Material(
        Sheets.CHEST_SHEET, 
        ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, "entity/chest/spruce_trapped_right")
    );
    
    public static void init() {
        registerBlockEntityRenderers();
    }
    
    private static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(ObjectRegistry.SPRUCE_CHEST_BLOCK_ENTITY.get(), 
            context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.SPRUCE_TRAPPED_CHEST_BLOCK_ENTITY.get(), 
            ModTrappedChestRenderer::new);
            
        BlockEntityRendererRegistry.register(ObjectRegistry.BIRCH_CHEST_BLOCK_ENTITY.get(), 
            context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.BIRCH_TRAPPED_CHEST_BLOCK_ENTITY.get(), 
            ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.JUNGLE_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.JUNGLE_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.ACACIA_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.ACACIA_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.DARK_OAK_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.DARK_OAK_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.MANGROVE_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.MANGROVE_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.CHERRY_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.CHERRY_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.PALE_OAK_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.PALE_OAK_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.BAMBOO_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.BAMBOO_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.CRIMSON_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.CRIMSON_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);

        BlockEntityRendererRegistry.register(ObjectRegistry.WARPED_CHEST_BLOCK_ENTITY.get(),
                context -> new ModChestRenderer(context, false));
        BlockEntityRendererRegistry.register(ObjectRegistry.WARPED_TRAPPED_CHEST_BLOCK_ENTITY.get(),
                ModTrappedChestRenderer::new);
    }
}