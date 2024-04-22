package me.lutto.shardsmp.listeners.weapons

import me.lutto.shardsmp.AbilityActivateEvent
import me.lutto.shardsmp.ShardSMP
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.Waterlogged
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PoseidonTridentListener(private val shardSMP: ShardSMP) : Listener {

    @EventHandler
    fun onAbilityActivate(event: AbilityActivateEvent) {
        if (event.getCustomItem() != shardSMP.itemManager.getItem("poseidon_trident")) return

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