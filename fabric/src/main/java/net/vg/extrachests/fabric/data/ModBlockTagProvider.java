package net.vg.extrachests.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.vg.extrachests.registry.ModChestRegistries;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider<Block> {

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var axe = builder(BlockTags.MINEABLE_WITH_AXE);
        ModChestRegistries.chestBlocks().values().forEach(sup ->
                axe.add(BuiltInRegistries.BLOCK.getResourceKey(sup.get()).orElseThrow()));
        ModChestRegistries.trappedBlocks().values().forEach(sup ->
                axe.add(BuiltInRegistries.BLOCK.getResourceKey(sup.get()).orElseThrow()));
    }
}
