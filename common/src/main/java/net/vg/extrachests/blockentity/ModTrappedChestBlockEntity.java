// ModTrappedChestBlockEntity.java
package net.vg.extrachests.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import net.vg.extrachests.block.ModTrappedChestBlock;
import net.vg.extrachests.util.WoodType;

public class ModTrappedChestBlockEntity extends ChestBlockEntity {
    private final WoodType woodType;

    // same pattern as normal chest: take the type in ctor
    public ModTrappedChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.woodType = ((ModTrappedChestBlock) state.getBlock()).getWoodType();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(woodType.getTranslationKey(true, false));
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
        super.signalOpenCount(level, pos, state, oldCount, newCount);
        if (oldCount != newCount) {
            Orientation o = ExperimentalRedstoneUtils.initialOrientation(
                    level,
                    state.getValue(TrappedChestBlock.FACING).getOpposite(),
                    net.minecraft.core.Direction.UP
            );
            Block b = state.getBlock();
            level.updateNeighborsAt(pos, b, o);
            level.updateNeighborsAt(pos.below(), b, o);
        }
    }

    public WoodType getWoodType() { return woodType; }
}
