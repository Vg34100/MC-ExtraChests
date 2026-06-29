// ModTrappedChestBlock.java
package net.vg.extrachests.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvents;
import net.vg.extrachests.blockentity.ModTrappedChestBlockEntity;
import net.vg.extrachests.util.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class ModTrappedChestBlock extends ChestBlock {
    private final WoodType woodType;
    private final Supplier<BlockEntityType<? extends ChestBlockEntity>> beTypeSupplier;

    public ModTrappedChestBlock(WoodType woodType,
                                Supplier<BlockEntityType<? extends ChestBlockEntity>> beTypeSupplier,
                                BlockBehaviour.Properties props) {
        super(beTypeSupplier, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, props);
        this.woodType = woodType;
        this.beTypeSupplier = beTypeSupplier;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModTrappedChestBlockEntity((BlockEntityType<?>) beTypeSupplier.get(), pos, state);
    }

    // UI/menu bits (same as your version)
    private final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> menuCombiner =
            new DoubleBlockCombiner.Combiner<>() {
                public Optional<MenuProvider> acceptDouble(ChestBlockEntity a, ChestBlockEntity b) {
                    Container container = new CompoundContainer(a, b);
                    return Optional.of(new MenuProvider() {
                        @Nullable
                        public AbstractContainerMenu createMenu(int i, Inventory inv, Player p) {
                            if (a.canOpen(p) && b.canOpen(p)) {
                                a.unpackLootTable(inv.player);
                                b.unpackLootTable(inv.player);
                                return ChestMenu.sixRows(i, inv, container);
                            }
                            return null;
                        }
                        public Component getDisplayName() {
                            if (a.hasCustomName()) return a.getDisplayName();
                            if (b.hasCustomName()) return b.getDisplayName();
                            return Component.translatable(woodType.getTranslationKey(true, true));
                        }
                    });
                }
                public Optional<MenuProvider> acceptSingle(ChestBlockEntity be) { return Optional.of(be); }
                public Optional<MenuProvider> acceptNone() { return Optional.empty(); }
            };

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return this.combine(state, level, pos, false).apply(this.menuCombiner).orElse(null);
    }

    // redstone methods same as your class (ChestBlockEntity.getOpenCount etc.)
    @Override public boolean isSignalSource(BlockState s) { return true; }
    @Override public int getSignal(BlockState s, BlockGetter g, BlockPos p, net.minecraft.core.Direction d) {
        return net.minecraft.util.Mth.clamp(ChestBlockEntity.getOpenCount(g, p), 0, 15);
    }
    @Override public int getDirectSignal(BlockState s, BlockGetter g, BlockPos p, net.minecraft.core.Direction d) {
        return d == net.minecraft.core.Direction.UP ? s.getSignal(g, p, d) : 0;
    }

    public WoodType getWoodType() { return woodType; }
}
