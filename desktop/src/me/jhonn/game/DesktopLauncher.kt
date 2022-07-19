package me.jhonn.game

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val myGame = StarfishGame()
        val config = Lwjgl3ApplicationConfiguration()
        config.setTitle("Starfish Collector")
        config.setWindowedMode(800,600)
        val launcher = Lwjgl3Application(myGame, config)
    }
}