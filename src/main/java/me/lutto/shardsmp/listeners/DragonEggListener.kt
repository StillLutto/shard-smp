package me.lutto.shardsmp.listeners

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.Upgradable
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

class DragonEggListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        if (event.inventory.holder !is Player) return
        if (!event.inventory.matrix.contains(ItemStack(Material.DRAGON_EGG))) return
        val player: Player = event.inventory.holder as Player

        var weapon: ItemStack? = null
        for (customItem in shardSMP.itemManager.getItemList()) {
            if (customItem !is Upgradable) continue
            player.sendMessage("upgradable")
            for (currentItem in event.inventory.matrix) {
                if (currentItem == null) continue
                val currentItemId = currentItem.itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "custom_item"), PersistentDataType.STRING]
                val customItemId = customItem.getItemStack().itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "custom_item"), PersistentDataType.STRING]
                if (currentItemId != customItemId) continue

                weapon = customItem.getItemStack().clone()
            }
        }
        if (weapon == null) return

        val weaponMeta: ItemMeta = weapon.itemMeta
        val weaponUUID: UUID = UUID.randomUUID()
        weaponMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] = weaponUUID.toString()
        weapon.itemMeta = weaponMeta

        shardSMP.itemManager.setUpgraded(weaponUUID, true)
        event.inventory.result = weapon
    }

}