package me.lutto.shardsmp.items.miscellaneous

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.event.Listener

class Life(private val shardSMP: ShardSMP) : CustomItem(
    "life",
    Material.RABBIT_FOOT,
    MiniMessage.miniMessage().deserialize("<color:#ff1c27>Life").decoration(TextDecoration.ITALIC, false)
        .decoration(TextDecoration.ITALIC, false),
    listOf(),
    2
), Listener {

    init {
        shardSMP.itemManager.registerItem(this)
    }

}
