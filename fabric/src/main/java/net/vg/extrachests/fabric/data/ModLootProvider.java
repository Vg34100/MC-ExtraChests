package net.vg.extrachests.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.vg.extrachests.registry.ModChestRegistries;

import java.util.concurrent.CompletableFuture;

public class ModLootProvider extends FabricBlockLootTableProvider {
    protected ModLootProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Generate loot tables for all chest blocks
        // Chests drop themselves when broken
        ModChestRegistries.chestBlocks().forEach((wood, block) -> {
            this.dropSelf(block.get());
        });
        ModChestRegistries.trappedBlocks().forEach((wood, block) -> {
            this.dropSelf(block.get());
        });
    }
}
