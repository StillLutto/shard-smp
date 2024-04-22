package me.lutto.shardsmp.listeners.weapons

import me.lutto.shardsmp.AbilityActivateEvent
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.instance.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TankShieldListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onAbilityActivate(event: AbilityActivateEvent) {
        if (event.getCustomItem() != shardSMP.itemManager.getItem("tank_shield")) return
        val player: Player = event.getPlayer()
        val customItem: CustomItem = event.getCustomItem()

        player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 2))
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 400, 2))

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.02
        player.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.baseValue = 0.02

        val actionbarWarning = Bukkit.getScheduler().runTaskTimer(shardSMP,
            Runnable {
                player.sendActionBar(Component.text("You can't attack for 5 seconds!", NamedTextColor.RED))
            }, 1, 40
        )

        customItem.setIsActivated(true)
        Bukkit.getScheduler().runTaskLater(shardSMP, Runnable {
            customItem.setIsActivated(false)
            actionbarWarning.cancel()
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1
        }, 400)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val player: Player = event.damager as Player

        for (item in player.inventory) {
            val customItemKey = NamespacedKey(shardSMP, "custom_item")
            val uuidKey = NamespacedKey(shardSMP, "uuid")

            if (item.itemMeta == null) continue
            if (!item.itemMeta.persistentDataContainer.has(customItemKey)) continue
            if (!item.itemMeta.persistentDataContainer.has(uuidKey)) continue
            if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "tank_shield") continue
            if (!(shardSMP.itemManager.getItem(item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return)?.isActivated() ?: return)) continue

            event.isCancelled = true
        }
    }

}