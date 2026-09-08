// common/src/main/java/net/vg/justvariants_campfires/mixin/BlockEntityTypeInvoker.java
package net.vg.extrachests.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeInvoker<T extends BlockEntity> {
    @FunctionalInterface
    interface Factory<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }

    @Invoker("<init>")
    static <T extends BlockEntity> BlockEntityType<T> invokeNew(
            @Coerce Factory<? extends T> factory,
            Set<Block> validBlocks
    ) {
        throw new AssertionError();
    }
}
