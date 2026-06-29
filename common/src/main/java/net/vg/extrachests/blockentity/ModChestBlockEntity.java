// ModChestBlockEntity.java
package net.vg.extrachests.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vg.extrachests.block.ModChestBlock;
import net.vg.extrachests.util.WoodType;

public class ModChestBlockEntity extends ChestBlockEntity {
    private final WoodType woodType;

    public ModChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.woodType = ((ModChestBlock) state.getBlock()).getWoodType();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(woodType.getTranslationKey(false, false));
    }

    public WoodType getWoodType() { return woodType; }
}
