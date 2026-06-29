package net.vg.extrachests.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.registry.ModChestRegistries;
import net.vg.extrachests.util.WoodType;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            @Override
            public void buildRecipes() {
                generateChestRecipes();
                generateChestBoatRecipes();
            }
            
            private void generateChestRecipes() {
                // Generate recipes for all implemented wood types
                ModChestRegistries.chestBlocks().forEach((wood, block) -> {
                    shaped(RecipeCategory.DECORATIONS, block.get())
                            .pattern("###")
                            .pattern("# #")
                            .pattern("###")
                            .define('#', getPlanksForWoodType(wood))
                            .group("chest")
                            .unlockedBy("has_planks", has(getPlanksForWoodType(wood)))
                            .save(output);
                });
                ModChestRegistries.trappedBlocks().forEach((wood, block) -> {
                    // Trapped chest recipe (chest + tripwire hook)
                    shapeless(RecipeCategory.REDSTONE, block.get())
                            .requires(block.get())
                            .requires(Items.TRIPWIRE_HOOK)
                            .group("chest")
                            .unlockedBy("has_chest", has(block.get()))
                            .save(output);
                });
            }

            public void generateChestBoatRecipes() {
                ModChestBoatRegistries.items().forEach((key, itemSup) -> {
                    var boatItem = ModChestBoatRegistries.vanillaBoatItem(key.boatWood);

                    // look up the chest block *safely*
                    var chestSup = ModChestRegistries.chestBlocks().get(key.chestWood);
                    if (chestSup == null) return; // skip combos without a chest block registered

                    var chestItem = chestSup.get().asItem();

                    shapeless(RecipeCategory.TRANSPORTATION, itemSup.get())
                            .requires(boatItem)
                            .requires(chestItem)
                            .group("chest_boat")
                            .unlockedBy("has_boat", has(boatItem))
                            .save(output);
                });
            }

            private Item getPlanksForWoodType(WoodType woodType) {
                return switch (woodType) {
                    case OAK -> Items.OAK_PLANKS;
                    case BIRCH -> Items.BIRCH_PLANKS;
                    case SPRUCE -> Items.SPRUCE_PLANKS;
                    case JUNGLE -> Items.JUNGLE_PLANKS;
                    case ACACIA -> Items.ACACIA_PLANKS;
                    case DARK_OAK -> Items.DARK_OAK_PLANKS;
                    case MANGROVE -> Items.MANGROVE_PLANKS;
                    case CHERRY -> Items.CHERRY_PLANKS;
                    case PALE_OAK -> Items.PALE_OAK_PLANKS;
                    case BAMBOO -> Items.BAMBOO_PLANKS;
                    case CRIMSON -> Items.CRIMSON_PLANKS;
                    case WARPED -> Items.WARPED_PLANKS;
                };
            }
        };
    }

    @Override
    public String getName() {
        return "";
    }
}