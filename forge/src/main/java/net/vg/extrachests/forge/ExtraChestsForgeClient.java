package net.vg.extrachests.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.client.ExtraChestsClient;
import net.vg.extrachests.client.renderer.ModChestBoatRenderer;
import net.vg.extrachests.registry.ModChestBoatRegistries;

@Mod.EventBusSubscriber(modid = Extrachests.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ExtraChestsForgeClient {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModChestBoatRegistries.entities().forEach((combo, type) ->
                event.registerEntityRenderer(type.get(), context -> new ModChestBoatRenderer(context,
                        ModChestBoatRegistries.boatType(combo.boatWood), combo.raft,
                        new ResourceLocation(Extrachests.MOD_ID, combo.texturePath()))));
        ExtraChestsClient.registerBlockEntityRenderers();
    }
}
