package net.vg.extrachests.util;

import net.minecraft.resources.ResourceLocation;
import net.vg.extrachests.Extrachests;

public class Identifier {
    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(Extrachests.MOD_ID, path);
    }

    public static ResourceLocation of(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
