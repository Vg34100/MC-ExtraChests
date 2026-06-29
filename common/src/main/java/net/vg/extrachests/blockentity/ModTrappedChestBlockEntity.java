package net.vg.justvariants_campfires.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import net.vg.justvariants_campfires.registry.ObjectRegistry;
import net.vg.justvariants_campfires.util.WoodType;

public class ModTrappedChestBlockEntity extends ChestBlockEntity {

    private final WoodType woodType;

    public ModTrappedChestBlockEntity(WoodType woodType, net.minecraft.world.level.block.entity.BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.woodType = woodType;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(woodType.getTranslationKey(true, false));
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
        super.signalOpenCount(level, blockPos, blockState, i, j);
        if (i != j) {
            Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, ((Direction)blockState.getValue(TrappedChestBlock.FACING)).getOpposite(), Direction.UP);
            Block block = blockState.getBlock();
            level.updateNeighborsAt(blockPos, block, orientation);
            level.updateNeighborsAt(blockPos.below(), block, orientation);
        }
    }

    public WoodType getWoodType() {
        return woodType;
    }
}