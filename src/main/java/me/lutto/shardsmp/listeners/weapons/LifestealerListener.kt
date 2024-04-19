package me.lutto.shardsmp.listeners.weapons

import me.lutto.shardsmp.ShardSMP
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.persistence.PersistentDataType
import java.util.*

class LifestealerListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val player: Player = event.damager as Player
        if (player.gameMode == GameMode.CREATIVE) return

        val itemInMainHand = player.inventory.itemInMainHand
        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        if (itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "lifestealer") return

        val chance = Random().nextInt(4) == 0
        if (!chance) return

        if (player.health + event.finalDamage > 20) {
            player.health = 20.0
            return
        }
        player.health += event.finalDamage
    }

}