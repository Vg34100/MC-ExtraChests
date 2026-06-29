package net.vg.justvariants_campfires.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.vg.justvariants_campfires.util.WoodType;
import net.vg.justvariants_campfires.registry.ObjectRegistry;

import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {
    protected ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
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
    }
}