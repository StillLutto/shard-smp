package me.lutto.shardsmp.listeners.weapons

import me.lutto.shardsmp.AbilityActivateEvent
import me.lutto.shardsmp.ShardSMP
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable

class MjolnirListener(private val shardSMP: ShardSMP) : Listener {

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
            nearbyPlayer.damage(3.0)
        }
    }

    @EventHandler
    fun onAbilityActivated(event: AbilityActivateEvent) {
        if (event.getCustomItem() != shardSMP.itemManager.getItem("mjolnir")) return
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