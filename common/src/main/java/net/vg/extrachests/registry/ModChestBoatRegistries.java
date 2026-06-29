// common/src/main/java/net/vg/justvariants_campfires/registry/ModChestBoatRegistries.java
package net.vg.extrachests.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.ChestRaft;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.util.Identifier;
import net.vg.extrachests.util.WoodType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModChestBoatRegistries {
    private ModChestBoatRegistries() {}

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Extrachests.MOD_ID, Registries.ENTITY_TYPE);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Extrachests.MOD_ID, Registries.ITEM);

    /** boat woods (bamboo => raft) */
    private static final List<WoodType> BOAT_WOODS = List.of(
            WoodType.OAK, WoodType.BIRCH, WoodType.SPRUCE, WoodType.JUNGLE,
            WoodType.ACACIA, WoodType.DARK_OAK, WoodType.MANGROVE,
            WoodType.CHERRY, WoodType.PALE_OAK, WoodType.BAMBOO // raft
    );

    /** chest woods (skip OAK; vanilla already has oak-chest boats) */
    private static final List<WoodType> CHEST_WOODS = List.of(
            WoodType.BIRCH, WoodType.SPRUCE, WoodType.JUNGLE, WoodType.ACACIA,
            WoodType.DARK_OAK, WoodType.MANGROVE, WoodType.CHERRY,
            WoodType.PALE_OAK, WoodType.BAMBOO, WoodType.WARPED, WoodType.CRIMSON
    );

    /** combo key */
    public static final class ComboKey {
        public final WoodType boatWood;
        public final WoodType chestWood;
        public final boolean raft;

        public ComboKey(WoodType boatWood, WoodType chestWood) {
            this.boatWood = boatWood;
            this.chestWood = chestWood;
            this.raft = (boatWood == WoodType.BAMBOO);
        }

        /** boats: "<boat>_<chest>_chest_boat", raft: "bamboo_raft_<chest>_chest" */
        public String idPath() {
            if (raft) return "bamboo_raft_" + chestWood.getName() + "_chest";
            return boatWood.getName() + "_" + chestWood.getName() + "_chest_boat";
        }

        /** we keep everything in chest_boat/ like vanilla */
        public String texturePath() {
            String name = (boatWood == WoodType.BAMBOO)
                    ? "bamboo_" + chestWood.getName()
                    : boatWood.getName() + "_" + chestWood.getName();
            return "textures/entity/chest_boat/" + name + ".png";
        }
    }

    /** entity + item lookups */
    private static final Map<ComboKey, RegistrySupplier<? extends EntityType<? extends AbstractBoat>>> ENTITY_BY_COMBO = new LinkedHashMap<>();
    private static final Map<ComboKey, RegistrySupplier<Item>> ITEM_BY_COMBO = new LinkedHashMap<>();

    public static Map<ComboKey, RegistrySupplier<? extends EntityType<? extends AbstractBoat>>> entities() { return ENTITY_BY_COMBO; }
    public static Map<ComboKey, RegistrySupplier<Item>> items() { return ITEM_BY_COMBO; }
    public static List<WoodType> boatWoods() { return BOAT_WOODS; }
    public static List<WoodType> chestWoods() { return CHEST_WOODS; }

    public static void registerAll() {
        for (WoodType boat : BOAT_WOODS) {
            // if you prefer to drive from chestBlocks(), swap the next line
            for (WoodType chest : CHEST_WOODS) {
                if (chest == WoodType.OAK) continue; // vanilla covers oak chest
                registerOne(new ComboKey(boat, chest));
            }
            // alt (drive from registered chest blocks):
            // for (WoodType chest : ModChestRegistries.chestBlocks().keySet()) { ... }
        }
        ENTITY_TYPES.register();
        ITEMS.register();
    }

    private static void registerOne(ComboKey key) {
        String id = key.idPath();

        // ENTITY
        ResourceKey<EntityType<?>> etKey = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.of(id));

        if (key.raft) {
            RegistrySupplier<EntityType<ChestRaft>> raftType = ENTITY_TYPES.register(id, () ->
                    EntityType.Builder.<ChestRaft>of(
                                    (type, level) -> new ChestRaft(type, level, () -> items().get(key).get()),
                                    MobCategory.MISC
                            )
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .updateInterval(3)
                            .build(etKey)
            );
            ENTITY_BY_COMBO.put(key, raftType);
        } else {
            RegistrySupplier<EntityType<ChestBoat>> boatType = ENTITY_TYPES.register(id, () ->
                    EntityType.Builder.<ChestBoat>of(
                                    (type, level) -> new ChestBoat(type, level, () -> items().get(key).get()),
                                    MobCategory.MISC
                            )
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .updateInterval(3)
                            .build(etKey)
            );
            ENTITY_BY_COMBO.put(key, boatType);
        }

        // ITEM (BoatItem spawns whatever type we made above)
        var itemKey = ResourceKey.create(Registries.ITEM, Identifier.of(id));
        RegistrySupplier<Item> itemSup = ITEMS.register(id, () -> {
            EntityType<? extends AbstractBoat> et = ENTITY_BY_COMBO.get(key).get();
            return new BoatItem(et, new Item.Properties().stacksTo(1).setId(itemKey));
        });
        ITEM_BY_COMBO.put(key, itemSup);
    }

    /** vanilla base boats/raft for recipes */
    public static Item vanillaBoatItem(WoodType boat) {
        return switch (boat) {
            case OAK -> Items.OAK_BOAT;
            case BIRCH -> Items.BIRCH_BOAT;
            case SPRUCE -> Items.SPRUCE_BOAT;
            case JUNGLE -> Items.JUNGLE_BOAT;
            case ACACIA -> Items.ACACIA_BOAT;
            case DARK_OAK -> Items.DARK_OAK_BOAT;
            case MANGROVE -> Items.MANGROVE_BOAT;
            case CHERRY -> Items.CHERRY_BOAT;
            case PALE_OAK -> Items.PALE_OAK_BOAT;
            case BAMBOO -> Items.BAMBOO_RAFT; // raft
            default -> throw new IllegalStateException("Unexpected value: " + boat);
        };
    }
}
