package me.jhonn.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
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


class LevelScreen(game: BaseGame) : BaseScreen(game) {
    private val turtle: Turtle
    private var win: Boolean = false
    private var ocean: BaseActor = BaseActor(0f, 0f, mainStage)
    private var starfishLabel: Label


    init {
        val fileHandle: FileHandle = Gdx.files.local("assets/OpenSans.ttf")
        starfishLabel = Label("Star Left: ", game.getLabelStyle(fileHandle))
        starfishLabel.color = Color.CYAN


        val buttonStyle = ButtonStyle().apply {
            val buttonTexture = Texture(Gdx.files.internal("undo.png"))
            val buttonRegion = TextureRegion(buttonTexture)
            up = TextureRegionDrawable(buttonRegion)

        }

        val restartButton = Button(buttonStyle).apply {
            color = Color.CYAN
            addListener { e: Event ->
                if (e !is InputEvent || !e.type.equals(Type.touchDown)) {
                    return@addListener false
                }
                game.screen = LevelScreen(game)
                return@addListener false
            }
        }

        ocean.apply {
            loadTexture("water-border.jpg")
            setSize(1200f, 900f)
            BaseActor.createWorldBounds(ocean)
            mainStage.addActor(this)
        }



        Starfish(200f, 800f, mainStage)
        Starfish(600f, 100f, mainStage)
        Starfish(950f, 700f, mainStage)

        Rock(500f, 527f, mainStage)
        Rock(150f, 680f, mainStage)
        Rock(60f, 39f, mainStage)
        Rock(200f, 240f, mainStage)


        turtle = Turtle(300f, 400f, mainStage)
        mainStage.addActor(turtle)

        uiTable.apply {
            pad(10f)
            add(starfishLabel).top()
            add().expandX().expandY()
            add(restartButton).top()

        }
    }

    override fun update(deltaTime: Float) {
        starfishLabel.setText("Star Left: ${BaseActor.count(mainStage, "Starfish")}")

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
                uiStage.addActor(this)
            }

        }
    }

    override fun resume() {
        game.screen = MenuScreen(game)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.E) {
            game.screen = LevelBonus(game)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.screen = MenuScreen(game)
        }
        return false
    }
}