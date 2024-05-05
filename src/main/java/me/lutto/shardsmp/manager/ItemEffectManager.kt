package me.lutto.shardsmp.manager

import me.lutto.shardsmp.ShardSMP
import org.bukkit.potion.PotionEffect

class ItemEffectManager(private val shardSMP: ShardSMP) {

    private var itemEffects: MutableMap<String, PotionEffect> = mutableMapOf()
    private var upgradedItems: MutableList<String> = mutableListOf()

    fun addItemEffect(itemId: String, effect: PotionEffect, upgradeRequired: Boolean) {
        itemEffects[itemId] = effect
        if (upgradeRequired) upgradedItems.add(itemId)
    }

    fun getItemEffects(): MutableMap<String, PotionEffect> = itemEffects
    fun isUpgradedItem(itemId: String): Boolean = upgradedItems.contains(itemId)

}