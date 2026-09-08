// ModChestRegistries.java (common)
package net.vg.extrachests.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
import net.vg.extrachests.mixin.BlockEntityTypeInvoker;
import net.vg.extrachests.util.Identifier;
import net.vg.extrachests.util.WoodType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class ModChestRegistries {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Extrachests.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Extrachests.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Extrachests.MOD_ID, Registries.ITEM);

    // define what you want once
    private static final List<WoodType> WOODS = List.of(
            WoodType.SPRUCE,
            WoodType.ACACIA,
            WoodType.BIRCH,
            WoodType.JUNGLE,
            WoodType.BAMBOO,
            WoodType.CHERRY,
            WoodType.CRIMSON,
            WoodType.DARK_OAK,
            WoodType.PALE_OAK,
            WoodType.WARPED
            // add more: WoodType.OAK, WoodType.BIRCH, ...
    );
    // at the top of ModChestRegistries (once)
    private static final Map<WoodType, RegistrySupplier<BlockEntityType<ModChestBlockEntity>>> CHEST_TYPES =
            new EnumMap<>(WoodType.class);
    private static final Map<WoodType, RegistrySupplier<BlockEntityType<ModTrappedChestBlockEntity>>> TRAPPED_TYPES =
            new EnumMap<>(WoodType.class);
    public static Map<WoodType, RegistrySupplier<BlockEntityType<ModChestBlockEntity>>> chestTypes() { return CHEST_TYPES; }
    public static Map<WoodType, RegistrySupplier<BlockEntityType<ModTrappedChestBlockEntity>>> trappedTypes() { return TRAPPED_TYPES; }

    private static final Map<WoodType, RegistrySupplier<Block>> CHEST_BLOCKS = new EnumMap<>(WoodType.class);
    private static final Map<WoodType, RegistrySupplier<Block>> TRAPPED_BLOCKS = new EnumMap<>(WoodType.class);
    public static Map<WoodType, RegistrySupplier<Block>> chestBlocks()   { return CHEST_BLOCKS; }
    public static Map<WoodType, RegistrySupplier<Block>> trappedBlocks() { return TRAPPED_BLOCKS; }
    public static List<WoodType> getWOODS() { return WOODS; }


    public static void registerAll() {
        // normal chests
        for (WoodType wood : WOODS) {
            registerChest(wood, false);
        }
        // trapped chests (do the same woods here if you want them)
        for (WoodType wood : WOODS) {
            registerChest(wood, true);
        }

        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
        ITEMS.register();
    }

    private static void registerChest(WoodType wood, boolean trapped) {
        String base = wood.getName(); // e.g., "spruce"
        String id = trapped ? base + "_trapped_chest" : base + "_chest";

        // keys
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK,
                Identifier.of(Extrachests.MOD_ID, id));
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                Identifier.of(Extrachests.MOD_ID, id));

        // break the block <-> BE type cycle with tiny refs
        final RegistrySupplier<? extends BlockEntityType<? extends ChestBlockEntity>>[] beRef = new RegistrySupplier[1];

        // BLOCK
        RegistrySupplier<Block> blockSup = BLOCKS.register(id, () -> {
            BlockBehaviour.Properties props = BlockBehaviour.Properties.of().setId(blockKey);
            Supplier<BlockEntityType<? extends ChestBlockEntity>> beTypeSupplier =
                    () -> (BlockEntityType<? extends ChestBlockEntity>) beRef[0].get();

            return trapped
                    ? new ModTrappedChestBlock(wood, beTypeSupplier, props)
                    : new ModChestBlock(wood, beTypeSupplier, props);
        });

        // BLOCK ENTITY TYPE (self-capture its own type; include block in valid set)
        if (trapped) {
            RegistrySupplier<BlockEntityType<ModTrappedChestBlockEntity>> beSup =
                    BLOCK_ENTITY_TYPES.register(id, () -> {
                        final BlockEntityType<ModTrappedChestBlockEntity>[] self = new BlockEntityType[1];
                        BlockEntityTypeInvoker.Factory<ModTrappedChestBlockEntity> factory =
                                (pos, state) -> new ModTrappedChestBlockEntity(self[0], pos, state);
                        BlockEntityType<ModTrappedChestBlockEntity> type =
                                BlockEntityTypeInvoker.invokeNew(factory, Set.of(blockSup.get()));
                        self[0] = type;
                        return type;
                    });

            beRef[0] = beSup;                 // finish the cycle for the block ctor
            TRAPPED_TYPES.put(wood, beSup);   // stash for renderer registration
            TRAPPED_BLOCKS.put(wood, blockSup);
        } else {
            RegistrySupplier<BlockEntityType<ModChestBlockEntity>> beSup =
                    BLOCK_ENTITY_TYPES.register(id, () -> {
                        final BlockEntityType<ModChestBlockEntity>[] self = new BlockEntityType[1];
                        BlockEntityTypeInvoker.Factory<ModChestBlockEntity> factory =
                                (pos, state) -> new ModChestBlockEntity(self[0], pos, state);
                        BlockEntityType<ModChestBlockEntity> type =
                                BlockEntityTypeInvoker.invokeNew(factory, Set.of(blockSup.get()));
                        self[0] = type;
                        return type;
                    });

            beRef[0] = beSup;
            CHEST_TYPES.put(wood, beSup);
            CHEST_BLOCKS.put(wood, blockSup);
        }

        // ITEM
        ITEMS.register(id, () -> new BlockItem(blockSup.get(), new Item.Properties().setId(itemKey)));
    }

    private ModChestRegistries() {}
}
