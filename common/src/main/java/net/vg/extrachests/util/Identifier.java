package net.vg.extrachests.util;

import net.vg.extrachests.Extrachests;

public class Identifier {
    public static net.minecraft.resources.Identifier of(String path) {
        return net.minecraft.resources.Identifier.fromNamespaceAndPath(Extrachests.MOD_ID, path);
    }

    public static net.minecraft.resources.Identifier of(String namespace, String path) {
        return net.minecraft.resources.Identifier.fromNamespaceAndPath(namespace, path);
    }
}
