package me.jhonn.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage

abstract class GameBeta : Game() {
    protected lateinit var mainStage: Stage

    override fun create() {
        mainStage = Stage()
        initialize()
    }
    abstract fun initialize()
    abstract fun update(deltaTime: Float)
    override fun render() {
        val delta = Gdx.graphics.deltaTime
        mainStage.act(delta)
        update(delta)

        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mainStage.draw()
    }

}