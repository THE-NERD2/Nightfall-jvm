package org.nightfall.default.worldgen

import org.nightfall.Point
import org.nightfall.default.tiles.GrassTile
import org.nightfall.materials.TileInstance
import org.nightfall.settings.CheckboxSetting
import org.nightfall.worldgen.World
import org.nightfall.worldgen.WorldGenerator

class DefaultGen: WorldGenerator() {
    override val order = Order.BASE_TERRAIN
    override fun addBlock(world: World, x: Int, y: Int, z: Int): TileInstance<*>? {
        // TODO
        return null
    }
}
class FlatGen: WorldGenerator() {
    companion object {
        internal var inUse = false
    }

    override val order = Order.AFTER_NATURE
    override fun addBlock(world: World, x: Int, y: Int, z: Int): TileInstance<*>? {
        if(inUse) {
            return if (y < world.sizeY - 1) TileInstance(GrassTile, world, x, y, z) else null
        } else return world[Point(x, y, z)]
    }
}
object FlatGenQuestion: CheckboxSetting("Use FlatGen?") {
    init {
        addActionListener {
            FlatGen.inUse = isSelected
        }
    }
}