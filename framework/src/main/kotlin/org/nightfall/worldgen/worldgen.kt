package org.nightfall.worldgen

import org.nightfall.Point
import org.nightfall.materials.TileInstance
import org.nightfall.mods.Mods
import org.nightfall.TileMap
import java.io.Serializable
import kotlin.reflect.full.primaryConstructor

class World(val sizeX: Int, val sizeY: Int, val sizeZ: Int): Serializable {
    private var initialized = false
    val tiles: TileMap = TileMap { point, _ ->
        point.orthogonals.forEach {
            this[it]?.calculateOcclusion()
        }
    }

    fun initialize() = if(!initialized) {
        val worldGenerators: List<WorldGenerator> = Mods.worldGenerators.map {
            it.primaryConstructor?.call() ?: throw IllegalStateException("WorldGenerator must have a constructor")
        }.sortedBy { it.order.value }
        worldGenerators.forEach {
            for(x in 0..sizeX) {
                for(y in 0..sizeY) {
                    for(z in 0..sizeZ) {
                        val newBlock = it.addBlock(this, x, y, z)
                        if(newBlock is TileInstance<*>) {
                            tiles[Point(x, y, z)] = newBlock
                        }
                    }
                }
            }
        }
        initialized = true
    } else {}
    fun getBlock(x: Int, y: Int, z: Int) = tiles[Point(x, y, z)]
}
abstract class WorldGenerator {
    enum class Order(val value: Int) {
        BASE_TERRAIN(1),
        TERRAIN(2),
        AFTER_TERRAIN(3),
        NATURE(4),
        AFTER_NATURE(5),
        STRUCTURES(6),
        AFTER_STRUCTURES(7),
        AFTER_ALL(8)
    }

    abstract val order: Order
    abstract fun addBlock(world: World, x: Int, y: Int, z: Int): TileInstance<*>?
}