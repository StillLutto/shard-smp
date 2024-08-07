package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.extension.wrap
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.Upgradable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.persistence.PersistentDataType
import java.util.*

class EnderBow(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "ender_bow",
    Material.BOW,
    shardSMP.miniMessage.deserialize("<gradient:#8e1daa:#4b14aa>ᴇɴᴅᴇʀ ʙᴏᴡ").decoration(TextDecoration.ITALIC, false)
        .decoration(TextDecoration.ITALIC, false),
    "ᴜѕᴇ ᴛᴏ ѕʜᴏᴏᴛ ᴀɴ ᴀʀʀᴏᴡ ᴛʜᴀᴛ ᴛᴇʟᴇᴘᴏʀᴛѕ ʏᴏᴜ ᴡʜᴇʀᴇ ɪᴛ ʟᴀɴᴅѕ".wrap(30).map {
        shardSMP.miniMessage.deserialize("<gray>$it").decoration(TextDecoration.ITALIC, false)
    },
    3,
    false,
    30,
    false
), Upgradable, Listener {

    override fun getUpgradedCooldownTime(): Int = 15
    override fun getUpgradedCustomModelData(): Int = 3

    init {
        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "PSP",
            "PBP",
            "NAN"
        )
        recipe.setIngredient('P', Material.ENDER_PEARL)
        recipe.setIngredient('S', ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('B', Material.BOW)
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        recipe.setIngredient('A', Material.ARROW)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        if (event.projectile !is Arrow) return

        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val bow = event.bow ?: return
        if (bow.itemMeta?.persistentDataContainer?.get(customItemKey, PersistentDataType.STRING) != "ender_bow") return

        val customItem: CustomCooldownItem = shardSMP.itemManager.getCooldownItem("ender_bow") ?: return
        val itemUUID: UUID = UUID.fromString(bow.itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] ?: return)
        if (!shardSMP.itemManager.isActivated(itemUUID)) return
        if (shardSMP.itemManager.getItemCooldown()["ender_bow"]!!.asMap().containsKey(itemUUID)) return

        val projectile = event.projectile
        projectile.setMetadata("ender_bow_arrow", FixedMetadataValue(shardSMP, true))

        (shardSMP.itemManager.getItemCooldown()[customItem.getId()] ?: return).asMap()[itemUUID] = System.currentTimeMillis() + (customItem.getCooldownTime()) * 1000
        shardSMP.itemManager.setIsActivated(itemUUID, false)
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (!event.entity.hasMetadata("ender_bow_arrow")) return
        if (event.entity.shooter !is Player) return

        val player: Player = event.entity.shooter as Player
        val hitEntity = event.hitEntity
        val projectile: Projectile = event.entity

        if (hitEntity != null) {
            val playerLocation = Location(player.world, player.location.x, player.location.y, player.location.z, hitEntity.yaw, hitEntity.pitch)
            hitEntity.teleportAsync(playerLocation)
            val hitEntityLocation = Location(hitEntity.world, hitEntity.location.x, hitEntity.location.y, hitEntity.location.z, player.yaw, player.pitch)
            player.teleportAsync(hitEntityLocation)
        } else {
            val playerLocation = Location(projectile.world, projectile.location.x, projectile.location.y, projectile.location.z, player.location.yaw, player.location.pitch)
            player.teleportAsync(playerLocation)
        }

        projectile.removeMetadata("ender_bow_arrow", shardSMP)
        projectile.remove()
        player.sendRichMessage("<green>You have been teleported!")
        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1.0f, 1.0f)
        player.spawnParticle(Particle.PORTAL, player.location, 100)
    }

}
