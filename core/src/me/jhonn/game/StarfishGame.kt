package me.jhonn.game

import me.jhonn.game.screens.MenuScreen

class StarfishGame : BaseGame() {

    override fun create() {
        game = this
      setActiveScreen( MenuScreen())
    }


}