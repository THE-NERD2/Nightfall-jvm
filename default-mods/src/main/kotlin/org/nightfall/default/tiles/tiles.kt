package org.nightfall.default.tiles

import org.nightfall.materials.Tile
import org.nightfall.worldgen.World

class GrassTile(world: World, x: Int, y: Int, z: Int): Tile(world, x, y, z) {
    override val modelPath = "org/nightfall/default/models/cube_1111.gltf"
    override val texturePath = "org/nightfall/default/textures/grass.png"
}