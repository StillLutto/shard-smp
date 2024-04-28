package me.lutto.shardsmp.items.miscellaneous

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class Shard(private val shardSMP: ShardSMP) : CustomItem(
    "shard",
    Material.ECHO_SHARD,
    MiniMessage.miniMessage().deserialize("<dark_purple>Shard").decoration(TextDecoration.ITALIC, false)
        .decoration(TextDecoration.ITALIC, false),
    listOf(),
    1
), Listener {

    init {
        shardSMP.itemManager.registerItem(this)
    }

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
