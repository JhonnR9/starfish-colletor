package me.jhonn.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import me.jhonn.game.BaseGame
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type
import me.jhonn.game.entities.*
import kotlin.random.Random


class LevelScreen : BaseScreen() {
    private val turtle: Turtle
    private var win: Boolean = false
    private var ocean: BaseActor = BaseActor(0f, 0f, mainStage)
    private var starfishLabel: Label


    init {
        val buttonStyle = ButtonStyle().apply {
            val buttonTexture = Texture(Gdx.files.internal("undo.png"))
            val buttonRegion = TextureRegion(buttonTexture)
            up = TextureRegionDrawable(buttonRegion)
        }

         Button(buttonStyle).apply {
            color = Color.CYAN
            setPosition(720f, 520f)
            uiStage.addActor(this)

            addListener { e: Event ->
                if (e !is InputEvent || !e.type.equals(Type.touchDown)) {
                    return@addListener false
                }
                BaseGame.setActiveScreen(LevelScreen())
                return@addListener false
            }
        }

        ocean.apply {
            loadTexture("water-border.jpg")
            setSize(1200f, 900f)
            BaseActor.createWorldBounds(ocean)
        }

        for (i in 1..5) {
            Starfish(randomX(), randomY(), mainStage).boundToWorld()
        }
        for (i in 1..5) {
            Rock(randomX(), randomY(), mainStage).boundToWorld()
        }

        turtle = Turtle(300f, 400f, mainStage)
    }

    init {
        val fileHandle: FileHandle = Gdx.files.local("assets/OpenSans.ttf")

        starfishLabel = Label("Star Left: ", BaseGame.getLabelStyle(fileHandle)).apply {
            setPosition(20f, 520f)
            color = Color.CYAN
            uiStage.addActor(this)
        }
    }

    private fun randomX(): Float {
        return Random.nextInt(0, ocean.width.toInt()).toFloat()
    }

    private fun randomY(): Float {
        return Random.nextInt(0, ocean.height.toInt()).toFloat()
    }


    override fun update(deltaTime: Float) {
        starfishLabel.setText("Starfish Left: ${BaseActor.count(mainStage, "Starfish")}")

        for (rockActor in BaseActor.getList(mainStage, "Rock")) {
            turtle.preventOverlap(rockActor)
        }
        for (starfishActor in BaseActor.getList(mainStage, "Starfish")) {
            if (starfishActor is Starfish) {
                if (turtle.overlaps(starfishActor) && !starfishActor.collected) {
                    with(starfishActor) {
                        collected = true
                        clearActions()
                        addAction(Actions.fadeOut(1f))
                        addAction(Actions.after(Actions.removeActor()))

                        Whirlpool(0f, 0f, mainStage).apply {
                            centerAtActor(starfishActor)
                            setOpacity(0.25f)
                        }
                    }
                }
            }

        }

        if (BaseActor.count(mainStage, "Starfish") == 0 && !win) {
            win = true
            BaseActor(0f, 0f, uiStage).apply {
                loadTexture("assets/you-win.png")
                centerAtPosition(400f, 300f)
                setOpacity(0f)
                addAction(Actions.delay(1f))
                addAction(Actions.after(Actions.fadeIn(1f)))

            }

        }
    }

    override fun resume() {
        println("resume")
        BaseGame.setActiveScreen(MenuScreen())
        super.resume()
    }
}