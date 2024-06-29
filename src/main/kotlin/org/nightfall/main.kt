package org.nightfall

import com.formdev.flatlaf.FlatDarkLaf
import net.miginfocom.swing.MigLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class UI: JPanel() {
    init {
        layout = MigLayout("fill")
        add(JPanel().apply {
            layout = MigLayout()
            add(JButton("Create world").apply {
                addActionListener {
                    // TODO
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

fun main() {
    FlatDarkLaf.setup()
    SwingUtilities.invokeLater {
        val frame = JFrame("Nightfall")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane = UI()
        frame.setSize(800, 600)
        frame.isVisible = true
    }
}