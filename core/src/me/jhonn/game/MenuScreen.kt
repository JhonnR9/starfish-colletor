package me.jhonn.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import me.jhonn.game.entities.BaseActor


class MenuScreen() : BaseScreen() {
    init {
        val ocean = BaseActor(0f, 0f, mainStage).apply {
            loadTexture("water.jpg")
            setSize(800f, 600f)
        }
        val title = BaseActor(0f, 0f, mainStage).apply {
            loadTexture("starfish-collector.png")
            centerAtPosition(400f, 300f)
            moveBy(0f, 100f)
        }
        val start = BaseActor(0f, 0f, mainStage).apply {
            loadTexture("message-start.png")
            centerAtPosition(400f, 300f)
            moveBy(0f, -100f)

        }
    }

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            BaseGame.setActiveScreen(LevelScreen())
        }
    }
}