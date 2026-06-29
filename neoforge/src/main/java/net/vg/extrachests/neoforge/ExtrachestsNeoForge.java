package net.vg.extrachests.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.vg.extrachests.Extrachests;

@Mod(Extrachests.MOD_ID)
public final class ExtrachestsNeoForge {
    public ExtrachestsNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Extrachests.init();
        modEventBus.addListener(this::commonSetup);
        if (FMLEnvironment.getDist().isClient()) {
            ExtraChestsNeoForgeClient client = new ExtraChestsNeoForgeClient();
            modEventBus.addListener(client::registerEntityRenderers);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Extrachests::postInit);
    }
}
