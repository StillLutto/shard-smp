package me.lutto.shardsmp

import me.lutto.shardsmp.commands.GiveItemCommand
import me.lutto.shardsmp.commands.tabcompleters.GiveItemTabCompleter
import me.lutto.shardsmp.listeners.weapons.ShardListener
import me.lutto.shardsmp.manager.ItemManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ShardSMP : JavaPlugin() {

    lateinit var itemManager: ItemManager

    override fun onEnable() {
        itemManager = ItemManager(this)

        Bukkit.getPluginManager().registerEvents(ShardListener(this), this)

        getCommand("giveitem")!!.setExecutor(GiveItemCommand(this))
        getCommand("giveitem")!!.setTabCompleter(GiveItemTabCompleter(this))
    }

}
