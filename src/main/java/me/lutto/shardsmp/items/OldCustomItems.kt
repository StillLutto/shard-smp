package me.lutto.shardsmp.items

import org.bukkit.ChatColor

enum class OldCustomItems(val id: String, val displayName: String) {

    SHARD("shard", ChatColor.DARK_PURPLE.toString() + "Shard"),
    LIFE("life", ChatColor.RED.toString() + "Life"),
    EARTH_SHATTERER("earth_shatterer", ChatColor.translateAlternateColorCodes('§', "§6§lEarth Shatterer")),
    ENDER_BOW("ender_bow", ChatColor.translateAlternateColorCodes('§', "§6§lEnder Bow")),
    LIFESTEALER("lifestealer", ChatColor.translateAlternateColorCodes('§', "§6§lLifestealer")),
    MJOLNIR("mjolnir", ChatColor.translateAlternateColorCodes('§', "§6§lMjölnir")),
    POSEIDON_TRIDENT("poseidon_trident", ChatColor.translateAlternateColorCodes('§', "§6§lPoseidon's Trident")),
    PYRO_SWORD("pyro_sword", ChatColor.translateAlternateColorCodes('§', "§6§lPyro Sword")),
    TANK_SHIELD("tank_shield", ChatColor.translateAlternateColorCodes('§', "§6§lTank Shield")),
    TITANS_EDGE("titans_edge", ChatColor.translateAlternateColorCodes('§', "§6§lTitans Edge")),
    VANISH_BLADE("vanish_blade", ChatColor.translateAlternateColorCodes('§', "§6§lVanish Blade"));

}