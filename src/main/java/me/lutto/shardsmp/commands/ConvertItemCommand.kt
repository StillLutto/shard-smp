package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.OldCustomItems
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

@Suppress("DEPRECATION")
class ConvertItemCommand(private val shardSMP: ShardSMP) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            Bukkit.getLogger().info("A player must run this command!")
            return false
        }

        val itemInMainHand: ItemStack = sender.inventory.itemInMainHand

        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val uuidKey = NamespacedKey(shardSMP, "uuid")

        if (itemInMainHand.itemMeta == null) return false
        if (itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey) || itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) {
            sender.sendRichMessage("<red>This is already a custom item!")
            return false
        }

        for (oldCustomItem: OldCustomItems in OldCustomItems.entries) {
            sender.sendMessage(itemInMainHand.itemMeta.displayName)
            if (itemInMainHand.itemMeta.displayName != oldCustomItem.displayName) continue
            sender.sendMessage("why you a-not a-work")
            sender.sendMessage(oldCustomItem.id)

            val item: ItemStack = (shardSMP.itemManager.getItem(oldCustomItem.id) ?: continue).getItemStack()
            val itemMeta: ItemMeta = item.itemMeta
            itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] = UUID.randomUUID().toString()
            item.itemMeta = itemMeta
            sender.inventory.setItemInMainHand(item)
            return true
        }

        sender.sendRichMessage("<red>Custom Item not found!")
        Bukkit.getLogger().info("Custom Item not found! Item DisplayName: ${itemInMainHand.itemMeta.displayName}")
        return false

    }

}