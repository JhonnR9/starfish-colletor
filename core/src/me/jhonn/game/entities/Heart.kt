package me.jhonn.game.entities

import com.badlogic.gdx.scenes.scene2d.Stage

class Heart(x: Float, y: Float, stage: Stage) : BaseActor(x, y, stage) {
    init {
        loadAnimationFromSheet("heart.png",2,2,0.2f,true)
        setSize(50f,50f)
        boundaryPolygon = createBoundaryPolygon(8)
    }
}