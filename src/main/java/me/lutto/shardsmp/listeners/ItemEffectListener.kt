package me.lutto.shardsmp.listeners

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.events.ItemUpgradeEvent
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import java.util.*

class ItemEffectListener(private val shardSMP: ShardSMP) : Listener {

    private fun checkItemEffect(item: ItemStack): PotionEffect? {
        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val uuidKey = NamespacedKey(shardSMP, "uuid")

        if (item.itemMeta == null) return null
        if (!item.itemMeta.persistentDataContainer.has(customItemKey)) return null
        if (!item.itemMeta.persistentDataContainer.has(uuidKey)) return null
        for ((itemId, effect) in shardSMP.itemEffectManager.getItemEffects()) {
            if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != itemId) continue
            val itemUUID: UUID =
                UUID.fromString(item.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
            if (shardSMP.itemEffectManager.isUpgradedItem(itemId) && !shardSMP.itemManager.isUpgraded(itemUUID)) continue

            return effect
        }

        return null
    }

    @EventHandler
    fun onPlayerInventorySlotChange(event: PlayerInventorySlotChangeEvent) {
        if (checkItemEffect(event.newItemStack) != null) {
            event.player.addPotionEffect(checkItemEffect(event.newItemStack)!!)
        } else if (checkItemEffect(event.oldItemStack) != null) {
            event.player.removePotionEffect(checkItemEffect(event.oldItemStack)!!.type)
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        for (item in event.player.inventory) {
            if (item == null) continue
            if (checkItemEffect(item) == null) continue
            event.player.addPotionEffect(checkItemEffect(item)!!)
        }
    }

    @EventHandler
    fun onItemUpgrade(event: ItemUpgradeEvent) {
        if (checkItemEffect(event.getItem()) != null) {
            event.getPlayer().addPotionEffect(checkItemEffect(event.getItem())!!)
        }
    }

}