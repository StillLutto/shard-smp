package me.lutto.shardsmp.listeners

import me.lutto.shardsmp.ShardSMP
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent

class LivesListener(private val shardSMP: ShardSMP) : Listener {

    private fun setPlayerlistName(player: Player) {
        val playerLives: Int = shardSMP.livesManager.getLives(player.uniqueId)
        player.playerListName(MiniMessage.miniMessage().deserialize("<red>[$playerLives] <white>${PlainTextComponentSerializer.plainText().serialize(player.displayName())}"))
        if (playerLives >= 4) {
            player.playerListName(MiniMessage.miniMessage().deserialize("<green>[$playerLives] <white>${PlainTextComponentSerializer.plainText().serialize(player.displayName())}"))
        } else if (playerLives >= 2) {
            player.playerListName(MiniMessage.miniMessage().deserialize("<gold>[$playerLives] <white>${PlainTextComponentSerializer.plainText().serialize(player.displayName())}"))
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        shardSMP.livesManager.addPlayer(player.uniqueId)

        setPlayerlistName(player)
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player: Player = event.entity
        val killer: Player = player.killer ?: return

        if (player.displayName() == killer.displayName()) return

        if (killer.type == EntityType.PLAYER) {
            shardSMP.livesManager.removeLives(player.uniqueId, 1)
            player.sendRichMessage("<red>You now have ${shardSMP.livesManager.getLives(player.uniqueId)} lives!")
            setPlayerlistName(player)
        }
    }

}