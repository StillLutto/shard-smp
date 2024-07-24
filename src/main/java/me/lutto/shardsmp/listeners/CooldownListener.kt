package me.lutto.shardsmp.listeners

import com.google.common.cache.CacheBuilder
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.Upgradable
import me.lutto.shardsmp.items.events.AbilityActivateEvent
import me.lutto.shardsmp.items.events.AbilityDeactivateEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.TimeUnit

class CooldownListener(private val shardSMP: ShardSMP) : Listener {

    private var activationMessage: Long = -1

    init {
        for (customItem in shardSMP.itemManager.getItemList()) {
            if (customItem !is CustomCooldownItem) continue
            shardSMP.itemManager.setItemCooldown(customItem.getId(), CacheBuilder.newBuilder().expireAfterWrite(customItem.getCooldownTime().toLong(), TimeUnit.SECONDS).build())
        }
    }

    private fun getStringCooldown(durationLeft: Long): String {
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(durationLeft)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(durationLeft) - (minutes * 60)
        if (minutes == 0L) return "${seconds}s"
        return "${minutes}m ${seconds}s"
    }

    private fun displayHeldItemCooldown(player: Player, slot: Int) {
        val item = player.inventory.getItem(slot) ?: return

        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline) return this.cancel()
                if (player.inventory.heldItemSlot != slot) return this.cancel()
                if (item != player.inventory.getItem(player.inventory.heldItemSlot)) return this.cancel()

                val customItemKey = NamespacedKey(shardSMP, "custom_item")
                val uuidKey = NamespacedKey(shardSMP, "uuid")
                val itemUUID: UUID = UUID.fromString(item.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING] ?: return this.cancel())
                val itemID: String = item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return this.cancel()

                checkCooldown(player, itemID, itemUUID)
            }
        }.runTaskTimer(shardSMP, 0, 20)
    }

    private fun checkCooldown(player: Player, itemId: String, itemUUID: UUID): Boolean { // false = full stop, true = continue
        val itemCooldown = shardSMP.itemManager.getItemCooldown()[itemId] ?: return false
        val customItem: CustomCooldownItem = shardSMP.itemManager.getCooldownItem(itemId) ?: return false

        if (!itemCooldown.asMap().containsKey(itemUUID)) return true

        var durationLeft: Long = (itemCooldown.asMap()[itemUUID] ?: return false) - System.currentTimeMillis()
        if (customItem is Upgradable) run {
            if (!shardSMP.itemManager.isUpgraded(itemUUID)) return@run
            val timePassed = TimeUnit.SECONDS.toMillis(customItem.getCooldownTime().toLong()) - durationLeft
            val upgradedCooldown = TimeUnit.SECONDS.toMillis((customItem as Upgradable).getUpgradedCooldownTime().toLong())

            durationLeft = upgradedCooldown - timePassed
        }

        if (durationLeft <= 0L) {
            shardSMP.itemManager.resetItemCooldown(itemId, itemUUID)
            return true
        }

        if (activationMessage + 3000 > System.currentTimeMillis()) return false
        player.sendActionBar(shardSMP.miniMessage.deserialize("日 | <aqua>${getStringCooldown(durationLeft)}"))

        return false
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
        val customItem: CustomCooldownItem = shardSMP.itemManager.getCooldownItem(itemId) ?: return

        if (customItem.isRightClick()) {
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
        if (!checkCooldown(player, itemId, itemUUID)) return

        if (!customItem.isUsedOnActivation() && shardSMP.itemManager.isActivated(itemUUID)) {
            player.sendActionBar(shardSMP.miniMessage.deserialize("<red>${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} ᴅᴇᴀᴄᴛɪᴠᴀᴛᴇᴅ"))
            return Bukkit.getPluginManager().callEvent(AbilityDeactivateEvent(player, customItem))
        } else if (!customItem.isUsedOnActivation()) {
            player.sendActionBar(Component.text("${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} ᴀᴄᴛɪᴠᴀᴛᴇᴅ", NamedTextColor.GREEN))
            activationMessage = System.currentTimeMillis()
            return shardSMP.itemManager.setIsActivated(itemUUID, true)
        }

        Bukkit.getPluginManager().callEvent(AbilityActivateEvent(player, customItem, itemUUID))
    }

    @EventHandler
    fun onAbilityActivate(event: AbilityActivateEvent) {
        val player: Player = event.getPlayer()
        val itemInMainHand = player.inventory.itemInMainHand

        val uuidKey = NamespacedKey(shardSMP, "uuid")
        val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING] ?: return)
        val customItem: CustomCooldownItem = event.getItem()

        player.sendActionBar(Component.text("${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} ᴀᴄᴛɪᴠᴀᴛᴇᴅ", NamedTextColor.GREEN))
        (shardSMP.itemManager.getItemCooldown()[customItem.getId()] ?: return).asMap()[itemUUID] = System.currentTimeMillis() + (customItem.getCooldownTime()) * 1000
        shardSMP.itemManager.setIsActivated(itemUUID, true)
        activationMessage = System.currentTimeMillis()
    }

    @EventHandler
    fun onAbilityDeactivate(event: AbilityDeactivateEvent) {
        val player: Player = event.getPlayer()
        val itemInMainHand = player.inventory.itemInMainHand

        val uuidKey = NamespacedKey(shardSMP, "uuid")
        val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING] ?: return)

        player.sendActionBar(Component.text("${PlainTextComponentSerializer.plainText().serialize(itemInMainHand.displayName()).trim('[', ']')} ᴅᴇᴀᴄᴛɪᴠᴀᴛᴇᴅ", NamedTextColor.RED))
        shardSMP.itemManager.setIsActivated(itemUUID, false)
        activationMessage = System.currentTimeMillis()
    }

    @EventHandler
    fun onPlayerSlotChange(event: PlayerInventorySlotChangeEvent) {
        val player: Player = event.player
        if (player.inventory.heldItemSlot != event.slot) return
        displayHeldItemCooldown(player, player.inventory.heldItemSlot)
    }

    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        displayHeldItemCooldown(event.player, event.newSlot)
    }

}