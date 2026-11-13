package net.vg.extrachests.forge;

import java.util.function.Consumer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.vg.extrachests.util.WoodType;

/** Forge 1.20.1 supplies custom item renderers through Item.initializeClient. */
public final class ForgeChestBlockItem extends BlockItem {
    private final WoodType wood;
    private final boolean trapped;

    public ForgeChestBlockItem(Block block, Properties properties, WoodType wood, boolean trapped) {
        super(block, properties);
        this.wood = wood;
        this.trapped = trapped;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer renderer;

            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new ChestItemRenderer(wood, trapped);
                return renderer;
            }
        });
    }

    private static final class ChestItemRenderer extends net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer {
        private final WoodType wood;
        private final boolean trapped;

        ChestItemRenderer(WoodType wood, boolean trapped) {
            super(net.minecraft.client.Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                    net.minecraft.client.Minecraft.getInstance().getEntityModels());
            this.wood = wood;
            this.trapped = trapped;
        }

        @Override
        public void renderByItem(net.minecraft.world.item.ItemStack stack, net.minecraft.world.item.ItemDisplayContext context,
                                 com.mojang.blaze3d.vertex.PoseStack poses, net.minecraft.client.renderer.MultiBufferSource buffers,
                                 int light, int overlay) {
            net.vg.extrachests.client.renderer.LegacyChestItemRenderer.render(wood, trapped, poses, buffers, light, overlay);
        }
    }
}
