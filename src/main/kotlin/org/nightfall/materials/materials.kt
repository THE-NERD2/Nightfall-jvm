package org.nightfall.materials

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import java.io.Serializable

abstract class Tile(val x: Int, val y: Int, val z: Int): Serializable {
    abstract val modelPath: String
    abstract val texturePath: String
    lateinit var model: Model

    fun update(dt: Double) {}
    fun load(mgr: AssetManager) = mgr.load(modelPath, Model::class.java)
    fun initialize(mgr: AssetManager) {
        model = mgr.get(modelPath, Model::class.java)
        val texture = Texture(Gdx.files.internal(texturePath))
        val material = Material(TextureAttribute.createDiffuse(texture))
        for(node in model.nodes) {
            for(part in node.parts) {
                part.material = material
            }
        }
    }
}