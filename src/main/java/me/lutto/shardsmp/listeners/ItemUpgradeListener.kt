package me.lutto.shardsmp.listeners

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.Upgradable
import me.lutto.shardsmp.items.events.ItemUpgradeEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemUpgradeListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onItemUpgrade(event: ItemUpgradeEvent) {
        val customItem: Upgradable = event.getCustomItem() as Upgradable
        val item: ItemStack = event.getItem()
        val itemMeta: ItemMeta = item.itemMeta
        itemMeta.setCustomModelData(customItem.getUpgradedCustomModelData())
        item.itemMeta = itemMeta
    }

}