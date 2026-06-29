package net.vg.extrachests.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.vg.extrachests.Extrachests;
import net.neoforged.fml.common.Mod;

@Mod(Extrachests.MOD_ID)
public final class ExtrachestsNeoForge {
    public ExtrachestsNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // Run our common setup.
        Extrachests.init();

        // Register for the common setup event
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Fix the valid blocks and register creative tab items after everything is registered
        event.enqueueWork(() -> {
            Extrachests.postInit();
        });
    }
}
