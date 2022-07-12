package me.jhonn.game.entities

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions

class Starfish(x: Float, y: Float, stage: Stage) : BaseActor(x, y, stage) {
    init {
        loadTexture("starfish.png")
        boundaryPolygon = createBoundaryPolygon(8)
        val spin: Action = Actions.rotateBy(30f, 1f)
        this.addAction(Actions.forever(spin))
    }

     var collected = false

    fun collect() {
        collected = true
        clearActions()
        addAction(Actions.fadeOut(1f))
        addAction(Actions.after(Actions.removeActor()))
    }
}