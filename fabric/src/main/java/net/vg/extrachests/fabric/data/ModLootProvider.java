package net.vg.justvariants_campfires.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLootProvider extends FabricBlockLootTableProvider {
    protected ModLootProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Generate loot tables for all chest blocks
        // Chests drop themselves when broken
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.SPRUCE_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.SPRUCE_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.BIRCH_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.BIRCH_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.JUNGLE_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.JUNGLE_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.ACACIA_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.ACACIA_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.DARK_OAK_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.DARK_OAK_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.MANGROVE_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.MANGROVE_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.CHERRY_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.CHERRY_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.PALE_OAK_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.PALE_OAK_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.BAMBOO_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.BAMBOO_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.CRIMSON_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.CRIMSON_TRAPPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.WARPED_CHEST.get());
        this.dropSelf(net.vg.justvariants_campfires.registry.ObjectRegistry.WARPED_TRAPPED_CHEST.get());
    }
}
