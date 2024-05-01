package me.lutto.shardsmp.items.events

import me.lutto.shardsmp.items.CustomCooldownItem
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.UUID

class AbilityActivateEvent(private val player: Player, private val customItem: CustomCooldownItem, private val itemUUID: UUID) : Event() {

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
    fun getItem(): CustomCooldownItem = customItem
    fun getItemUUID(): UUID = itemUUID

}