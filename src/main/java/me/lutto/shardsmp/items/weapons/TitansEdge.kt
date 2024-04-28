package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.items.events.AbilityActivateEvent
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TitansEdge(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "titans_edge",
    Material.DIAMOND_SWORD,
    MiniMessage.miniMessage().deserialize("<gradient:#aa0000:#ff5555>Titans Edge")
        .decoration(TextDecoration.ITALIC, false),
    listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
    5,
    true,
    120,
    true
), Listener {

    init {
        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onAbilityActivated(event: AbilityActivateEvent) {
        if (event.getItem() != shardSMP.itemManager.getItem("titans_edge")) return

        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 2))
    }

}
