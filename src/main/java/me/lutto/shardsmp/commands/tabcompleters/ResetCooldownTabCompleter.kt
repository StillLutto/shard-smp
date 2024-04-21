package me.lutto.shardsmp.commands.tabcompleters

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.instance.CustomItem
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class ResetCooldownTabCompleter(private val shardSMP: ShardSMP) : TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {

        if (args.size == 1) {
            val itemList: MutableList<CustomItem> = shardSMP.itemManager.getItemList()
            var itemNameList: MutableList<String> = mutableListOf()
            itemNameList.add("all")
            for (item in itemList) {
                itemNameList.add(item.getId())
            }
            return StringUtil.copyPartialMatches(args[0], itemNameList, ArrayList())
        }

        return ArrayList()

    }

}