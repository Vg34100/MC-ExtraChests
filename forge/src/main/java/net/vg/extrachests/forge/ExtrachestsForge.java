package net.vg.extrachests.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vg.extrachests.Extrachests;

@Mod(Extrachests.MOD_ID)
public final class ExtrachestsForge {
    public ExtrachestsForge() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Extrachests.MOD_ID, bus);
        Extrachests.init();
        bus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Extrachests::postInit);
    }
}
