package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.events.AbilityActivateEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.scheduler.BukkitRunnable

class Mjolnir(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "mjolnir",
    Material.NETHERITE_AXE,
    MiniMessage.miniMessage().deserialize("<gradient:#3a4261:#7277a6>Mjölnir")
        .decoration(TextDecoration.ITALIC, false),
    listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
    6,
    true,
    120,
    true
), Listener {

    init {
        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "SLS",
            "TAT",
            "SNS"
        )
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        recipe.setIngredient('S', ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('A', Material.NETHERITE_AXE)
        recipe.setIngredient('T', Material.TRIDENT)
        recipe.setIngredient('L', Material.LODESTONE)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    private fun launchPlayer(player: Player, launchLocation: Location, launchPower: Double) {
        val direction = player.location.toVector().subtract(launchLocation.toVector()).normalize()
        val launchVelocity = direction.multiply(launchPower).setY(1)
        player.velocity = launchVelocity
    }

    fun hitNearbyEntities(world: World, centerRadiusLocation: Location) {
        for (nearbyEntity in world.getNearbyEntities(centerRadiusLocation, 5.0, 5.0, 5.0)) {
            if (nearbyEntity !is Player) continue
            val nearbyPlayer: Player = nearbyEntity

            launchPlayer(nearbyPlayer, centerRadiusLocation, 1.0)
            if (nearbyPlayer.health <= 6) {
                nearbyPlayer.health = 0.0
            }
            nearbyPlayer.health -= 6
        }
    }

    @EventHandler
    fun onAbilityActivated(event: AbilityActivateEvent) {
        if (event.getItem() != shardSMP.itemManager.getItem("mjolnir")) return
        val player: Player = event.getPlayer()
        val world = player.world

        val thrownMjolnir: ItemDisplay = player.world.spawnEntity(player.eyeLocation, EntityType.ITEM_DISPLAY) as ItemDisplay
        thrownMjolnir.itemStack = shardSMP.itemManager.getItem("mjolnir")!!.getItemStack()
        thrownMjolnir.itemDisplayTransform = ItemDisplay.ItemDisplayTransform.THIRDPERSON_RIGHTHAND
        val velocity = player.location.direction.multiply(0.75)

        object : BukkitRunnable() {
            override fun run() {
                val blockTypeAtMjolnirLocation = world.getBlockAt(thrownMjolnir.location).type // or maybe materialAtMjolnirPosition...?

                if (!thrownMjolnir.isValid) {
                    this.cancel()
                }

                if (blockTypeAtMjolnirLocation != Material.AIR && blockTypeAtMjolnirLocation != Material.WATER) {
                    thrownMjolnir.remove()
                    this.cancel()

                    val blockHitLocation = thrownMjolnir.location
                    world.playSound(blockHitLocation, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                    world.spawnParticle(Particle.EXPLOSION_HUGE, blockHitLocation, 1)
                    hitNearbyEntities(world, blockHitLocation)

                    return
                }

                for (nearbyPlayer in world.getNearbyEntities(thrownMjolnir.location, 0.25, 0.25, 0.25)) {
                    if (nearbyPlayer is Player && nearbyPlayer != player) {
                        thrownMjolnir.remove()
                        this.cancel()

                        world.playSound(thrownMjolnir.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                        world.spawnParticle(Particle.EXPLOSION_HUGE, thrownMjolnir.location, 1)
                        hitNearbyEntities(world, thrownMjolnir.location)
                        return
                    }
                }

                val thrownMjolnirLocation = thrownMjolnir.location.add(velocity)
                thrownMjolnir.teleport(thrownMjolnirLocation)
            }
        }.runTaskTimer(shardSMP, 1, 1)
    }

}
