package me.jhonn.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import me.jhonn.game.BaseGame
import me.jhonn.game.entities.BaseActor


class MenuScreen() : BaseScreen() {
    init {
        BaseActor(0f, 0f, mainStage).apply {
            loadTexture("water.jpg")
            setSize(800f, 600f)
        }
        BaseActor(0f, 0f, mainStage).apply {
            loadTexture("starfish-collector.png")
            centerAtPosition(400f, 300f)
            moveBy(0f, 100f)
        }
        TextButton("Start", BaseGame.textButtonStyle).apply {
            setPosition(150f, 150f)
            uiStage.addActor(this)
            addListener { e: Event ->
                if (e !is InputEvent || !e.type.equals(Type.touchDown)) {
                    return@addListener false
                }
                BaseGame.setActiveScreen(LevelScreen())
                return@addListener false
            }
        }
        TextButton(" Quit ", BaseGame.textButtonStyle).apply {
            setPosition(500f, 150f)
            uiStage.addActor(this)
            addListener { e: Event ->
                if (e !is InputEvent || !e.type.equals(Type.touchDown)) {
                    return@addListener false
                }
                Gdx.app.exit()
                return@addListener false

            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            BaseGame.setActiveScreen(LevelScreen())
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        return false
    }

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            BaseGame.setActiveScreen(LevelScreen())
        }
    }
}