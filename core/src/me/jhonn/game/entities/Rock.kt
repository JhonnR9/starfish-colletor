package me.jhonn.game.entities

import com.badlogic.gdx.scenes.scene2d.Stage

class Rock(x: Float, y: Float, stage: Stage) : BaseActor(x, y, stage) {
    init {
        loadTexture("assets/rock.png")
        boundaryPolygon = createBoundaryPolygon(8)
        stage.addActor(this)
    }
}