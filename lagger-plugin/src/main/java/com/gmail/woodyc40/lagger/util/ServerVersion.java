package com.gmail.woodyc40.lagger.util;

import org.bukkit.Bukkit;

/**
 * An enumeration of API versions supported by this plugin.
 */
public enum ServerVersion {
    V1_8("1.8"),
    V1_13("1.13"),
    V1_14("1.14"),
    V1_15("1.15"),
    V1_16("1.16");

    private final String verString;

    /**
     * Creates a new version with the given dot-separated
     * major/minor version string.
     *
     * @param verString the version string
     */
    ServerVersion(String verString) {
        this.verString = verString;
    }

    /**
     * Determines the Bukkit API version of the currently-
     * running server.
     *
     * @return the current server version, or {@code null}
     * if it is not supported
     */
    public static ServerVersion getVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        for (ServerVersion sv : values()) {
            if (bukkitVersion.startsWith(sv.toString())) {
                return sv;
            }
        }

        return null;
    }

    /**
     * Determines whether the currently running server is
     * a PaperSpigot server.
     *
     * @return {@code true} if the server is PaperSpigot
     */
    public static boolean isPaper() {
        return Bukkit.getVersion().toLowerCase().contains("paper");
    }

    @Override
    public String toString() {
        return this.verString;
    }
}
