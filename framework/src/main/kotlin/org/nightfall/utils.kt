package org.nightfall

import org.nightfall.materials.TileInstance

data class Point(val x: Int, val y: Int, val z: Int) {
    val up by lazy { Point(x, y + 1, z) }
    val down by lazy { Point(x, y - 1, z) }
    val left by lazy { Point(x - 1, y, z) }
    val right by lazy { Point(x + 1, y, z) }
    val forward by lazy { Point(x, y, z - 1) }
    val backward by lazy { Point(x, y, z + 1) }
    val orthogonals by lazy { listOf(up, down, left, right, forward, backward) }
}

class TileMap(
    private val map: MutableMap<Point, TileInstance<*>> = mutableMapOf(),
    private val onSet: (Point, TileInstance<*>?) -> Unit
): MutableMap<Point, TileInstance<*>> by map {
    operator fun set(key: Point, value: TileInstance<*>?) {
        if(value == null) {
            map.remove(key)
        } else {
            map[key] = value
        }
        onSet(key, value)
    }
    override operator fun get(key: Point): TileInstance<*>? {
        return map[key]
    }
}