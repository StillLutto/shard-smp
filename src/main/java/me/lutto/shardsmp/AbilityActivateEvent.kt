package me.lutto.shardsmp

import me.lutto.shardsmp.instance.CustomItem
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class AbilityActivateEvent(private val player: Player, private val customItem: CustomItem) : Event() {

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
    fun getCustomItem(): CustomItem = customItem

}