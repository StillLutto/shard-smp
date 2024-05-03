package me.lutto.shardsmp

import me.lutto.shardsmp.commands.GiveItemCommand
import me.lutto.shardsmp.commands.ResetCooldownCommand
import me.lutto.shardsmp.commands.UpgradeItemCommand
import me.lutto.shardsmp.commands.tabcompleters.GiveItemTabCompleter
import me.lutto.shardsmp.instance.*
import me.lutto.shardsmp.items.miscellaneous.Life
import me.lutto.shardsmp.items.miscellaneous.Shard
import me.lutto.shardsmp.items.weapons.*
import me.lutto.shardsmp.listeners.CooldownListener
import me.lutto.shardsmp.listeners.DragonEggListener
import me.lutto.shardsmp.manager.ItemManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ShardSMP : JavaPlugin() {

    lateinit var itemManager: ItemManager

    override fun onEnable() {
        itemManager = ItemManager(this)

        Bukkit.getPluginManager().registerEvents(DragonEggListener(this), this)

        Bukkit.getPluginManager().registerEvents(Shard(this), this)
        Bukkit.getPluginManager().registerEvents(Life(this), this)
        Bukkit.getPluginManager().registerEvents(EarthShatterer(this), this)
        Bukkit.getPluginManager().registerEvents(EnderBow(this), this)
        Bukkit.getPluginManager().registerEvents(Lifestealer(this), this)
        Bukkit.getPluginManager().registerEvents(Mjolnir(this), this)
        Bukkit.getPluginManager().registerEvents(PoseidonTrident(this), this)
        Bukkit.getPluginManager().registerEvents(PyroSword(this), this)
        Bukkit.getPluginManager().registerEvents(TankShield(this), this)
        Bukkit.getPluginManager().registerEvents(TitansEdge(this), this)
        Bukkit.getPluginManager().registerEvents(VanishBlade(this), this)

        Bukkit.getPluginManager().registerEvents(CooldownListener(this), this) // always has to be after custom items

        getCommand("giveitem")!!.setExecutor(GiveItemCommand(this))
        getCommand("giveitem")!!.setTabCompleter(GiveItemTabCompleter(this))
        getCommand("resetcooldown")!!.setExecutor(ResetCooldownCommand(this))
        getCommand("upgradeitem")!!.setExecutor(UpgradeItemCommand(this))

        val pyroFireGiveRunnable = PyroFireGiveRunnable(this)
        val lifestealerHealthBoostRunnable = LifestealerHealthBoostRunnable(this)
        val poseidonWaterBreathingRunnable = PoseidonWaterBreathingRunnable(this)
        val titansEdgeStrengthRunnable = TitansEdgeStrengthRunnable(this)
        val vanishBladeInvisibilityRunnable = VanishBladeInvisibilityRunnable(this)
        pyroFireGiveRunnable.start()
        lifestealerHealthBoostRunnable.start()
        poseidonWaterBreathingRunnable.start()
        titansEdgeStrengthRunnable.start()
        vanishBladeInvisibilityRunnable.start()
    }

}
