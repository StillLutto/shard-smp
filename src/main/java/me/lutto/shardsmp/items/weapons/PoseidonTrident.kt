package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.Upgradable
import me.lutto.shardsmp.items.events.AbilityActivateEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.block.data.Waterlogged
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class PoseidonTrident(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "poseidon_trident",
    Material.TRIDENT,
    MiniMessage.miniMessage().deserialize("<gradient:#1616aa:#3646ff>Poseidon's Trident")
        .decoration(TextDecoration.ITALIC, false),
    listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Left Click]").decoration(TextDecoration.ITALIC, false)),
    11,
    false,
    180,
    true
), Upgradable, Listener {

    override fun getUpgradedCooldownTime(): Int = 120
    override fun getUpgradedCustomModelData(): Int = 11

    init {
        val riptideEnchant = ItemStack(Material.ENCHANTED_BOOK)
        val riptideEnchantItemMeta = riptideEnchant.itemMeta as EnchantmentStorageMeta
        riptideEnchantItemMeta.addEnchant(Enchantment.RIPTIDE, 3, false)
        riptideEnchant.setItemMeta(riptideEnchantItemMeta)

        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "PEP",
            "PTP",
            "SSS"
        )
        recipe.setIngredient('E', ExactChoice(riptideEnchant))
        recipe.setIngredient('S', ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('P', Material.SPONGE)
        recipe.setIngredient('T', Material.TRIDENT)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onAbilityActivate(event: AbilityActivateEvent) {
        if (event.getItem() != shardSMP.itemManager.getItem("poseidon_trident")) return

        val world: World = event.getPlayer().world
        val playerLocation: Location = event.getPlayer().location
        val radius = 10
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    val currentLocation = playerLocation.clone().add(x.toDouble(), y.toDouble(), z.toDouble())

                    val currentBlock = world.getBlockAt(currentLocation)
                    val blockType = currentBlock.type
                    val blockData = currentBlock.blockData

                    if (playerLocation.distance(currentLocation) > radius) continue

                    if (blockType == Material.WATER || blockType == Material.KELP || blockType == Material.KELP_PLANT || blockType == Material.SEAGRASS || blockType == Material.TALL_SEAGRASS) {
                        currentBlock.type = Material.AIR
                    } else if (blockData is Waterlogged && blockData.isWaterlogged) {
                        blockData.isWaterlogged = false
                        currentBlock.blockData = blockData
                    }
                }
            }
        }
    }

}
