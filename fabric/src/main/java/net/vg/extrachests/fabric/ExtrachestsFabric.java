package net.vg.extrachests.fabric;

import net.vg.extrachests.Extrachests;
import net.fabricmc.api.ModInitializer;

public final class ExtrachestsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Extrachests.init();
        // On Fabric, we can call postInit immediately after since registries are available
        Extrachests.postInit();
    }
}
