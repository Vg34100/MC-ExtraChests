// common/src/main/java/net/vg/justvariants_campfires/mixin/BlockEntityTypeInvoker.java
package net.vg.justvariants_campfires.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeInvoker<T extends BlockEntity> {
    @Invoker("<init>")
    static <T extends BlockEntity> BlockEntityType<T> invokeNew(
            BlockEntityType.BlockEntitySupplier<? extends T> factory,
            Set<Block> validBlocks
    ) {
        throw new AssertionError();
    }
}
