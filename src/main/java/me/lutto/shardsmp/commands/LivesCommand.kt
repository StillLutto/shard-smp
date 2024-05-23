package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LivesCommand(private val shardSMP: ShardSMP) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            Bukkit.getLogger().info("Command must be run by player!")
            return false
        }

        if (!sender.isOp()) {
            sender.sendRichMessage("<red>You need to be an operator to use this command!")
            return false
        }

        if (args.isEmpty()) {
            sender.sendRichMessage("<red>You did not provide any arguments. Try again.")
            sender.sendRichMessage("<red>Example: /giveitem <item>")
        }

        if (args.size != 2 && args.size != 3) {
            sender.sendRichMessage("<red>Invalid Usage!")
            sender.sendRichMessage("<red>Please use /life <give/remove/get> <player> <optional: amount>")
            return false
        }

        if (!args[0].equals("give", ignoreCase = true) && !args[0].equals(
                "remove",
                ignoreCase = true
            ) && !args[0].equals("get", ignoreCase = true) && !args[0].equals("set", ignoreCase = true)
        ) {
            sender.sendRichMessage("<red>Method not found!")
            return false
        }

        if (Bukkit.getPlayerExact(args[1]) == null) {
            sender.sendRichMessage("<red>Player not found!")
            return false
        }

        val target = Bukkit.getPlayerExact(args[1]) ?: return false

        if (args[0].equals("get", ignoreCase = true)) {
            sender.sendRichMessage("<green>${target.name} has ${shardSMP.livesManager.getLives(target.uniqueId)} lives.")
            return true
        }

        if (args.size != 3) {
            sender.sendRichMessage("<red>Invalid Usage!")
            sender.sendRichMessage("<red>Please use /life <give/remove/get> <player> <amount>")
            return true
        }

        if (!isNumeric(args[2])) {
            sender.sendRichMessage("<red>Please enter a valid number!")
            return true
        }

        if (args[0].equals("give", ignoreCase = true)) {
            if (shardSMP.livesManager.addLives(target.uniqueId, args[2].toInt())) {
                shardSMP.livesManager.updateListName(target)
                sender.sendRichMessage("<green>Given ${args[2]} lives to ${target.name} successfully.")
                sender.sendRichMessage("<green>${target.name} now has ${shardSMP.livesManager.getLives(target.uniqueId)}")
            } else {
                sender.sendRichMessage("<red>Please try again. Player lives must be between 1 and 5.")
                sender.sendRichMessage("<red>${target.name} has ${shardSMP.livesManager.getLives(target.uniqueId)}")
            }

            return true
        }

        if (args[0].equals("remove", ignoreCase = true)) {
            shardSMP.livesManager.removeLives(target.uniqueId, args[2].toInt())
            shardSMP.livesManager.updateListName(target)
            sender.sendRichMessage("<green>Removed ${args[2]} lives from ${target.name} successfully.")
            sender.sendRichMessage("<green>${target.name} now has ${shardSMP.livesManager.getLives(target.uniqueId)}")
            return true
        }

        if (args[0].equals("set", ignoreCase = true)) {
            if (shardSMP.livesManager.setLives(target.uniqueId, args[2].toInt())) {
                sender.sendRichMessage("<green>Set ${target.name} to ${args[2]} lives.")
                shardSMP.livesManager.updateListName(target)
            } else {
                sender.sendRichMessage("<red>Something went wrong. Please try again.")
            }
            return true
        }

        sender.sendRichMessage("<red>Something went wrong, please try again.")
        return true
    }

    private fun isNumeric(string: String): Boolean {
        try {
            string.toInt()
            return true
        } catch (e: NumberFormatException) {
            return false
        }
    }
}