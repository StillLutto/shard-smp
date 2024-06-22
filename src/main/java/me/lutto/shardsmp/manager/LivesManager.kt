package me.lutto.shardsmp.manager

import me.lutto.shardsmp.ShardSMP
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException
import java.util.*

class LivesManager(private val shardSMP: ShardSMP) {

    private var livesFile: File
    private var livesFileConfig: YamlConfiguration

    init {
        if (!shardSMP.dataFolder.exists()) {
            shardSMP.dataFolder.mkdir()
        }

        livesFile = File(shardSMP.dataFolder, "lives.yml")
        if (!livesFile.exists()) {
            try {
                livesFile.createNewFile()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        livesFileConfig = YamlConfiguration.loadConfiguration(livesFile)
    }

    fun updateListName(player: Player) {
        val playerLives: Int = getLives(player.uniqueId)
        player.playerListName(shardSMP.miniMessage.deserialize("<red>[$playerLives] <white>${PlainTextComponentSerializer.plainText().serialize(player.displayName())}"))
        if (playerLives >= 4) {
            player.playerListName(shardSMP.miniMessage.deserialize("<green>[$playerLives] <white>${PlainTextComponentSerializer.plainText().serialize(player.displayName())}"))
        } else if (playerLives >= 2) {
            player.playerListName(shardSMP.miniMessage.deserialize("<gold>[$playerLives] <white>${PlainTextComponentSerializer.plainText().serialize(player.displayName())}"))
        }
    }

    fun setLives(uuid: UUID, amount: Int): Boolean {
        if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) return false

        livesFileConfig[uuid.toString()] = amount
        try {
            livesFileConfig.save(livesFile)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return true
    }

    fun addLives(uuid: UUID, amount: Int): Boolean {
        if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) return false

        val playerLives = livesFileConfig.getInt(uuid.toString())

        if (amount <= 0) return false

        if (playerLives + amount > 5 || playerLives + amount < 1) return false

        livesFileConfig[uuid.toString()] = playerLives + amount
        try {
            livesFileConfig.save(livesFile)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return true
    }

    fun removeLives(uuid: UUID, amount: Int) {
        if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) return

        val playerLives = livesFileConfig.getInt(uuid.toString())

        if (playerLives - amount < 1) {
            val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
            offlinePlayer.banPlayer("You have lost all of your lives!")
            if (offlinePlayer.isOnline) {
                offlinePlayer.player!!.kick(Component.text("You have lost all your lives!", NamedTextColor.RED))
            }
            Bukkit.broadcast(Component.text("${PlainTextComponentSerializer.plainText().serialize(offlinePlayer.player!!.displayName())} lost all of their lives!", NamedTextColor.RED))

            livesFileConfig[uuid.toString()] = 0
        } else {
            livesFileConfig[uuid.toString()] = playerLives - amount
        }

        try {
            livesFileConfig.save(livesFile)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun addPlayer(uuid: UUID) {
        if (livesFileConfig.contains(uuid.toString())) return

        livesFileConfig[uuid.toString()] = 5
        try {
            livesFileConfig.save(livesFile)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun hasPlayer(uuid: UUID): Boolean = livesFileConfig.contains(uuid.toString())
    fun getLives(uuid: UUID): Int = livesFileConfig.getInt(uuid.toString())

}