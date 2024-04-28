//package me.lutto.shardsmp.listeners.weapons
//
//import com.mojang.datafixers.util.Pair
//import io.netty.channel.*
//import me.lutto.shardsmp.ShardSMP
//import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket
//import org.bukkit.Bukkit
//import org.bukkit.Material
//import org.bukkit.NamespacedKey
//import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
//import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack
//import org.bukkit.entity.Player
//import org.bukkit.event.EventHandler
//import org.bukkit.event.Listener
//import org.bukkit.event.player.PlayerJoinEvent
//import org.bukkit.event.player.PlayerQuitEvent
//import org.bukkit.inventory.ItemStack
//import org.bukkit.persistence.PersistentDataType
//
//class EquipmentChangeListener(private val shardSMP: ShardSMP) : Listener {
//
//    @EventHandler
//    fun onPlayerJoin(event: PlayerJoinEvent) {
//        val playerReceiving: Player = event.player
//
//        val channelHandler: ChannelDuplexHandler = object : ChannelDuplexHandler() {
//
//            @Throws(Exception::class)
//            override fun write(ctx: ChannelHandlerContext, rawPacket: Any, promise: ChannelPromise) {
//                if (rawPacket is ClientboundSetEquipmentPacket) {
//                    val packet: ClientboundSetEquipmentPacket = rawPacket
//                    var playerFromPacket: Player? = null
//                    for (onlinePlayer in Bukkit.getServer().onlinePlayers) {
//                        if (onlinePlayer.entityId != packet.entity) continue
//                        playerFromPacket = onlinePlayer
//                    }
//                    if (playerFromPacket == null) return
//
//                    for (item in playerFromPacket.inventory) {
//                        val customItemKey = NamespacedKey(shardSMP, "custom_item")
//                        val uuidKey = NamespacedKey(shardSMP, "uuid")
//
//                        if (item == null) continue
//                        if (item.itemMeta == null) continue
//                        if (!item.itemMeta.persistentDataContainer.has(customItemKey)) continue
//                        if (!item.itemMeta.persistentDataContainer.has(uuidKey)) continue
//                        if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "vanish_blade") continue
//                        if (!(shardSMP.itemManager.getItem(item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return)?.isActivated() ?: return)) continue
//
//                        for (index in packet.slots.indices) {
//                            packet.slots[index] = Pair(packet.slots[index].first, CraftItemStack.asNMSCopy(ItemStack(Material.AIR)))
//                            playerFromPacket.sendMessage(packet.slots[index].toString())
//                        }
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
//
//}