package net.vg.justvariants_campfires.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vg.justvariants_campfires.registry.ObjectRegistry;
import net.vg.justvariants_campfires.util.WoodType;

public class ModChestBlockEntity extends ChestBlockEntity {

    private final WoodType woodType;

    public ModChestBlockEntity(WoodType woodType, net.minecraft.world.level.block.entity.BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.woodType = woodType;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(woodType.getTranslationKey(false, false));
    }

    public WoodType getWoodType() {
        return woodType;
    }
}