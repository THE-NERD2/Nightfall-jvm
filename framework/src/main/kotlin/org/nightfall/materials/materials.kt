package org.nightfall.materials

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.math.Vector3
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader
import net.mgsx.gltf.scene3d.scene.SceneAsset
import org.nightfall.worldgen.World
import java.io.Serializable

open class Tile(val modelPath: String, val texturePath: String, resolver: FileHandleResolver? = null) {
    private val mgr = AssetManager(resolver)
    lateinit var model: Model

    init {
        mgr.setLoader(SceneAsset::class.java, ".gltf", GLTFAssetLoader())
    }
    internal fun load() {
        mgr.load(modelPath, SceneAsset::class.java)
        mgr.finishLoading()
    }
    internal fun initialize() {
        model = mgr.get(modelPath, SceneAsset::class.java).scene.model
        val texture = Texture(Gdx.files.internal(texturePath))
        val material = Material(TextureAttribute.createDiffuse(texture))
        /*for(node in model.nodes) {
            for(part in node.parts) {
                part.material = material
            }
        }*/
        model.materials[0] = material
    }
    open fun update(instance: TileInstance<*>, dt: Double) {}
}
class TileInstance<T: Tile>(val type: T, val world: World, val x: Int, val y: Int, val z: Int): Serializable {
    val instance: ModelInstance
    init {
        instance = ModelInstance(type.model)
        instance.transform.setTranslation(Vector3(x.toFloat(), y.toFloat(), z.toFloat()))
        instance.transform.scale(0.0625f, 0.0625f, 0.0625f)
    }
    fun dispose() = type.model.dispose()
}