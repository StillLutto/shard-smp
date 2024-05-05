package me.lutto.shardsmp.listeners

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import me.lutto.shardsmp.ShardSMP
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import java.util.*

class ItemEffectListener(private val shardSMP: ShardSMP) : Listener {

    fun checkItemEffect(item: ItemStack): PotionEffect? {
        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val uuidKey = NamespacedKey(shardSMP, "uuid")

        if (item.itemMeta == null) return null
        if (!item.itemMeta.persistentDataContainer.has(customItemKey)) return null
        if (!item.itemMeta.persistentDataContainer.has(uuidKey)) return null
        for ((itemId, effect) in shardSMP.itemEffectManager.getItemEffects()) {
            if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != itemId) continue
            val itemUUID: UUID =
                UUID.fromString(item.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
            if (shardSMP.itemEffectManager.isUpgradedItem(itemId) && !shardSMP.itemManager.isUpgraded(itemUUID)) continue

            return effect
        }

        return null
    }

    @EventHandler
    fun onPlayerInventorySlotChange(event: PlayerInventorySlotChangeEvent) {
        if (checkItemEffect(event.newItemStack) != null) {
            event.player.addPotionEffect(checkItemEffect(event.newItemStack)!!)
        } else if (checkItemEffect(event.oldItemStack) != null) {
            event.player.removePotionEffect(checkItemEffect(event.oldItemStack)!!.type)
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        for (item in event.player.inventory) {
            if (item == null) continue
            if (checkItemEffect(item) == null) continue
            event.player.addPotionEffect(checkItemEffect(item)!!)
        }
    }

//    @EventHandler
//    fun onPlayerJoin(event: PlayerJoinEvent) {
//        val playerReceiving: Player = event.player
//        event.player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0))
//
//        val channelHandler: ChannelDuplexHandler = object : ChannelDuplexHandler() {
//
//            @Throws(Exception::class)
//            override fun write(ctx: ChannelHandlerContext, rawPacket: Any, promise: ChannelPromise) {
//                if (rawPacket is ClientboundContainerSetSlotPacket) {
//                    val packet: ClientboundContainerSetSlotPacket = rawPacket
//                    playerReceiving.sendMessage("1")
//
//                    val item: ItemStack = CraftItemStack.asBukkitCopy(packet.item)
//                    val customItemKey = NamespacedKey(shardSMP, "custom_item")
//                    val uuidKey = NamespacedKey(shardSMP, "uuid")
//
//                    if (item.itemMeta == null) return
//                    playerReceiving.sendMessage("2")
//                    if (!item.itemMeta.persistentDataContainer.has(customItemKey)) return
//                    playerReceiving.sendMessage("3")
//                    if (!item.itemMeta.persistentDataContainer.has(uuidKey)) return
//                    playerReceiving.sendMessage("4")
//                    for ((itemId, effect) in shardSMP.itemEffectManager.getItemEffects()) {
//                        if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != itemId) continue
//                        val itemUUID: UUID = UUID.fromString(item.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
//                        if (shardSMP.itemEffectManager.isUpgradedItem(itemId) && !shardSMP.itemManager.isUpgraded(itemUUID)) continue
//
//                        playerReceiving.sendMessage("5")
//                        break
//                    }
//                }
//
//                super.write(ctx, rawPacket, promise)
//            }
//        }
//
//        val pipeline: ChannelPipeline = (playerReceiving as CraftPlayer).handle.connection.connection.channel.pipeline()
//        pipeline.addBefore("packet_handler", playerReceiving.name, channelHandler)
//    }
//
//    @EventHandler
//    fun onPlayerQuit(event: PlayerQuitEvent) {
//        val player: Player = event.player
//        val channel: Channel = (player as CraftPlayer).handle.connection.connection.channel
//        channel.eventLoop().submit {
//            channel.pipeline().remove(player.name)
//            return@submit
//        }
//    }

}