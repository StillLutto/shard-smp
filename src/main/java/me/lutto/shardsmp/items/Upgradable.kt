package me.lutto.shardsmp.items

interface Upgradable {
    fun getUpgradedCooldownTime(): Int
    fun getUpgradedCustomModelData(): Int
}