package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DonateCommand(private val shardSMP: ShardSMP) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            Bukkit.getLogger().info("Command must be run by player!")
            return false
        }

        if (args.size != 2) {
            sender.sendRichMessage("<red>Invalid Usage!")
            sender.sendRichMessage("<red>Please use /donate <player> <amount>")
            return false
        }

        if (Bukkit.getPlayerExact(args[0]) == null) {
            sender.sendRichMessage("<red>Player not found!")
            return false
        }

        if (Bukkit.getPlayerExact(args[0])!!.uniqueId === sender.uniqueId) {
            sender.sendRichMessage("<red>You cannot donate to yourself!")
            return false
        }

        if (!isNumeric(args[1])) {
            sender.sendRichMessage("<red>Please enter a valid number!")
            return false
        }

        val target = Bukkit.getPlayerExact(args[0])

        if (shardSMP.livesManager.addLives(target!!.uniqueId, args[1].toInt())) {
            shardSMP.livesManager.removeLives(sender.uniqueId, args[1].toInt())

            sender.sendRichMessage("<green>Given ${args[1]} lives to ${target.name} successfully.")
            sender.sendRichMessage("<green>${target.name} now has ${shardSMP.livesManager.getLives(target.uniqueId)}")

            shardSMP.livesManager.updateListName(target)
        } else {
            sender.sendRichMessage("<red>Please try again. Player lives must be between 1 and 5.")
            sender.sendRichMessage("<red>${target.name} has ${shardSMP.livesManager.getLives(target.uniqueId)}")
        }

        return true
    }

    private fun isNumeric(str: String): Boolean {
        try {
            str.toInt()
            return true
        } catch (e: NumberFormatException) {
            return false
        }
    }

}