package org.nightfall.mods

import org.nightfall.materials.Tile
import org.nightfall.settings.CheckboxSetting
import org.nightfall.worldgen.WorldGenerator
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

val JarEntry.nameWithoutExtension get() = name.substring(0, name.length - 6)
object Mods {
    val tiles = arrayListOf<KClass<Tile>>()
    val worldGenerators = arrayListOf<KClass<WorldGenerator>>()
    val checkboxSettings = arrayListOf<KClass<CheckboxSetting>>()

    fun initialize(extras: Array<String>) {
        print("Loading mods...")
        val files = extras.toCollection(arrayListOf())
        files.addAll(File("mods/").listFiles()?.map { it.absolutePath } ?: listOf())
        files.forEach {
            val file = File(it)
            val jar = JarFile(file)
            val urlClassLoader = URLClassLoader(arrayOf(file.toURI().toURL()), ClassLoader.getSystemClassLoader())
            val entries = jar.entries().asSequence().filter { it.name.endsWith(".class") && !it.name.contains("$") }
            entries.forEach {
                val className = it.nameWithoutExtension.replace("/", ".")
                try {
                    val clazz = urlClassLoader.loadClass(className).kotlin
                    if(clazz.superclasses.any { it == Tile::class }) {
                        tiles.add(clazz as KClass<Tile>)
                    } else if(clazz.superclasses.any { it == WorldGenerator::class }) {
                        worldGenerators.add(clazz as KClass<WorldGenerator>)
                    } else if(clazz.superclasses.any { it == CheckboxSetting::class }) {
                        checkboxSettings.add(clazz as KClass<CheckboxSetting>)
                    }
                } catch(_: ClassNotFoundException) {
                } catch(_: NoClassDefFoundError) {}
            }
        }
        println("done!")
    }
}