package me.lutto.shardsmp.listeners

import com.google.common.cache.CacheBuilder
import me.lutto.shardsmp.AbilityActivateEvent
import me.lutto.shardsmp.AbilityDeactivateEvent
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.instance.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
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

    init {
        for (customItem in shardSMP.itemManager.getItemList()) {
            shardSMP.itemManager.setItemCooldown(customItem.getId(), CacheBuilder.newBuilder().expireAfterWrite((customItem.getCooldownTime() ?: continue), TimeUnit.SECONDS).build())
        }
    }

    private fun getCustomItem(item: ItemStack): CustomItem? {
        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        if (item.itemMeta != null && !item.itemMeta.persistentDataContainer.has(customItemKey)) return null
        val itemId: String = item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return null
        return shardSMP.itemManager.getItem(itemId)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player: Player = event.player

        if (event.hand != EquipmentSlot.HAND) return
        if (!(player.isSneaking)) return

        if (player.inventory.itemInMainHand.isEmpty) return
        val itemInMainHand: ItemStack = player.inventory.itemInMainHand

        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        if (itemInMainHand.itemMeta != null && !itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) return
        val itemId: String = itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return
        val customItem: CustomItem = shardSMP.itemManager.getItem(itemId) ?: return

        if (customItem.isRightClick() != null && customItem.isRightClick()!!) {
            if (!(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) return
        } else {
            if (!(event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK)) return
        }

        val uuidKey = NamespacedKey(shardSMP, "uuid")
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) {
            val itemInMainHandMeta = itemInMainHand.itemMeta
            itemInMainHandMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] = UUID.randomUUID().toString()
            itemInMainHand.itemMeta = itemInMainHandMeta
        }

        val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING] ?: return)
        val currentItemCooldown = shardSMP.itemManager.getItemCooldown()[itemId] ?: return
        if (currentItemCooldown.asMap().containsKey(itemUUID)) {
            val durationLeft: Long = (currentItemCooldown.asMap()[itemUUID] ?: return) - System.currentTimeMillis()
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

        if (!(customItem.isUsedOnActivation() ?: return) && customItem.isActivated()) {
            Bukkit.getPluginManager().callEvent(AbilityDeactivateEvent(player, customItem))
            player.sendActionBar(MiniMessage.miniMessage().deserialize("<red>${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} Deactivated"))
            return Bukkit.getPluginManager().callEvent(AbilityDeactivateEvent(player, customItem))
        } else if(!(customItem.isUsedOnActivation() ?: return)) {
            player.sendActionBar(Component.text("${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} Activated", NamedTextColor.GREEN))
            return customItem.setIsActivated(true)
        }

        Bukkit.getPluginManager().callEvent(AbilityActivateEvent(player, customItem))
    }

    @EventHandler
    fun onAbilityActivate(event: AbilityActivateEvent) {
        val player: Player = event.getPlayer()
        val itemInMainHand = player.inventory.itemInMainHand

        val uuidKey = NamespacedKey(shardSMP, "uuid")
        val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING] ?: return)
        val customItem: CustomItem = event.getCustomItem()

        player.sendActionBar(Component.text("${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} Activated", NamedTextColor.GREEN))
        (shardSMP.itemManager.getItemCooldown()[customItem.getId()] ?: return).asMap()[itemUUID] = System.currentTimeMillis() + (customItem.getCooldownTime() ?: return) * 1000
        customItem.setIsActivated(false)
    }

    @EventHandler
    fun onAbilityDeactivate(event: AbilityDeactivateEvent) {
        val player: Player = event.getPlayer()
        val itemInMainHand = player.inventory.itemInMainHand

        val customItem: CustomItem = event.getCustomItem()

        player.sendActionBar(Component.text("${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} Deactivated", NamedTextColor.GREEN))
        customItem.setIsActivated(false)
    }

}