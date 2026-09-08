package net.vg.extrachests.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.vg.extrachests.Extrachests;
import net.vg.extrachests.entity.LegacyChestBoat;
import net.vg.extrachests.item.LegacyChestBoatItem;
import net.vg.extrachests.util.WoodType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModChestBoatRegistries {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Extrachests.MOD_ID, Registries.ENTITY_TYPE);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Extrachests.MOD_ID, Registries.ITEM);

    private static final List<WoodType> BOAT_WOODS = List.of(
            WoodType.OAK, WoodType.BIRCH, WoodType.SPRUCE, WoodType.JUNGLE, WoodType.ACACIA,
            WoodType.DARK_OAK, WoodType.MANGROVE, WoodType.CHERRY, WoodType.BAMBOO);
    private static final List<WoodType> CHEST_WOODS = List.of(
            WoodType.BIRCH, WoodType.SPRUCE, WoodType.JUNGLE, WoodType.ACACIA, WoodType.DARK_OAK,
            WoodType.MANGROVE, WoodType.CHERRY, WoodType.BAMBOO, WoodType.WARPED, WoodType.CRIMSON);

    public static final class ComboKey {
        public final WoodType boatWood;
        public final WoodType chestWood;
        public final boolean raft;

        public ComboKey(WoodType boatWood, WoodType chestWood) {
            this.boatWood = boatWood;
            this.chestWood = chestWood;
            this.raft = boatWood == WoodType.BAMBOO;
        }

        public String idPath() {
            return raft ? "bamboo_raft_" + chestWood.getName() + "_chest"
                    : boatWood.getName() + "_" + chestWood.getName() + "_chest_boat";
        }

        public String texturePath() {
            String name = raft ? "bamboo_" + chestWood.getName() : boatWood.getName() + "_" + chestWood.getName();
            return "textures/entity/chest_boat/" + name + ".png";
        }
    }

    private static final Map<ComboKey, RegistrySupplier<? extends EntityType<? extends Boat>>> ENTITY_BY_COMBO = new LinkedHashMap<>();
    private static final Map<ComboKey, RegistrySupplier<Item>> ITEM_BY_COMBO = new LinkedHashMap<>();

    public static Map<ComboKey, RegistrySupplier<? extends EntityType<? extends Boat>>> entities() { return ENTITY_BY_COMBO; }
    public static Map<ComboKey, RegistrySupplier<Item>> items() { return ITEM_BY_COMBO; }
    public static List<WoodType> boatWoods() { return BOAT_WOODS; }
    public static List<WoodType> chestWoods() { return CHEST_WOODS; }

    public static void registerAll() {
        for (WoodType boat : BOAT_WOODS) for (WoodType chest : CHEST_WOODS) registerOne(new ComboKey(boat, chest));
        ENTITY_TYPES.register();
        ITEMS.register();
    }

    private static void registerOne(ComboKey key) {
        String id = key.idPath();
        RegistrySupplier<Item>[] itemRef = new RegistrySupplier[1];
        RegistrySupplier<EntityType<LegacyChestBoat>> entity = ENTITY_TYPES.register(id, () ->
                EntityType.Builder.<LegacyChestBoat>of(
                                (type, level) -> new LegacyChestBoat(type, level, boatType(key.boatWood), () -> itemRef[0].get()),
                                MobCategory.MISC)
                        .sized(1.375F, 0.5625F).clientTrackingRange(10).updateInterval(3).build(id));
        RegistrySupplier<Item> item = ITEMS.register(id, () ->
                new LegacyChestBoatItem(entity, new Item.Properties().stacksTo(1)));
        itemRef[0] = item;
        ENTITY_BY_COMBO.put(key, entity);
        ITEM_BY_COMBO.put(key, item);
    }

    public static Boat.Type boatType(WoodType wood) {
        return switch (wood) {
            case OAK -> Boat.Type.OAK;
            case BIRCH -> Boat.Type.BIRCH;
            case SPRUCE -> Boat.Type.SPRUCE;
            case JUNGLE -> Boat.Type.JUNGLE;
            case ACACIA -> Boat.Type.ACACIA;
            case DARK_OAK -> Boat.Type.DARK_OAK;
            case MANGROVE -> Boat.Type.MANGROVE;
            case CHERRY -> Boat.Type.CHERRY;
            case BAMBOO -> Boat.Type.BAMBOO;
            default -> throw new IllegalStateException("No legacy boat type for " + wood);
        };
    }

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
            case BAMBOO -> Items.BAMBOO_RAFT;
            default -> throw new IllegalStateException("Unexpected boat wood " + boat);
        };
    }

    private ModChestBoatRegistries() {}
}
