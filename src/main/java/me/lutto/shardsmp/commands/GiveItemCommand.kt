package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveItemCommand(private val shardSMP: ShardSMP) : CommandExecutor {

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
                    sender.inventory.addItem(
                        shardSMP.itemManager.getItem(args[0])!!.getItemStack()
                    )
                } else if (args[0] == "all") {
                    for (customItem in shardSMP.itemManager.getItemList()) {
                        sender.inventory.addItem(
                            shardSMP.itemManager.getItem(customItem.getId())!!.getItemStack()
                        )
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