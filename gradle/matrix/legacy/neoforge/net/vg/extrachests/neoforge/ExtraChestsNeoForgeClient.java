package net.vg.extrachests.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.vg.extrachests.client.ExtraChestsClient;
import net.vg.extrachests.client.renderer.ModChestBoatRenderer;
import net.vg.extrachests.client.renderer.LegacyChestItemRenderer;
import net.vg.extrachests.registry.ModChestBoatRegistries;
import net.vg.extrachests.registry.ModChestRegistries;
import net.vg.extrachests.util.WoodType;

public final class ExtraChestsNeoForgeClient {
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModChestBoatRegistries.entities().forEach((combo, type) ->
                event.registerEntityRenderer(type.get(), context -> new ModChestBoatRenderer(context,
                        ResourceLocation.fromNamespaceAndPath("extrachests", combo.texturePath()))));
        ExtraChestsClient.registerBlockEntityRenderers();
    }

    public void registerClientExtensions(RegisterClientExtensionsEvent event) {
        ModChestRegistries.chestBlocks().forEach((wood, block) ->
                event.registerItem(extension(wood, false), block.get().asItem()));
        ModChestRegistries.trappedBlocks().forEach((wood, block) ->
                event.registerItem(extension(wood, true), block.get().asItem()));
    }

    private static IClientItemExtensions extension(WoodType wood, boolean trapped) {
        BlockEntityWithoutLevelRenderer renderer = new BlockEntityWithoutLevelRenderer(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) {
            @Override
            public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
                                     MultiBufferSource buffers, int light, int overlay) {
                LegacyChestItemRenderer.render(wood, trapped, poseStack, buffers, light, overlay);
            }
        };
        return new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        };
    }
}
