package net.vg.extrachests.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.vg.extrachests.util.Identifier;

/** Fresh properties, with the exact registered ID assigned before construction. */
public final class ModProperties {
    public static Item.Properties item(String path) {
        Item.Properties properties = new Item.Properties();
        properties.setId(ResourceKey.create(Registries.ITEM, Identifier.of(path)));
        return properties;
    }

    public static BlockBehaviour.Properties block(String path) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        properties.setId(ResourceKey.create(Registries.BLOCK, Identifier.of(path)));
        return properties;
    }

    private ModProperties() {}
}
