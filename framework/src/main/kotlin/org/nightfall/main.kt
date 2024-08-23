package org.nightfall

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.formdev.flatlaf.FlatLightLaf
import net.miginfocom.swing.MigLayout
import java.io.File
import org.nightfall.mods.Mods
import org.nightfall.worldgen.World
import javax.swing.*

private lateinit var frame: JFrame

class UI: JPanel() {
    init {
        layout = MigLayout()
        add(JPanel().apply {
            layout = MigLayout()
            add(JButton("Create world").apply {
                addActionListener {
                    // TODO: get size from user
                    val dialog = JDialog(frame)
                    dialog.layout = MigLayout()
                    Mods.checkboxSettings.map { it.instance!! }.forEach { dialog.add(it, "wrap") }
                    dialog.add(JButton("OK").apply {
                        addActionListener {
                            dialog.isVisible = false
                            val world = World(50, 10, 50)
                            val config = Lwjgl3ApplicationConfiguration()
                            config.setTitle("Nightfall")
                            config.setWindowedMode(800, 600)
                            config.useVsync(true)
                            config.setForegroundFPS(60)
                            Lwjgl3Application(GameWindow(world), config)
                        }
                    })
                    dialog.pack()
                    dialog.title = "Settings"
                    dialog.isVisible = true
                }
            }, "east")
        })
        File("world/").list()?.forEach {
            add(JButton(it).apply {
                addActionListener {
                    // TODO
                }
            }, "wrap")
        }
    }
}

fun main(args: Array<String>) {
    Mods.initialize(args)
    FlatLightLaf.setup()
    SwingUtilities.invokeLater {
        frame = JFrame("Nightfall")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane = UI()
        frame.setSize(800, 600)
        frame.isVisible = true
    }
}