package me.lutto.shardsmp.listeners

import me.lutto.shardsmp.ShardSMP
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent

class LivesListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        shardSMP.livesManager.addPlayer(player.uniqueId)
        shardSMP.livesManager.updateListName(player)
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player: Player = event.entity
        val killer: Player = player.killer ?: return

        if (player.displayName() == killer.displayName()) return

        if (killer.type == EntityType.PLAYER) {
            shardSMP.livesManager.removeLives(player.uniqueId, 1)
            player.sendRichMessage("<red>You now have ${shardSMP.livesManager.getLives(player.uniqueId)} lives!")
            shardSMP.livesManager.updateListName(player)
        }
    }

}