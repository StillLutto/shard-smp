package me.lutto.shardsmp

import me.lutto.shardsmp.commands.GiveItemCommand
import me.lutto.shardsmp.commands.ResetCooldownCommand
import me.lutto.shardsmp.commands.tabcompleters.GiveItemTabCompleter
import me.lutto.shardsmp.commands.tabcompleters.ResetCooldownTabCompleter
import me.lutto.shardsmp.listeners.CooldownListener
import me.lutto.shardsmp.listeners.ShardListener
import me.lutto.shardsmp.listeners.weapons.*
import me.lutto.shardsmp.manager.ItemManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ShardSMP : JavaPlugin() {

    lateinit var itemManager: ItemManager

    override fun onEnable() {
        itemManager = ItemManager(this)

        Bukkit.getPluginManager().registerEvents(ShardListener(this), this)
        Bukkit.getPluginManager().registerEvents(CooldownListener(this), this)

        Bukkit.getPluginManager().registerEvents(EarthShattererListener(this), this)
        Bukkit.getPluginManager().registerEvents(EnderBowListener(this), this)
        Bukkit.getPluginManager().registerEvents(LifestealerListener(this), this)
        Bukkit.getPluginManager().registerEvents(MjolnirListener(this), this)
        Bukkit.getPluginManager().registerEvents(TitansEdgeListener(this), this)

        getCommand("giveitem")!!.setExecutor(GiveItemCommand(this))
        getCommand("giveitem")!!.setTabCompleter(GiveItemTabCompleter(this))
        getCommand("resetcooldown")!!.setExecutor(ResetCooldownCommand(this))
        getCommand("resetcooldown")!!.setTabCompleter(ResetCooldownTabCompleter(this))
    }

}
