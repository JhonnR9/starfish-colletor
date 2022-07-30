package me.jhonn.game.entities

import com.badlogic.gdx.scenes.scene2d.Stage
import me.jhonn.game.entities.BaseActor

class Whirlpool(x:Float, y:Float, stage: Stage): BaseActor(x, y, stage) {
    init {
        loadAnimationFromSheet("whirlpool.png",2,5,0.1f,false)
        stage.addActor(this)
    }
    override fun act(delta: Float) {
        super.act(delta)
        if (isAnimationFinished()){
            remove()
        }
    }
}