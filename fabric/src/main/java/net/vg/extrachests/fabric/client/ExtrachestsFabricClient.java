package net.vg.extrachests.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.vg.extrachests.client.ExtraChestsClient;

public final class ExtrachestsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExtraChestsClient.init();
    }
}
