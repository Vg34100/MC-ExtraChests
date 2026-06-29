package net.vg.extrachests.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.blockentity.ModChestBlockEntity;
import net.vg.extrachests.util.WoodType;

public class ModChestRenderer implements BlockEntityRenderer<ModChestBlockEntity, ModChestRenderer.State> {

    public static class State extends BlockEntityRenderState {
        public WoodType woodType;
        public ChestType chestType = ChestType.SINGLE;
        public Direction facing = Direction.SOUTH;
        public float open = 0.0f;
        public boolean trapped = false;
    }

    protected final SpriteGetter sprites;
    protected final ChestModel singleModel;
    protected final ChestModel doubleLeftModel;
    protected final ChestModel doubleRightModel;
    private final boolean isTrapped;

    public ModChestRenderer(BlockEntityRendererProvider.Context ctx, boolean isTrapped) {
        this.sprites = ctx.sprites();
        this.isTrapped = isTrapped;
        this.singleModel = new ChestModel(ctx.bakeLayer(ModelLayers.CHEST));
        this.doubleLeftModel = new ChestModel(ctx.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        this.doubleRightModel = new ChestModel(ctx.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ModChestBlockEntity be, State state, float partialTick,
                                   Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay overlay) {
        extractChestState(be, be.getWoodType(), isTrapped, state, partialTick, overlay);
    }

    /** Shared extraction logic usable by subclasses for different BE types. */
    protected static void extractChestState(ChestBlockEntity be, WoodType woodType,
                                            boolean trapped, State state,
                                            float partialTick,
                                            ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderState.extractBase(be, state, overlay);
        state.woodType = woodType;
        state.trapped = trapped;

        Level level = be.getLevel();
        boolean hasLevel = level != null;
        BlockState blockState = hasLevel ? be.getBlockState()
                : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);

        state.chestType = blockState.hasProperty(ChestBlock.TYPE)
                ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        state.facing = blockState.getValue(ChestBlock.FACING);

        DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combineResult;
        if (hasLevel && blockState.getBlock() instanceof AbstractChestBlock<?> abstractChest) {
            combineResult = abstractChest.combine(blockState, level, be.getBlockPos(), true);
        } else {
            combineResult = DoubleBlockCombiner.Combiner::acceptNone;
        }

        state.open = ((Float2FloatFunction) combineResult.apply(
                ChestBlock.opennessCombiner((LidBlockEntity) be))).get(partialTick);

        if (state.chestType != ChestType.SINGLE) {
            state.lightCoords = ((Int2IntFunction) combineResult.apply(new BrightnessCombiner<>()))
                    .applyAsInt(state.lightCoords);
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector nodeCollector,
                       CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));

        float openAmount = 1.0f - state.open;
        openAmount = 1.0f - openAmount * openAmount * openAmount;

        ChestModel model = switch (state.chestType) {
            case LEFT -> doubleLeftModel;
            case RIGHT -> doubleRightModel;
            default -> singleModel;
        };

        String textureName = state.woodType.getTextureLocation(state.trapped, switch (state.chestType) {
            case LEFT -> "left";
            case RIGHT -> "right";
            default -> null;
        });
        SpriteId spriteId = new SpriteId(
                Sheets.CHEST_SHEET,
                Identifier.fromNamespaceAndPath(Extrachests.MOD_ID, "entity/chest/" + textureName)
        );

        nodeCollector.submitModel(model, openAmount, poseStack,
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1,
                spriteId, sprites, 0, state.breakProgress);

        poseStack.popPose();
    }
}
