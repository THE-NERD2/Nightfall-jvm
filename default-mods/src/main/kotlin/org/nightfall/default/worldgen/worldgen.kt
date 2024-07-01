package org.nightfall.default.worldgen

import org.nightfall.default.tiles.GrassTile
import org.nightfall.materials.TileInstance
import org.nightfall.worldgen.World
import org.nightfall.worldgen.WorldGenerator

class FlatGen: WorldGenerator() {
    override val order = Order.AFTER_NATURE
    override fun addBlock(world: World, x: Int, y: Int, z: Int) = if(y < world.sizeY - 1) TileInstance(GrassTile, world, x, y, z) else null
}