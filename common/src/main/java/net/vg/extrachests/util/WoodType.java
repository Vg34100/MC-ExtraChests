package net.vg.justvariants_campfires.util;

import net.vg.justvariants_campfires.Justvariants_campfires;

public enum WoodType {
    OAK("oak"),
    BIRCH("birch"),
    SPRUCE("spruce"),
    JUNGLE("jungle"),
    ACACIA("acacia"),
    DARK_OAK("dark_oak"),
    MANGROVE("mangrove"),
    CHERRY("cherry"),
    PALE_OAK("pale_oak"),
    BAMBOO("bamboo"),
    CRIMSON("crimson"),
    WARPED("warped");

    private final String name;

    WoodType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey(boolean isTrapped, boolean isDouble) {
        StringBuilder key = new StringBuilder("container.");
        key.append(Justvariants_campfires.MOD_ID).append(".");
        key.append(name);
        if (isTrapped) {
            key.append("_trapped");
        }
        key.append("_chest");
        if (isDouble) {
            key.append("Double");
        }
        return key.toString();
    }

    public String getTextureLocation(boolean isTrapped, String variant) {
        StringBuilder texture = new StringBuilder(name);
        if (isTrapped) {
            texture.append("_trapped");
        }
        if (variant != null && !variant.isEmpty()) {
            texture.append("_").append(variant);
        }
        return texture.toString();
    }

    public String getDisplayName() {
        // Convert "dark_oak" -> "Dark Oak", "spruce" -> "Spruce", etc.
        return java.util.Arrays.stream(name.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(java.util.stream.Collectors.joining(" "));
    }

    public String getBlockTranslationKey(boolean isTrapped) {
        StringBuilder key = new StringBuilder("block.");
        key.append(Justvariants_campfires.MOD_ID).append(".");
        key.append(name);
        if (isTrapped) {
            key.append("_trapped");
        }
        key.append("_chest");
        return key.toString();
    }

    public String getItemTranslationKey(boolean isTrapped) {
        StringBuilder key = new StringBuilder("item.");
        key.append(Justvariants_campfires.MOD_ID).append(".");
        key.append(name);
        if (isTrapped) {
            key.append("_trapped");
        }
        key.append("_chest");
        return key.toString();
    }
}