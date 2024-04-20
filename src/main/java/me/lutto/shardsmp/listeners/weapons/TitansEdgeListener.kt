package me.lutto.shardsmp.listeners.weapons

import me.lutto.shardsmp.AbilityActivatedEvent
import me.lutto.shardsmp.ShardSMP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TitansEdgeListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onAbilityActivated(event: AbilityActivatedEvent) {
        if (event.getCustomItem() != shardSMP.itemManager.getItem("titans_edge")) return

        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 2))
    }

}