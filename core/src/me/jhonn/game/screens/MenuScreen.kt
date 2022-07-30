package me.jhonn.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import me.jhonn.game.BaseGame
import me.jhonn.game.entities.BaseActor


class MenuScreen(game: BaseGame) : BaseScreen(game) {
    init {
        BaseActor(0f, 0f, mainStage).apply {
            loadTexture("water.jpg")
            setSize(800f, 600f)
            mainStage.addActor(this)
        }

        val gameTitle = BaseActor(0f, 0f, mainStage).apply {
            loadTexture("starfish-collector.png")
            moveBy(0f, 100f)
            uiStage.addActor(this)
        }
        val startButton = TextButton("Start", game.textButtonStyle).apply {

            uiStage.addActor(this)
            addListener { e: Event ->
                if (e !is InputEvent || !e.type.equals(Type.touchDown)) {
                    return@addListener false
                }
                game.screen = LevelScreen(game)
                return@addListener false
            }
        }
        val quitButton = TextButton(" Quit ", game.textButtonStyle).apply {
            uiStage.addActor(this)
            addListener { e: Event ->
                if (e !is InputEvent || !e.type.equals(Type.touchDown)) {
                    return@addListener false
                }
                Gdx.app.exit()
                return@addListener false
            }
        }
        uiTable.apply {
            add(gameTitle).colspan(2)
            row()
            add(startButton)
            add(quitButton)
        }
    }

    override fun update(deltaTime: Float) {
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.screen = LevelScreen(game)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        return false
    }

}