package net.vg.extrachests.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public final class LegacyChestBoat extends ChestBoat {
    private final Boat.Type variant;
    private final Supplier<Item> dropItem;

    public LegacyChestBoat(EntityType<? extends Boat> type, Level level, Boat.Type variant, Supplier<Item> dropItem) {
        super(type, level);
        this.variant = variant;
        this.dropItem = dropItem;
        setVariant(variant);
    }

    @Override
    public void setVariant(Boat.Type ignored) {
        super.setVariant(variant == null ? ignored : variant);
    }

    @Override
    public Item getDropItem() {
        return dropItem.get();
    }
}
