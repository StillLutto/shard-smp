package me.lutto.shardsmp.listeners.weapons

import me.lutto.shardsmp.AbilityActivateEvent
import me.lutto.shardsmp.ShardSMP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EarthShattererListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onAbilityActivated(event: AbilityActivateEvent) {
        if (event.getCustomItem() != shardSMP.itemManager.getItem("earth_shatterer")) return

        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.SPEED, 600, 1))
        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 600, 5))
    }

}