package net.vg.extrachests.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.block.ModChestBlock;
import net.vg.extrachests.block.ModTrappedChestBlock;
import net.vg.extrachests.blockentity.ModChestBlockEntity;
import net.vg.extrachests.blockentity.ModTrappedChestBlockEntity;
import net.vg.extrachests.util.WoodType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ModChestRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Extrachests.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Extrachests.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Extrachests.MOD_ID, Registries.ITEM);

    private static final List<WoodType> WOODS = List.of(
            WoodType.SPRUCE, WoodType.ACACIA, WoodType.BIRCH, WoodType.JUNGLE,
            WoodType.BAMBOO, WoodType.CHERRY, WoodType.CRIMSON,
            WoodType.DARK_OAK, WoodType.WARPED
    );
    private static final Map<WoodType, RegistrySupplier<BlockEntityType<ModChestBlockEntity>>> CHEST_TYPES = new EnumMap<>(WoodType.class);
    private static final Map<WoodType, RegistrySupplier<BlockEntityType<ModTrappedChestBlockEntity>>> TRAPPED_TYPES = new EnumMap<>(WoodType.class);
    private static final Map<WoodType, RegistrySupplier<Block>> CHEST_BLOCKS = new EnumMap<>(WoodType.class);
    private static final Map<WoodType, RegistrySupplier<Block>> TRAPPED_BLOCKS = new EnumMap<>(WoodType.class);

    public static Map<WoodType, RegistrySupplier<BlockEntityType<ModChestBlockEntity>>> chestTypes() { return CHEST_TYPES; }
    public static Map<WoodType, RegistrySupplier<BlockEntityType<ModTrappedChestBlockEntity>>> trappedTypes() { return TRAPPED_TYPES; }
    public static Map<WoodType, RegistrySupplier<Block>> chestBlocks() { return CHEST_BLOCKS; }
    public static Map<WoodType, RegistrySupplier<Block>> trappedBlocks() { return TRAPPED_BLOCKS; }
    public static List<WoodType> getWOODS() { return WOODS; }

    public static void registerAll() {
        for (WoodType wood : WOODS) registerChest(wood, false);
        for (WoodType wood : WOODS) registerChest(wood, true);
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
        ITEMS.register();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerChest(WoodType wood, boolean trapped) {
        String id = wood.getName() + (trapped ? "_trapped_chest" : "_chest");
        RegistrySupplier<? extends BlockEntityType<? extends ChestBlockEntity>>[] beRef = new RegistrySupplier[1];
        RegistrySupplier<Block> block = BLOCKS.register(id, () -> {
            Supplier<BlockEntityType<? extends ChestBlockEntity>> type = () -> beRef[0].get();
            return trapped
                    ? new ModTrappedChestBlock(wood, type, BlockBehaviour.Properties.of())
                    : new ModChestBlock(wood, type, BlockBehaviour.Properties.of());
        });

        if (trapped) {
            RegistrySupplier<BlockEntityType<ModTrappedChestBlockEntity>> type = BLOCK_ENTITY_TYPES.register(id, () -> {
                BlockEntityType<ModTrappedChestBlockEntity>[] self = new BlockEntityType[1];
                BlockEntityType<ModTrappedChestBlockEntity> value = BlockEntityType.Builder
                        .of((pos, state) -> new ModTrappedChestBlockEntity(self[0], pos, state), block.get()).build(null);
                self[0] = value;
                return value;
            });
            beRef[0] = type;
            TRAPPED_TYPES.put(wood, type);
            TRAPPED_BLOCKS.put(wood, block);
        } else {
            RegistrySupplier<BlockEntityType<ModChestBlockEntity>> type = BLOCK_ENTITY_TYPES.register(id, () -> {
                BlockEntityType<ModChestBlockEntity>[] self = new BlockEntityType[1];
                BlockEntityType<ModChestBlockEntity> value = BlockEntityType.Builder
                        .of((pos, state) -> new ModChestBlockEntity(self[0], pos, state), block.get()).build(null);
                self[0] = value;
                return value;
            });
            beRef[0] = type;
            CHEST_TYPES.put(wood, type);
            CHEST_BLOCKS.put(wood, block);
        }
        ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private ModChestRegistries() {}
}
