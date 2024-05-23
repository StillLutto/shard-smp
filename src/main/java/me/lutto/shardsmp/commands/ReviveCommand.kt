package me.lutto.shardsmp.commands

import me.lutto.shardsmp.ShardSMP
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.ban.ProfileBanList
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class ReviveCommand(private val shardSMP: ShardSMP) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            Bukkit.getLogger().info("Command must be run by player!")
            return false
        }

        if (!sender.isOp()) {
            sender.sendRichMessage("<red>You need to be an operator to use this command!")
            return false
        }

        if (args.size != 2) {
            sender.sendRichMessage("<red>Invalid Usage!")
            sender.sendRichMessage("<red>Please use /revive <player> <lives>")
            return false
        }

        if (!Bukkit.getOfflinePlayer(args[0]).isWhitelisted) {
            sender.sendRichMessage("<red>Player is not whitelisted!")
            return false
        }

        if (Bukkit.getOfflinePlayer(args[0]).uniqueId === sender.uniqueId) {
            sender.sendRichMessage("<red>You cannot revive yourself!")
            return false
        }

        if (!isNumeric(args[1])) {
            sender.sendRichMessage("<red>Please enter a valid number!")
            return false
        }

        val target = Bukkit.getOfflinePlayer(args[0])

        if (!target.isBanned) {
            sender.sendRichMessage("<red>That player is not banned!")
            return false
        }

        if (shardSMP.livesManager.setLives(target.uniqueId, args[1].toInt())) {
            sender.sendRichMessage("<green>${target.name} has been revived!")
            val profileBanList: ProfileBanList = Bukkit.getBanList(BanList.Type.PROFILE)
            profileBanList.pardon(Bukkit.createProfile(target.uniqueId))
        } else {
            sender.sendRichMessage("<red>Please try again. Player lives must be between 1 and 5.")
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