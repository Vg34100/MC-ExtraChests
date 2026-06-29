package net.vg.justvariants_campfires.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.vg.justvariants_campfires.registry.ObjectRegistry;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add all chest blocks to the axe mineable tag
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
            .add(ObjectRegistry.SPRUCE_CHEST.get())
            .add(ObjectRegistry.SPRUCE_TRAPPED_CHEST.get())
            .add(ObjectRegistry.BIRCH_CHEST.get())
            .add(ObjectRegistry.BIRCH_TRAPPED_CHEST.get())
            .add(ObjectRegistry.JUNGLE_CHEST.get())
            .add(ObjectRegistry.JUNGLE_TRAPPED_CHEST.get())
            .add(ObjectRegistry.ACACIA_CHEST.get())
            .add(ObjectRegistry.ACACIA_TRAPPED_CHEST.get())
            .add(ObjectRegistry.DARK_OAK_CHEST.get())
            .add(ObjectRegistry.DARK_OAK_TRAPPED_CHEST.get())
            .add(ObjectRegistry.MANGROVE_CHEST.get())
            .add(ObjectRegistry.MANGROVE_TRAPPED_CHEST.get())
            .add(ObjectRegistry.CHERRY_CHEST.get())
            .add(ObjectRegistry.CHERRY_TRAPPED_CHEST.get())
            .add(ObjectRegistry.PALE_OAK_CHEST.get())
            .add(ObjectRegistry.PALE_OAK_TRAPPED_CHEST.get())
            .add(ObjectRegistry.BAMBOO_CHEST.get())
            .add(ObjectRegistry.BAMBOO_TRAPPED_CHEST.get())
            .add(ObjectRegistry.CRIMSON_CHEST.get())
            .add(ObjectRegistry.CRIMSON_TRAPPED_CHEST.get())
            .add(ObjectRegistry.WARPED_CHEST.get())
            .add(ObjectRegistry.WARPED_TRAPPED_CHEST.get());
    }
}