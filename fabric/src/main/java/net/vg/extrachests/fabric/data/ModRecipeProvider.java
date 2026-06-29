package net.vg.justvariants_campfires.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.vg.justvariants_campfires.util.WoodType;
import net.vg.justvariants_campfires.registry.ObjectRegistry;
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
            }
            
            private void generateChestRecipes() {
                // Generate recipes for all implemented wood types
                for (WoodType woodType : WoodType.values()) {
                    if (woodType != WoodType.OAK) {
                        Item planks = getPlanksForWoodType(woodType);

                        // Regular chest recipe
                        if (woodType == WoodType.SPRUCE) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.SPRUCE_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            // Trapped chest recipe (chest + tripwire hook)
                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.SPRUCE_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.SPRUCE_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.SPRUCE_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.BIRCH) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.BIRCH_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            // Trapped chest recipe (chest + tripwire hook)
                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.BIRCH_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.BIRCH_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.BIRCH_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.JUNGLE) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.JUNGLE_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.JUNGLE_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.JUNGLE_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.JUNGLE_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.ACACIA) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.ACACIA_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.ACACIA_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.ACACIA_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.ACACIA_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.DARK_OAK) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.DARK_OAK_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.DARK_OAK_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.DARK_OAK_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.DARK_OAK_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.MANGROVE) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.MANGROVE_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.MANGROVE_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.MANGROVE_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.MANGROVE_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.CHERRY) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.CHERRY_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.CHERRY_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.CHERRY_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.CHERRY_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.PALE_OAK) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.PALE_OAK_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.PALE_OAK_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.PALE_OAK_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.PALE_OAK_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.BAMBOO) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.BAMBOO_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.BAMBOO_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.BAMBOO_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.BAMBOO_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.CRIMSON) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.CRIMSON_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.CRIMSON_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.CRIMSON_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.CRIMSON_CHEST.get()))
                                .save(output);
                        } else if (woodType == WoodType.WARPED) {
                            shaped(RecipeCategory.DECORATIONS, ObjectRegistry.WARPED_CHEST.get())
                                .pattern("###")
                                .pattern("# #")
                                .pattern("###")
                                .define('#', planks)
                                .group("chest")
                                .unlockedBy("has_planks", has(planks))
                                .save(output);

                            shapeless(RecipeCategory.REDSTONE, ObjectRegistry.WARPED_TRAPPED_CHEST.get())
                                .requires(ObjectRegistry.WARPED_CHEST.get())
                                .requires(Items.TRIPWIRE_HOOK)
                                .group("chest")
                                .unlockedBy("has_chest", has(ObjectRegistry.WARPED_CHEST.get()))
                                .save(output);
                        }
                    }
                }
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

//    OLD METHOD
//    @Override
//    public void buildRecipes(RecipeOutput exporter) {
        //    MANUAL EXAMPLE:
        //    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PebbleShot.THROWING_ROCK.get(), 4)
        //            .pattern("SS")
        //            .pattern("SS")
        //            .define('S', Items.COBBLESTONE)
        //            .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
        //            .save(exporter);
        //
        //    ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ObjectRegistry.GLAZED_CARROT.get())
        //            .requires(Items.CARROT)
        //            .requires(Items.HONEYCOMB)
        //            .unlockedBy("has_carrot", has(Items.CARROT))
        //            .save(exporter);
        // Block of Carrots (3x3 crafting)
//        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ObjectRegistry.BLOCK_OF_CARROTS.get())
//                .pattern("CCC")
//                .pattern("CCC")
//                .pattern("CCC")
//                .define('C', Items.CARROT)
//                .unlockedBy("has_carrot", has(Items.CARROT))
//                .save(exporter);
//
//        // Carrot Cake
//        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ObjectRegistry.CARROT_CAKE.get())
//                .pattern("MCM")
//                .pattern("SES")
//                .pattern("WWW")
//                .define('M', Items.MILK_BUCKET)
//                .define('C', Items.CARROT)
//                .define('S', Items.SUGAR)
//                .define('E', Items.EGG)
//                .define('W', Items.WHEAT)
//                .unlockedBy("has_carrot", has(Items.CARROT))
//                .save(exporter);
//
//
//        for (Util.ModItem modItem : ObjectRegistry.MOD_ITEMS) {
//            RecipeSystem.RecipeData recipe = modItem.recipe();
//
//            if (recipe.type() == RecipeSystem.RecipeType.SHAPELESS) {
//                ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(
//                        recipe.category(),
//                        modItem.item().get(),
//                        recipe.outputCount()
//                );
//
//                for (Item ingredient : recipe.ingredients()) {
//                    builder.requires(ingredient);
//                }
//
//                builder.unlockedBy("has_" + getItemName(recipe.ingredients()[0]),
//                                has(recipe.ingredients()[0]))
//                        .save(exporter);
//            }
//            else if (recipe.type() == RecipeSystem.RecipeType.SHAPED) {
//                ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(
//                        recipe.category(),
//                        modItem.item().get(),
//                        recipe.outputCount()
//                );
//
//                // Add the pattern
//                for (String patternRow : recipe.pattern()) {
//                    builder.pattern(patternRow);
//                }
//
//                // Use the ingredient map directly
//                recipe.ingredientMap().forEach(builder::define);
//
//                builder.unlockedBy(
//                        "has_" + getItemName(recipe.ingredients()[0]),
//                        has(recipe.ingredients()[0])
//                ).save(exporter);
//            }
//        }
//
//    }

    @Override
    public String getName() {
        return "";
    }
}