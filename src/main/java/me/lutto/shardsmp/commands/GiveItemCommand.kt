package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import me.lutto.shardsmp.items.Upgradable
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

class GiveItemCommand(private val shardSMP: ShardSMP) : CommandExecutor {

    private fun giveItem(player: Player, customItem: CustomItem) {
        val item: ItemStack = customItem.getItemStack()
        if (customItem is Upgradable) {
            val itemMeta: ItemMeta = item.itemMeta
            itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "uuid"), PersistentDataType.STRING] = UUID.randomUUID().toString()
            item.itemMeta = itemMeta
        }
        player.inventory.addItem(
            item
        )
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender is Player) {

            if (args.isEmpty()) {
                sender.sendMessage("You did not provide any arguments. Try again.")
                sender.sendMessage("Example: /giveitem <item>")
            } else {
                if (!sender.isOp()) {
                    sender.sendRichMessage("<red>You need to be an operator to use this command!")
                    return false
                }

                if (shardSMP.itemManager.getItem(args[0]) != null) {
                    giveItem(sender, shardSMP.itemManager.getItem(args[0])!!)
                } else if (args[0] == "all") {
                    for (customItem in shardSMP.itemManager.getItemList()) {
                        giveItem(sender, shardSMP.itemManager.getItem(customItem.getId())!!)
                    }
                } else {
                    sender.sendRichMessage("<red>Item does not exist!")
                    return false
                }

                sender.sendRichMessage("<gold>You have been given ${args[0]}!")
                return true
            }
        } else {
            Bukkit.getLogger().info("Command must be run by player!")
            return false
        }

        return false

    }

}