package net.vg.extrachests.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.vg.extrachests.client.ExtraChestsClient;
import net.vg.extrachests.client.renderer.LegacyChestItemRenderer;
import net.vg.extrachests.registry.ModChestRegistries;

public final class ExtrachestsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExtraChestsClient.init();
        ModChestRegistries.chestBlocks().forEach((wood, block) ->
                BuiltinItemRendererRegistry.INSTANCE.register(block.get().asItem(),
                        (stack, context, poseStack, buffers, light, overlay) ->
                                LegacyChestItemRenderer.render(wood, false, poseStack, buffers, light, overlay)));
        ModChestRegistries.trappedBlocks().forEach((wood, block) ->
                BuiltinItemRendererRegistry.INSTANCE.register(block.get().asItem(),
                        (stack, context, poseStack, buffers, light, overlay) ->
                                LegacyChestItemRenderer.render(wood, true, poseStack, buffers, light, overlay)));
    }
}
