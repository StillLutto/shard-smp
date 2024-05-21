package me.lutto.shardsmp.items.events

import me.lutto.shardsmp.items.CustomItem
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack
import java.util.UUID

class ItemUpgradeEvent(private val player: Player, private val item: ItemStack, private val customItem: CustomItem, private val itemUUID: UUID) : Event() {

    companion object {
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    fun getPlayer(): Player = player
    fun getItem(): ItemStack = item
    fun getCustomItem(): CustomItem = customItem
    fun getItemUUID(): UUID = itemUUID

}