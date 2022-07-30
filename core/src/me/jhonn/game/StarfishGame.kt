package me.jhonn.game

import com.badlogic.gdx.Gdx
import me.jhonn.game.screens.MenuScreen

class StarfishGame : BaseGame() {
    override fun create() {
        game = this
        screen = (MenuScreen(this))
    }

}