package net.vg.justvariants_campfires.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import net.vg.justvariants_campfires.Justvariants_campfires;
import net.vg.justvariants_campfires.blockentity.ModChestBlockEntity;
import net.vg.justvariants_campfires.util.WoodType;

public class ModChestRenderer extends ChestRenderer<ModChestBlockEntity> {
    
    private final ChestModel singleModel;
    private final ChestModel doubleLeftModel;
    private final ChestModel doubleRightModel;
    private final boolean isTrapped;
    
    public ModChestRenderer(BlockEntityRendererProvider.Context context, boolean isTrapped) {
        super(context);
        this.isTrapped = isTrapped;
        this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.doubleLeftModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        this.doubleRightModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
    }
    
    @Override
    public void render(ModChestBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, Vec3 vec3) {
        Level level = blockEntity.getLevel();
        boolean bl = level != null;
        BlockState blockState = bl ? blockEntity.getBlockState() : (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? (ChestType)blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        Block block = blockState.getBlock();
        if (block instanceof AbstractChestBlock<?> abstractChestBlock) {
            boolean bl2 = chestType != ChestType.SINGLE;
            poseStack.pushPose();
            float g = ((Direction)blockState.getValue(ChestBlock.FACING)).toYRot();
            poseStack.translate(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-g));
            poseStack.translate(-0.5F, -0.5F, -0.5F);
            DoubleBlockCombiner.NeighborCombineResult neighborCombineResult;
            if (bl) {
                neighborCombineResult = abstractChestBlock.combine(blockState, level, blockEntity.getBlockPos(), true);
            } else {
                neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;
            }

            float h = ((Float2FloatFunction)neighborCombineResult.apply(ChestBlock.opennessCombiner((LidBlockEntity)blockEntity))).get(f);
            h = 1.0F - h;
            h = 1.0F - h * h * h;
            int k = ((Int2IntFunction)neighborCombineResult.apply(new BrightnessCombiner())).applyAsInt(i);
            
            Material material = getMaterial(blockEntity.getWoodType(), chestType);
            VertexConsumer vertexConsumer = material.buffer(multiBufferSource, RenderType::entityCutout);
            if (bl2) {
                if (chestType == ChestType.LEFT) {
                    this.renderModel(poseStack, vertexConsumer, this.doubleLeftModel, h, k, j);
                } else {
                    this.renderModel(poseStack, vertexConsumer, this.doubleRightModel, h, k, j);
                }
            } else {
                this.renderModel(poseStack, vertexConsumer, this.singleModel, h, k, j);
            }

            poseStack.popPose();
        }
    }
    
    private Material getMaterial(WoodType woodType, ChestType chestType) {
        String textureName = woodType.getTextureLocation(isTrapped, switch (chestType) {
            case LEFT -> "left";
            case RIGHT -> "right";
            default -> null;
        });
        
        return new Material(
            Sheets.CHEST_SHEET,
            ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, "entity/chest/" + textureName)
        );
    }
    
    private void renderModel(PoseStack poseStack, VertexConsumer vertexConsumer, ChestModel chestModel, float f, int i, int j) {
        chestModel.setupAnim(f);
        chestModel.renderToBuffer(poseStack, vertexConsumer, i, j);
    }
}