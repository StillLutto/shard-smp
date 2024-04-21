package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.instance.CustomItem
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class ResetCooldownCommand(private val shardSMP: ShardSMP) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender is Player) {


            if (!sender.isOp()) {
                sender.sendRichMessage("<red>You need to be an operator to use this command!")
                return false
            }

            val itemInMainHand: ItemStack = sender.inventory.itemInMainHand
            if (itemInMainHand.isEmpty) {
                sender.sendRichMessage("<red>You have nothing in your hand!")
                return false
            }
            val customItemKey = NamespacedKey(shardSMP, "custom_item")
            if (itemInMainHand.itemMeta != null && !itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) {
                sender.sendRichMessage("<red>This is not a custom item!")
                return false
            }
            val uuidKey = NamespacedKey(shardSMP, "uuid")
            if (!itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) {
                sender.sendRichMessage("<red>This item is not on cooldown!")
                return false
            }
            val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
            val itemId: String = itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return false

            shardSMP.itemManager.resetItemCooldown(itemId, itemUUID)
            sender.sendRichMessage("<green>Cooldown has been reset!")

            return true
        }

        return false
    }

}