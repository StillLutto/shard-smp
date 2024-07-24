package me.lutto.shardsmp

import me.lutto.shardsmp.commands.*
import me.lutto.shardsmp.commands.tabcompleters.GiveItemTabCompleter
import me.lutto.shardsmp.commands.tabcompleters.LivesTabCompleter
import me.lutto.shardsmp.items.miscellaneous.Life
import me.lutto.shardsmp.items.miscellaneous.Shard
import me.lutto.shardsmp.items.weapons.*
import me.lutto.shardsmp.listeners.*
import me.lutto.shardsmp.manager.ItemEffectManager
import me.lutto.shardsmp.manager.ItemManager
import me.lutto.shardsmp.manager.LivesManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ShardSMP : JavaPlugin() {

    companion object {
        lateinit var plugin: ShardSMP
    }

    val miniMessage: MiniMessage = MiniMessage.miniMessage()

    lateinit var itemManager: ItemManager
    lateinit var livesManager: LivesManager
    lateinit var itemEffectManager: ItemEffectManager

    override fun onEnable() {
        plugin = this
        itemManager = ItemManager(this)
        livesManager = LivesManager(this)
        itemEffectManager = ItemEffectManager(this)

        itemEffectManager.addItemEffect("lifestealer", PotionEffect(PotionEffectType.HEALTH_BOOST, PotionEffect.INFINITE_DURATION, 1), true)
        itemEffectManager.addItemEffect("poseidon_trident", PotionEffect(PotionEffectType.WATER_BREATHING, PotionEffect.INFINITE_DURATION, 0), true)
        itemEffectManager.addItemEffect("pyro_sword", PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0), true)
        itemEffectManager.addItemEffect("tank_shield", PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0), true)
        itemEffectManager.addItemEffect("earth_shatterer", PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 0), true)
        itemEffectManager.addItemEffect("titans_edge", PotionEffect(PotionEffectType.INCREASE_DAMAGE, PotionEffect.INFINITE_DURATION, 0), true)
        itemEffectManager.addItemEffect("vanish_blade", PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0), true)

        Bukkit.getPluginManager().registerEvents(ItemCraftListener(this), this)
        Bukkit.getPluginManager().registerEvents(DragonEggListener(this), this)
        Bukkit.getPluginManager().registerEvents(ItemUpgradeListener(this), this)
        Bukkit.getPluginManager().registerEvents(LivesListener(this), this)
        Bukkit.getPluginManager().registerEvents(ItemEffectListener(this), this)

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
        getCommand("convertitem")!!.setExecutor(ConvertItemCommand(this))
        getCommand("maxenchant")!!.setExecutor(MaxEnchantCommand())
    }

}
