package net.vg.extrachests.fabric.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.util.WoodType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public  class ModModelProvider extends FabricModelProvider {
    private final FabricDataOutput dataOutput;
    
    public ModModelProvider(FabricDataOutput output) {
        super(output);
        this.dataOutput = output;
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        // Generate chest item models for all implemented wood types
        generateChestItemModels();
        
        // Generate blockstate files for all implemented wood types
        generateBlockstateFiles();
        
        // Generate block model files for all implemented wood types
        generateBlockModelFiles();

        // Generate chest boat model files
        generateChestBoatItemModels(gen);

    }
    
    /**
     * Generates chest item models for all implemented wood types
     * Creates the special chest item JSON files with custom textures
     */
    private void generateChestItemModels() {
        try {
            for (WoodType woodType : WoodType.values()) {
                // Generate for all wood types except OAK (vanilla handles that)
                if (woodType != WoodType.OAK) {
                    // Generate regular chest item model
                    generateChestItemModel(woodType, false);
                    
                    // Generate trapped chest item model
                    generateChestItemModel(woodType, true);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate chest item models", e);
        }
    }
    
    private void generateChestItemModel(WoodType woodType, boolean isTrapped) throws IOException {
        // Create the JSON structure
        JsonObject root = new JsonObject();
        JsonObject model = new JsonObject();
        JsonObject chestModel = new JsonObject();

        model.addProperty("type", "minecraft:special");
        model.addProperty("base", "minecraft:item/chest");

        chestModel.addProperty("type", "minecraft:chest");
        String textureName = woodType.getName() + (isTrapped ? "_trapped" : "");
        chestModel.addProperty("texture", Extrachests.MOD_ID + ":" + textureName);

        model.add("model", chestModel);
        root.add("model", model);

        // Build file path to common resources folder (NOT generated folder)
        String fileName = woodType.getName() + (isTrapped ? "_trapped" : "") + "_chest.json";
        Path outputPath = this.dataOutput.getOutputFolder()
                .getParent().getParent().getParent().getParent() // Navigate up from fabric/src/generated/resources
                .resolve("common")
                .resolve("src")
                .resolve("main")
                .resolve("resources")
                .resolve("assets")
                .resolve(Extrachests.MOD_ID)
                .resolve("items")
                .resolve(fileName);

        // Create directories and write file
        Files.createDirectories(outputPath.getParent());

        String jsonString = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(root);
        Files.writeString(outputPath, jsonString);

        System.out.println("Generated chest item model: " + fileName);
    }
    
    /**
     * Generates blockstate files for all implemented wood types
     */
    private void generateBlockstateFiles() {
        try {
            for (WoodType woodType : WoodType.values()) {
                // Generate for all wood types except OAK (vanilla handles that)
                if (woodType != WoodType.OAK) {
                    // Generate regular chest blockstate
                    generateBlockstateFile(woodType, false);
                    
                    // Generate trapped chest blockstate
                    generateBlockstateFile(woodType, true);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate blockstate files", e);
        }
    }
    
    private void generateBlockstateFile(WoodType woodType, boolean isTrapped) throws IOException {
        // Create the JSON structure
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        
        String modelName = woodType.getName() + (isTrapped ? "_trapped" : "") + "_chest";
        String modelPath = Extrachests.MOD_ID + ":block/" + modelName;
        
        // Generate all 24 variants (4 facings × 3 types × 2 waterlogged states)
        String[] facings = {"north", "south", "west", "east"};
        String[] types = {"single", "left", "right"};
        boolean[] waterloggedStates = {false, true};
        int[] rotations = {0, 180, 270, 90}; // Corresponding to north, south, west, east
        
        for (int f = 0; f < facings.length; f++) {
            for (String type : types) {
                for (boolean waterlogged : waterloggedStates) {
                    String variantKey = String.format("facing=%s,type=%s,waterlogged=%s", 
                        facings[f], type, waterlogged);
                    
                    JsonObject variantValue = new JsonObject();
                    variantValue.addProperty("model", modelPath);
                    
                    // Add rotation for non-north facings
                    if (rotations[f] != 0) {
                        variantValue.addProperty("y", rotations[f]);
                    }
                    
                    variants.add(variantKey, variantValue);
                }
            }
        }
        
        root.add("variants", variants);
        
        // Build file path to common resources folder
        String fileName = woodType.getName() + (isTrapped ? "_trapped" : "") + "_chest.json";
        Path outputPath = this.dataOutput.getOutputFolder()
                .getParent().getParent().getParent().getParent() // Navigate up from fabric/src/generated/resources
                .resolve("common")
                .resolve("src")
                .resolve("main")
                .resolve("resources")
                .resolve("assets")
                .resolve(Extrachests.MOD_ID)
                .resolve("blockstates")
                .resolve(fileName);
                
        // Create directories and write file
        Files.createDirectories(outputPath.getParent());
        
        String jsonString = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(root);
        Files.writeString(outputPath, jsonString);
        
        System.out.println("Generated blockstate file: " + fileName);
    }
    
    /**
     * Generates block model files for all implemented wood types
     */
    private void generateBlockModelFiles() {
        try {
            for (WoodType woodType : WoodType.values()) {
                // Generate for all wood types except OAK (vanilla handles that)
                if (woodType != WoodType.OAK) {
                    // Generate regular chest block model
                    generateBlockModelFile(woodType, false);
                    
                    // Generate trapped chest block model
                    generateBlockModelFile(woodType, true);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate block model files", e);
        }
    }
    
    private void generateBlockModelFile(WoodType woodType, boolean isTrapped) throws IOException {
        // Create the simple JSON structure
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/chest");
        
        // Build file path to common resources folder
        String fileName = woodType.getName() + (isTrapped ? "_trapped" : "") + "_chest.json";
        Path outputPath = this.dataOutput.getOutputFolder()
                .getParent().getParent().getParent().getParent() // Navigate up from fabric/src/generated/resources
                .resolve("common")
                .resolve("src")
                .resolve("main")
                .resolve("resources")
                .resolve("assets")
                .resolve(Extrachests.MOD_ID)
                .resolve("models")
                .resolve("block")
                .resolve(fileName);
                
        // Create directories and write file
        Files.createDirectories(outputPath.getParent());
        
        String jsonString = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(root);
        Files.writeString(outputPath, jsonString);
        
        System.out.println("Generated block model file: " + fileName);
    }

    public static void generateChestBoatItemModels(ItemModelGenerators gen) {
        ModChestBoatRegistries.items().forEach((key, itemSup) ->
                gen.generateFlatItem(itemSup.get(), ModelTemplates.FLAT_ITEM)
        );
    }
}

