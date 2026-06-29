package net.vg.justvariants_campfires.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
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
import net.vg.justvariants_campfires.blockentity.ModTrappedChestBlockEntity;
import net.vg.justvariants_campfires.util.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class ModTrappedChestBlock extends ChestBlock {
    
    private final WoodType woodType;
    private final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> trappedMenuProviderCombiner;
    
    public ModTrappedChestBlock(WoodType woodType, Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntitySupplier, BlockBehaviour.Properties properties) {
        super(blockEntitySupplier, properties);
        this.woodType = woodType;
        
        this.trappedMenuProviderCombiner = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
            public Optional<MenuProvider> acceptDouble(final ChestBlockEntity chestBlockEntity, final ChestBlockEntity chestBlockEntity2) {
                final Container container = new CompoundContainer(chestBlockEntity, chestBlockEntity2);
                return Optional.of(new MenuProvider() {
                    @Nullable
                    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                        if (chestBlockEntity.canOpen(player) && chestBlockEntity2.canOpen(player)) {
                            chestBlockEntity.unpackLootTable(inventory.player);
                            chestBlockEntity2.unpackLootTable(inventory.player);
                            return ChestMenu.sixRows(i, inventory, container);
                        } else {
                            return null;
                        }
                    }

                    public Component getDisplayName() {
                        if (chestBlockEntity.hasCustomName()) {
                            return chestBlockEntity.getDisplayName();
                        } else {
                            return chestBlockEntity2.hasCustomName() ? chestBlockEntity2.getDisplayName() : 
                                Component.translatable(woodType.getTranslationKey(true, true));
                        }
                    }
                });
            }

            public Optional<MenuProvider> acceptSingle(ChestBlockEntity chestBlockEntity) {
                return Optional.of(chestBlockEntity);
            }

            public Optional<MenuProvider> acceptNone() {
                return Optional.empty();
            }
        };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModTrappedChestBlockEntity(woodType, this.blockEntityType.get(), pos, state);
    }
    
    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return this.combine(state, level, pos, false)
                .apply(this.trappedMenuProviderCombiner).orElse(null);
    }
    
    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
        return Mth.clamp(ChestBlockEntity.getOpenCount(getter, pos), 0, 15);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? state.getSignal(getter, pos, direction) : 0;
    }

    public WoodType getWoodType() {
        return woodType;
    }
}