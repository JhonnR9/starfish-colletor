package me.jhonn.game

import com.badlogic.gdx.Game

abstract class BaseGame : Game() {
    init {
        game = this
    }
    companion object {
        lateinit var game: BaseGame
        fun setActiveScreen(s: BaseScreen){
            game.screen = s
        }
    }
}