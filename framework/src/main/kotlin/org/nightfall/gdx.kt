package org.nightfall

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import org.nightfall.materials.TileInstance
import org.nightfall.mods.Mods
import org.nightfall.worldgen.World
import kotlin.reflect.full.createInstance

class GameWindow(private val world: World): KtxApplicationAdapter {
    private lateinit var env: Environment
    private lateinit var cam: PerspectiveCamera
    private lateinit var camController: CameraInputController
    private lateinit var modelBatch: ModelBatch
    override fun create() {
        env = Environment()
        env.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        env.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
        env.add(DirectionalLight().set(0.45f, 0.45f, 0.45f, 0.5f, 0.8f, 0.1f))
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.position.set(-15f, 3f, -15f)
        cam.lookAt(15f, 0f, 15f)
        cam.near = 1f
        cam.far = 100f
        cam.update()
        camController = CameraInputController(cam)
        Gdx.input.inputProcessor = camController
        modelBatch = ModelBatch()
        Mods.tiles.map {
            try {
                it.createInstance()
            } catch(e: IllegalArgumentException) {
                try {
                    it.objectInstance
                } catch(e: IllegalArgumentException) {
                    null
                }
            }
        }.also {
            it.forEach {
                it?.load()
                it?.initialize()
            }
        }
        world.initialize()
    }
    override fun render() {
        camController.update()
        clearScreen(0.5294f, 0.8078f, 0.9216f, 1f)
        modelBatch.begin(cam)
        for(x in 0..world.sizeX) {
            for(y in 0..world.sizeY) {
                for(z in 0..world.sizeZ) {
                    val tile = world.getBlock(x, y, z)
                    if(tile is TileInstance<*>) {
                        modelBatch.render(tile.instance, env)
                    }
                }
            }
        }
        modelBatch.end()
    }
    override fun dispose() {
        modelBatch.dispose()
        for(x in 0..world.sizeX) {
            for(y in 0..world.sizeY) {
                for(z in 0..world.sizeZ) {
                    world.getBlock(x, y, z)?.dispose()
                }
            }
        }
    }
    override fun resize(width: Int, height: Int) {
        Gdx.gl.glViewport(0, 0, width, height)
    }
}