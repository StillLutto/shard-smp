package me.lutto.shardsmp.items.weapons

import com.mojang.datafixers.util.Pair
import io.netty.channel.*
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.Upgradable
import me.lutto.shardsmp.items.events.AbilityActivateEvent
import me.lutto.shardsmp.manager.getUUID
import me.lutto.shardsmp.manager.isCustomItem
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket
import org.bukkit.*
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import java.util.*

class VanishBlade(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "vanish_blade",
    Material.DIAMOND_SWORD,
    shardSMP.miniMessage.deserialize("<gradient:#919191:#c2c2c2>ᴠᴀɴɪѕʜ ʙʟᴀᴅᴇ")
        .decoration(TextDecoration.ITALIC, false),
    listOf(shardSMP.miniMessage.deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
    10,
    true,
    150,
    true
), Upgradable, Listener {

    override fun getUpgradedCooldownTime(): Int = 90
    override fun getUpgradedCustomModelData(): Int = 14

    init {
        val invisibilityPotion = ItemStack(Material.SPLASH_POTION)
        val invisibilityPotionItemMeta = invisibilityPotion.itemMeta as PotionMeta
        invisibilityPotionItemMeta.basePotionData = PotionData(PotionType.INVISIBILITY, true, false)
        invisibilityPotion.setItemMeta(invisibilityPotionItemMeta)

        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "SNS",
            "IDI",
            "SBS"
        )
        recipe.setIngredient('I', RecipeChoice.ExactChoice(invisibilityPotion))
        recipe.setIngredient('S', RecipeChoice.ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('B', Material.DIAMOND_BLOCK)
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        recipe.setIngredient('D', Material.DIAMOND_SWORD)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    private fun changeEquipment(player: Player, toOwnItems: Boolean) {
        if (toOwnItems) {
            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.uniqueId === player.uniqueId) continue
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.OFF_HAND, player.inventory.itemInOffHand)
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HAND, player.inventory.itemInMainHand)
                if (player.inventory.helmet != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HEAD, player.inventory.helmet)
                if (player.inventory.chestplate != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.CHEST, player.inventory.chestplate)
                if (player.inventory.leggings != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.LEGS, player.inventory.leggings)
                if (player.inventory.boots != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.FEET, player.inventory.boots)
            }
        } else {
            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.uniqueId === player.uniqueId) continue
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HAND, ItemStack(Material.AIR))
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.OFF_HAND, ItemStack(Material.AIR))
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HEAD, ItemStack(Material.AIR))
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.CHEST, ItemStack(Material.AIR))
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.LEGS, ItemStack(Material.AIR))
                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.FEET, ItemStack(Material.AIR))
            }
        }
    }

    @EventHandler
    fun onAbilityActivate(event: AbilityActivateEvent) {
        if (event.getItem() != shardSMP.itemManager.getCooldownItem("vanish_blade")) return
        val player: Player = event.getPlayer()

        player.isInvisible = true
        player.arrowsInBody = 0
        changeEquipment(player, false)

        var abilityDuration: Long = 300
        if (shardSMP.itemManager.isUpgraded(event.getItemUUID())) {
            abilityDuration = 500
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(shardSMP, Runnable {
            player.sendRichMessage("<green>You are now visible to all players")
            shardSMP.itemManager.setIsActivated(event.getItemUUID(), false)
            player.isInvisible = false
            changeEquipment(player, true)
        }, abilityDuration)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player

        val channelHandler: ChannelDuplexHandler = object : ChannelDuplexHandler() {

            @Throws(Exception::class)
            override fun write(ctx: ChannelHandlerContext, rawPacket: Any, promise: ChannelPromise) {
                if (rawPacket is ClientboundSetEquipmentPacket) {
                    val packet: ClientboundSetEquipmentPacket = rawPacket
                    var playerFromPacket: Player? = null
                    for (onlinePlayer in Bukkit.getServer().onlinePlayers) {
                        if (onlinePlayer.entityId != packet.entity) continue
                        playerFromPacket = onlinePlayer
                    }
                    if (playerFromPacket == null) return

                    for (item in playerFromPacket.inventory) {
                        if (item == null || !item.isCustomItem("vanish_blade")) continue
                        val itemUUID: UUID = item.getUUID() ?: return
                        if (!shardSMP.itemManager.isActivated(itemUUID)) continue

                        for (index in packet.slots.indices) {
                            packet.slots[index] = Pair(packet.slots[index].first, CraftItemStack.asNMSCopy(ItemStack(Material.AIR)))
                        }
                        break
                    }
                }

                super.write(ctx, rawPacket, promise)
            }
        }

        val pipeline: ChannelPipeline = (player as CraftPlayer).handle.connection.connection.channel.pipeline()
        pipeline.addBefore("packet_handler", player.name, channelHandler)

        Bukkit.getScheduler().runTaskLater(shardSMP, Runnable {
            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.uniqueId === player.uniqueId) continue
                for (item in onlinePlayer.inventory) {
                    val customItemKey = NamespacedKey(shardSMP, "custom_item")
                    val uuidKey = NamespacedKey(shardSMP, "uuid")

                    if (item == null) continue
                    if (item.itemMeta == null) continue
                    if (!item.itemMeta.persistentDataContainer.has(customItemKey)) continue
                    if (!item.itemMeta.persistentDataContainer.has(uuidKey)) continue
                    if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "vanish_blade") continue
                    val itemUUID: UUID = UUID.fromString(item.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
                    if (!shardSMP.itemManager.isActivated(itemUUID)) continue

                    player.sendEquipmentChange(onlinePlayer, EquipmentSlot.HAND, ItemStack(Material.AIR))
                    player.sendEquipmentChange(onlinePlayer, EquipmentSlot.OFF_HAND, ItemStack(Material.AIR))
                    player.sendEquipmentChange(onlinePlayer, EquipmentSlot.HEAD, ItemStack(Material.AIR))
                    player.sendEquipmentChange(onlinePlayer, EquipmentSlot.CHEST, ItemStack(Material.AIR))
                    player.sendEquipmentChange(onlinePlayer, EquipmentSlot.LEGS, ItemStack(Material.AIR))
                    player.sendEquipmentChange(onlinePlayer, EquipmentSlot.FEET, ItemStack(Material.AIR))
                    break
                }
            }
        }, 1)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player
        val channel: Channel = (player as CraftPlayer).handle.connection.connection.channel
        channel.eventLoop().submit {
            channel.pipeline().remove(player.name)
            return@submit
        }

        player.isInvisible = false
        changeEquipment(player, true)
    }

}
