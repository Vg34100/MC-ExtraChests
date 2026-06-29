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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vg.extrachests.blockentity.ModChestBlockEntity;
import net.vg.extrachests.util.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class ModChestBlock extends ChestBlock {

    private final WoodType woodType;
    private final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> menuProviderCombiner;
    private final Supplier<BlockEntityType<? extends ChestBlockEntity>> beTypeSupplier;

    public ModChestBlock(WoodType woodType,
                         Supplier<BlockEntityType<? extends ChestBlockEntity>> beTypeSupplier,
                         Properties props) {
        super(beTypeSupplier, props);
        this.woodType = woodType;
        this.beTypeSupplier = beTypeSupplier;

        this.menuProviderCombiner = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
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
                                    Component.translatable(woodType.getTranslationKey(false, true));
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
        return new ModChestBlockEntity((BlockEntityType<?>) beTypeSupplier.get(), pos, state);
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return this.combine(state, level, pos, false)
                .apply(this.menuProviderCombiner).orElse(null);
    }

    public WoodType getWoodType() {
        return woodType;
    }

}