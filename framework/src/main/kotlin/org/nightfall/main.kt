package org.nightfall

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.formdev.flatlaf.FlatDarkLaf
import net.miginfocom.swing.MigLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import org.nightfall.mods.Mods
import org.nightfall.worldgen.World

class UI: JPanel() {
    init {
        layout = MigLayout("fill")
        add(JPanel().apply {
            layout = MigLayout()
            add(JButton("Create world").apply {
                addActionListener {
                    // TODO: get size from user
                    val world = World(30, 4, 30)
                    val config = Lwjgl3ApplicationConfiguration()
                    config.setTitle("Nightfall")
                    config.setWindowedMode(800, 600)
                    config.useVsync(true)
                    config.setForegroundFPS(60)
                    Lwjgl3Application(GameWindow(world), config)
                }
            }, "east")
        }, "north")
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
    FlatDarkLaf.setup()
    SwingUtilities.invokeLater {
        val frame = JFrame("Nightfall")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane = UI()
        frame.setSize(800, 600)
        frame.isVisible = true
    }
}