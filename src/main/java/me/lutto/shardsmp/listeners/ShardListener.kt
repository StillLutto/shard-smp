package me.lutto.shardsmp.listeners

import me.lutto.shardsmp.ShardSMP
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent


class ShardListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player: Player = event.entity
        val killer: Player = event.entity.killer ?: return

        if (player.displayName() == killer.displayName()) return
        if (killer.type != EntityType.PLAYER) return

        if (killer.inventory.firstEmpty() == -1) {
            player.world.dropItemNaturally(player.location, shardSMP.itemManager.getItem("shard")?.getItemStack()!!)
            return
        }
        killer.inventory.addItem(shardSMP.itemManager.getItem("shard")?.getItemStack()!!)
    }

}