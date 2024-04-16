package me.lutto.shardsmp.listeners

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import me.lutto.shardsmp.ShardSMP
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*
import java.util.concurrent.TimeUnit

class CooldownListener(private val shardSMP: ShardSMP) : Listener {

    private val itemCooldown: MutableMap<String, Cache<UUID, Long>> = mutableMapOf()

    init {
        for (customItem in shardSMP.itemManager.getItemList()) {
            if (shardSMP.itemManager.getCooldown(customItem.getId())?.second?.toLong() == null) continue
            itemCooldown[customItem.getId()] = CacheBuilder.newBuilder().expireAfterWrite(shardSMP.itemManager.getCooldown(customItem.getId())?.second!!.toLong(), TimeUnit.SECONDS).build()
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player

        if (event.hand != EquipmentSlot.HAND) return
        if (!(player.isSneaking)) return

        if (player.inventory.itemInMainHand.isEmpty) return
        val itemInMainHand: ItemStack = player.inventory.itemInMainHand

        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        if (itemInMainHand.itemMeta != null && !itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) return
        val itemId: String = itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return

        if (shardSMP.itemManager.getItem(itemId) == null) return

        if (shardSMP.itemManager.getCooldown(itemId)?.first!!) {
            if (!(event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK)) return
        } else {
            if (!(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) return
        }

        val uuidKey = NamespacedKey(shardSMP, "uuid")
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) {
            val itemInMainHandMeta = itemInMainHand.itemMeta
            itemInMainHandMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] = UUID.randomUUID().toString()
            itemInMainHand.itemMeta = itemInMainHandMeta
        }

        val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING] ?: return)
        if (itemCooldown[itemId] == null) return
        if (itemCooldown[itemId]!!.asMap().containsKey(itemUUID)) {
            val durationLeft: Long = (itemCooldown[itemId]!!.asMap()[itemUUID] ?: return) - System.currentTimeMillis()
            if (TimeUnit.MILLISECONDS.toMinutes(durationLeft) >= 2) {
                player.sendActionBar(
                    MiniMessage.miniMessage().deserialize(
                        "<red>You have to wait ${TimeUnit.MILLISECONDS.toMinutes(durationLeft)} minutes and ${
                            TimeUnit.MILLISECONDS.toSeconds(durationLeft) - TimeUnit.MILLISECONDS.toMinutes(durationLeft) * 60
                        } seconds before using the ability again"
                    )
                )
            } else {
                player.sendActionBar(MiniMessage.miniMessage().deserialize("<red>You have to wait ${TimeUnit.MILLISECONDS.toSeconds(durationLeft)} seconds before using the ability again"))
            }
            return
        }

        player.sendActionBar(Component.text("${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} Activated", NamedTextColor.GREEN))
        itemCooldown[itemId]!!.asMap()[itemUUID] = System.currentTimeMillis() + ((shardSMP.itemManager.getCooldown(itemId) ?: return).second) * 1000
    }

}