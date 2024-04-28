//package me.lutto.shardsmp.listeners.weapons
//
//import me.lutto.shardsmp.items.events.AbilityActivateEvent
//import me.lutto.shardsmp.ShardSMP
//import net.kyori.adventure.text.Component
//import net.kyori.adventure.text.format.NamedTextColor
//import org.bukkit.Bukkit
//import org.bukkit.NamespacedKey
//import org.bukkit.entity.Player
//import org.bukkit.event.EventHandler
//import org.bukkit.event.Listener
//import org.bukkit.event.entity.EntityDamageByEntityEvent
//import org.bukkit.persistence.PersistentDataType
//import org.bukkit.potion.PotionEffectType
//
//
//class PyroSwordListener(private val shardSMP: ShardSMP) : Listener {
//
//    @EventHandler
//    fun onHit(event: EntityDamageByEntityEvent) {
//        if (event.damager !is Player) return
//        if (event.entity !is Player) return
//
//        val itemInMainHand = (event.damager as Player).inventory.itemInMainHand
//        val customItemKey = NamespacedKey(shardSMP, "custom_item")
//        val uuidKey = NamespacedKey(shardSMP, "uuid")
//
//        if (itemInMainHand.isEmpty) return
//        if (!itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) return
//        if (!itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) return
//        if (itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "pyro_sword") return
//        if (!(shardSMP.itemManager.getItem(itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return)?.isActivated() ?: return)) return
//
//        val damagee = event.entity as Player
//        if (damagee.isDead) return
//
//        damagee.removePotionEffect(PotionEffectType.FIRE_RESISTANCE)
//        damagee.sendActionBar(Component.text("Fire Removed!", NamedTextColor.RED))
//
//        Bukkit.getPluginManager().callEvent(AbilityActivateEvent(event.damager as Player,shardSMP.itemManager.getItem("pyro_sword") ?: return))
//    }
//
//}