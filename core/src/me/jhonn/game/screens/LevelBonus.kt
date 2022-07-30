package me.jhonn.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import me.jhonn.game.BaseGame
import me.jhonn.game.entities.BaseActor
import me.jhonn.game.entities.Turtle

class LevelBonus(game: BaseGame) : BaseScreen(game) {
    private val turtle: Turtle
    private var starfishLabel: Label

    init {
        BaseActor(0f, 0f, mainStage).apply {
            loadTexture("water-border.jpg")
            setSize(800f, 600f)
            BaseActor.createWorldBounds(this)
            mainStage.addActor(this)
        }
        turtle = Turtle(20f, 50f, mainStage)

        val buttonStyle = Button.ButtonStyle().apply {
            val buttonTexture = Texture(Gdx.files.internal("undo.png"))
            val buttonRegion = TextureRegion(buttonTexture)
            up = TextureRegionDrawable(buttonRegion)
        }
        Button(buttonStyle).apply {
            color = Color.CYAN
            setPosition(720f, 520f)
            uiStage.addActor(this)

            addListener { e: Event ->
                if (e !is InputEvent || !e.type.equals(InputEvent.Type.touchDown)) {
                    return@addListener false
                }
                game.screen = LevelBonus(game)
                return@addListener false
            }
        }
        val fileHandle: FileHandle = Gdx.files.local("rosalinda_berlinata.ttf")
        starfishLabel = Label("Star Left: ", game.getLabelStyle(fileHandle)).apply {
            setPosition(20f, 520f)
            color = Color.CYAN
            uiStage.addActor(this)
        }
    }

    override fun update(deltaTime: Float) {

    }

    override fun keyDown(keycode: Int): Boolean {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.screen = LevelScreen(game)
        }
        return false
    }
}