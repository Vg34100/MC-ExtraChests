package net.vg.extrachests;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.registry.ModChestRegistries;

public final class Extrachests {
    public static final String MOD_ID = "extrachests";

    public static void init() {
        // Write common init code here.
        ModChestRegistries.registerAll();
        ModChestBoatRegistries.registerAll();

    }

    public static void postInit() {
        // Register creative tab items after everything is initialized
        registerCreativeTabItems();
    }

    private static void registerCreativeTabItems() {
        // Add regular chests to the Functional Blocks creative tab
        CreativeTabRegistry.append(
                CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModChestRegistries.chestBlocks().values().stream()
                        .map(RegistrySupplier::get)
                        .toArray(ItemLike[]::new)
        );
        CreativeTabRegistry.append(
                CreativeModeTabs.REDSTONE_BLOCKS,
                ModChestRegistries.trappedBlocks().values().stream()
                        .map(RegistrySupplier::get)
                        .toArray(ItemLike[]::new)
        );

        // Add boat chests
        CreativeTabRegistry.append(
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModChestBoatRegistries.items()
                        .values()
                        .stream()
                        .map(RegistrySupplier::get)
                        .toArray(ItemLike[]::new)
        );
    }
}
