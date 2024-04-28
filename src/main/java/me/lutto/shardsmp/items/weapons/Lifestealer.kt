package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.persistence.PersistentDataType
import java.util.*

class Lifestealer(private val shardSMP: ShardSMP) : CustomItem(
    "lifestealer",
    Material.DIAMOND_SWORD,
    MiniMessage.miniMessage().deserialize("<gradient:#57301f:#417837><gradient:#aa0000:#ff2119>Lifestealer").decoration(TextDecoration.ITALIC, false)
        .decoration(TextDecoration.ITALIC, false),
    listOf(),
    9
), Listener {

    init {
        shardSMP.itemManager.registerItem(this)
    }

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
