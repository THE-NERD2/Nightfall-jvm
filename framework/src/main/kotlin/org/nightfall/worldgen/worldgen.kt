package org.nightfall.worldgen

import org.nightfall.Point
import org.nightfall.materials.TileInstance
import org.nightfall.mods.Mods
import org.nightfall.TileMap
import org.nightfall.instance
import java.io.Serializable

class World(val sizeX: Int, val sizeY: Int, val sizeZ: Int): Serializable {
    private var initialized = false
    private val tiles: TileMap = TileMap { point, _ ->
        point.orthogonals.forEach {
            this[it]?.calculateOcclusion()
        }
    }

    fun initialize() = if(!initialized) {
        val worldGenerators = Mods.worldGenerators.map { it.instance }.sortedBy { it!!.order.value }
        worldGenerators.forEach {
            for(x in 0..sizeX) {
                for(y in 0..sizeY) {
                    for(z in 0..sizeZ) {
                        val newBlock = it!!.addBlock(this, x, y, z)
                        if(newBlock is TileInstance<*>) {
                            this[Point(x, y, z)] = newBlock
                        }
                    }
                }
            }
        }
        initialized = true
    } else {}
    operator fun get(point: Point): TileInstance<*>? {
        return tiles[point]
    }
    operator fun set(point: Point, instance: TileInstance<*>?) {
        tiles[point] = instance
    }
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