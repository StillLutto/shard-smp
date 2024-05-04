package me.lutto.shardsmp

import me.lutto.shardsmp.commands.*
import me.lutto.shardsmp.commands.tabcompleters.GiveItemTabCompleter
import me.lutto.shardsmp.commands.tabcompleters.LivesTabCompleter
import me.lutto.shardsmp.instance.*
import me.lutto.shardsmp.items.miscellaneous.Life
import me.lutto.shardsmp.items.miscellaneous.Shard
import me.lutto.shardsmp.items.weapons.*
import me.lutto.shardsmp.listeners.CooldownListener
import me.lutto.shardsmp.listeners.DragonEggListener
import me.lutto.shardsmp.listeners.ItemUpgradeListener
import me.lutto.shardsmp.listeners.LivesListener
import me.lutto.shardsmp.manager.ItemManager
import me.lutto.shardsmp.manager.LivesManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ShardSMP : JavaPlugin() {

    lateinit var itemManager: ItemManager
    lateinit var livesManager: LivesManager

    override fun onEnable() {
        itemManager = ItemManager(this)
        livesManager = LivesManager(this)

        Bukkit.getPluginManager().registerEvents(DragonEggListener(this), this)
        Bukkit.getPluginManager().registerEvents(ItemUpgradeListener(this), this)
        Bukkit.getPluginManager().registerEvents(LivesListener(this), this)

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
        getCommand("lives")!!.setExecutor(LivesCommand(this))
        getCommand("lives")!!.setTabCompleter(LivesTabCompleter())
        getCommand("donate")!!.setExecutor(DonateCommand(this))
        getCommand("revive")!!.setExecutor(ReviveCommand(this))

        val pyroFireGiveRunnable = PyroFireGiveRunnable(this)
        val lifestealerHealthBoostRunnable = LifestealerHealthBoostRunnable(this)
        val poseidonWaterBreathingRunnable = PoseidonWaterBreathingRunnable(this)
        val tankShieldResistanceRunnable = TankShieldResistanceRunnable(this)
        val titansEdgeStrengthRunnable = TitansEdgeStrengthRunnable(this)
        val vanishBladeInvisibilityRunnable = VanishBladeInvisibilityRunnable(this)
        pyroFireGiveRunnable.start()
        lifestealerHealthBoostRunnable.start()
        poseidonWaterBreathingRunnable.start()
        tankShieldResistanceRunnable.start()
        titansEdgeStrengthRunnable.start()
        vanishBladeInvisibilityRunnable.start()
    }

}
