package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.items.events.AbilityActivateEvent
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffectType

class PyroSword(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "pyro_sword",
    Material.DIAMOND_SWORD,
    MiniMessage.miniMessage().deserialize("<gradient:#ff8b0f:#ffd60a>Pyro Sword")
        .decoration(TextDecoration.ITALIC, false),
    listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
    4,
    true,
    240,
    false
), Listener {

    init {
        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        if (event.entity !is Player) return

        val itemInMainHand = (event.damager as Player).inventory.itemInMainHand
        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val uuidKey = NamespacedKey(shardSMP, "uuid")

        if (itemInMainHand.isEmpty) return
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) return
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) return
        if (itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "pyro_sword") return
        if (!(shardSMP.itemManager.getCooldownItem(itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return)?.isActivated() ?: return)) return

        val damagee = event.entity as Player
        if (damagee.isDead) return

        damagee.removePotionEffect(PotionEffectType.FIRE_RESISTANCE)
        damagee.sendActionBar(Component.text("Fire Removed!", NamedTextColor.RED))

        Bukkit.getPluginManager().callEvent(AbilityActivateEvent(event.damager as Player,shardSMP.itemManager.getCooldownItem("pyro_sword") ?: return))
    }

}
