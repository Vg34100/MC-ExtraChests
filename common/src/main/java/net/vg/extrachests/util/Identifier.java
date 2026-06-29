package net.vg.justvariants_campfires.util;

import net.minecraft.resources.ResourceLocation;
import net.vg.justvariants_campfires.Justvariants_campfires;

public class Identifier {
    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(Justvariants_campfires.MOD_ID, path);
    }

    public static ResourceLocation of(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
