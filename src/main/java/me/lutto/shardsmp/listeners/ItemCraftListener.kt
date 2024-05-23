package me.lutto.shardsmp.listeners

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent

class ItemCraftListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onItemCraft(event: CraftItemEvent) {
        for (customItem: CustomItem in shardSMP.itemManager.getItemList()) {
            if (customItem.getRecipe()?.result != event.recipe.result) continue

            event.currentItem = customItem.getItemStack()
        }
    }

}