package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import me.lutto.shardsmp.items.Upgradable
import me.lutto.shardsmp.items.events.ItemUpgradeEvent
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class UpgradeItemCommand(private val shardSMP: ShardSMP) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            Bukkit.getLogger().info("Command must be run by player!")
            return false
        }

        if (!sender.isOp()) {
            sender.sendRichMessage("<red>You need to be an operator to use this command!")
            return false
        }

        val itemInMainHand: ItemStack = sender.inventory.itemInMainHand

        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) {
            sender.sendRichMessage("<red>This item is not a custom item!")
            return false
        }

        val customItem: CustomItem = shardSMP.itemManager.getItem(itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING]!!) ?: return false
        if (customItem !is Upgradable) {
            sender.sendRichMessage("<red>This item is not upgradable!")
            return false
        }

        val uuidKey = NamespacedKey(shardSMP, "uuid")
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) {
            val itemInMainHandMeta = itemInMainHand.itemMeta
            itemInMainHandMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] = UUID.randomUUID().toString()
            itemInMainHand.itemMeta = itemInMainHandMeta
            return false
        }

        val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING])
        if (shardSMP.itemManager.isUpgraded(itemUUID)) {
            shardSMP.itemManager.setUpgraded(itemUUID, false)
            sender.sendRichMessage("<green>This item is not upgraded anymore!")
        } else {
            shardSMP.itemManager.setUpgraded(itemUUID, true)
            Bukkit.getPluginManager().callEvent(ItemUpgradeEvent(itemInMainHand, customItem, itemUUID))
            sender.sendRichMessage("<green>This item has been upgraded!")
        }
        return true

    }

}