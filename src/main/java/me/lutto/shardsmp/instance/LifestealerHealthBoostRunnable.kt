package me.lutto.shardsmp.instance

import me.lutto.shardsmp.ShardSMP
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class LifestealerHealthBoostRunnable(private val shardSMP: ShardSMP) : BukkitRunnable() {

    fun start() {
        runTaskTimer(shardSMP, 0, 20)
    }

    override fun run() {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            for (item in onlinePlayer.inventory) {
                val customItemKey = NamespacedKey(shardSMP, "custom_item")
                val uuidKey = NamespacedKey(shardSMP, "uuid")

                if (item == null) continue
                if (item.itemMeta == null) continue
                if (!item.itemMeta.persistentDataContainer.has(customItemKey)) continue
                if (!item.itemMeta.persistentDataContainer.has(uuidKey)) continue
                if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "lifestealer") continue

                val itemUUID: UUID = UUID.fromString(item.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
                if (!shardSMP.itemManager.isUpgraded(itemUUID)) return

                onlinePlayer.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 21, 1))
            }
        }
    }

}
