package com.gmail.woodyc40.lagger.util;

import org.bukkit.Bukkit;

public enum ServerVersion {
    V1_8("1.8"),
    V1_13("1.13"),
    V1_14("1.14"),
    V1_15("1.15"),
    V1_16("1.16");

    private final String verString;

    ServerVersion(String verString) {
        this.verString = verString;
    }

    public static ServerVersion getVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        for (ServerVersion sv : values()) {
            if (bukkitVersion.startsWith(sv.getVerString())) {
                return sv;
            }
        }

        return null;
    }

    public static boolean isPaper() {
        return Bukkit.getVersion().toLowerCase().contains("paper");
    }

    public String getVerString() {
        return this.verString;
    }
}
