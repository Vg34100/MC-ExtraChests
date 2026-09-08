package net.vg.extrachests.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vg.extrachests.block.ModTrappedChestBlock;
import net.vg.extrachests.util.WoodType;

public class ModTrappedChestBlockEntity extends ChestBlockEntity {
    private final WoodType woodType;

    public ModTrappedChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        woodType = ((ModTrappedChestBlock) state.getBlock()).getWoodType();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(woodType.getTranslationKey(true, false));
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
        super.signalOpenCount(level, pos, state, oldCount, newCount);
        if (oldCount != newCount) {
            Block block = state.getBlock();
            level.updateNeighborsAt(pos, block);
            level.updateNeighborsAt(pos.below(), block);
        }
    }

    public WoodType getWoodType() { return woodType; }
}
