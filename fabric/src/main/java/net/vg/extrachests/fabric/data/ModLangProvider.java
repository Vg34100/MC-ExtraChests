package net.vg.extrachests.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.util.WoodType;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ModLangProvider extends FabricLanguageProvider {
    protected ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        String mod = Extrachests.MOD_ID;

        // Generate translations for all wood types
        for (WoodType woodType : WoodType.values()) {
            String displayName = woodType.getDisplayName() + " Chest";
            String trappedDisplayName = woodType.getDisplayName() + " Trapped Chest";
            
            // Generate for all wood types except OAK (vanilla handles that)
            if (woodType != WoodType.OAK) {
                // Block translations
                translationBuilder.add(woodType.getBlockTranslationKey(false), displayName);
                translationBuilder.add(woodType.getBlockTranslationKey(true), trappedDisplayName);
                
                // Item translations (same display name as blocks)
                translationBuilder.add(woodType.getItemTranslationKey(false), displayName);
                translationBuilder.add(woodType.getItemTranslationKey(true), trappedDisplayName);
                
                // Container translations (for GUI titles)
                translationBuilder.add(woodType.getTranslationKey(false, false), displayName);
                translationBuilder.add(woodType.getTranslationKey(true, false), displayName);
                
                // Double container translations (for large chests)
                translationBuilder.add(woodType.getTranslationKey(false, true), "Large " + displayName);
                translationBuilder.add(woodType.getTranslationKey(true, true), "Large " + displayName);
            }
        }

        // Chest boats
        // helper names
        Function<WoodType, String> boatLabel = w ->
                (w == WoodType.BAMBOO)
                        ? "Bamboo Raft"
                        : w.getDisplayName() + " Boat";
        Function<WoodType, String> chestLabel = w ->
                w.getDisplayName() + " Chest";

        // chest boats / rafts (from the bulk registry)
        ModChestBoatRegistries.items().forEach((combo, itemSup) -> {
            String path = combo.idPath();                // e.g. "spruce_bamboo_chest_boat" or "bamboo_raft_warped_chest"
            String name = boatLabel.apply(combo.boatWood) + " with " + chestLabel.apply(combo.chestWood);

            // item name
            translationBuilder.add("item." + mod + "." + path, name);
            // entity type name (for F3 + B / debug lists / future use)
            translationBuilder.add("entity." + mod + "." + path, name);
        });

    }
}