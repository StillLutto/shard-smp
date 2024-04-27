package me.lutto.shardsmp.listeners.weapons

import me.lutto.shardsmp.AbilityActivateEvent
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.instance.CustomItem
import org.bukkit.*
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.persistence.PersistentDataType
import java.util.*

class EnderBowListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        if (event.projectile !is Arrow) return

        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val bow = event.bow ?: return
        if (bow.itemMeta?.persistentDataContainer?.get(customItemKey, PersistentDataType.STRING) != "ender_bow") return

        val customItem: CustomItem = shardSMP.itemManager.getItem("ender_bow") ?: return
        val itemUUID: UUID = UUID.fromString(bow.itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] ?: return)
        if (!customItem.isActivated()) return
        if (shardSMP.itemManager.getItemCooldown()["ender_bow"]!!.asMap().containsKey(itemUUID)) return

        val projectile = event.projectile
        projectile.setMetadata("ender_bow_arrow", FixedMetadataValue(shardSMP, true))
        Bukkit.getPluginManager().callEvent(AbilityActivateEvent((event.entity as Player), customItem))
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (!event.entity.hasMetadata("ender_bow_arrow")) return
        if (event.entity.shooter !is Player) return

        val player: Player = event.entity.shooter as Player
        val hitEntity = event.hitEntity

        if (hitEntity != null) {
            val playerLocation = Location(player.world, player.location.x, player.location.y, player.location.z, hitEntity.yaw, hitEntity.pitch)
            hitEntity.teleportAsync(playerLocation)
            val hitEntityLocation = Location(hitEntity.world, hitEntity.location.x, hitEntity.location.y, hitEntity.location.z, player.yaw, player.pitch)
            player.teleportAsync(hitEntityLocation)
        } else {
            val playerLocation = Location(player.world, event.entity.location.x, event.entity.location.y, event.entity.location.z, player.location.yaw, player.location.pitch)
            player.teleportAsync(playerLocation)
        }

        event.entity.removeMetadata("ender_bow_arrow", shardSMP)
        event.entity.remove()
        player.sendRichMessage("<green>You have been teleported!")
        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1.0f, 1.0f)
        player.spawnParticle(Particle.PORTAL, player.location, 100)
    }

}