package me.jhonn.game

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import me.jhonn.game.entities.*
import kotlin.random.Random

class LevelScreen: BaseScreen() {
    private val turtle: Turtle
    private var win: Boolean = false
    var ocean: BaseActor = BaseActor(0f, 0f, mainStage)

    init {
        ocean.apply {
            loadTexture("water-border.jpg")
            setSize(1200f, 900f)
            BaseActor.createWorldBounds(ocean)
        }

        for (i in 0..5) {
            Starfish(randomX(), randomY(), mainStage).boundToWorld()
        }
        for (i in 0..5) {
            Rock(randomX(), randomY(), mainStage).boundToWorld()
        }

        turtle = Turtle(300f, 400f, mainStage)
    }
    private fun randomX(): Float {
        return Random.nextInt(0, ocean.width.toInt()).toFloat()
    }

    private fun randomY(): Float {
        return Random.nextInt(0, ocean.height.toInt()).toFloat()
    }


    override fun update(deltaTime: Float) {
        for (rockActor in BaseActor.getList(mainStage, "Rock")) {
            turtle.preventOverlap(rockActor)
        }
        for (starfishActor in BaseActor.getList(mainStage, "Starfish")) {
            val starfish = starfishActor as Starfish
            if (turtle.overlaps(starfish) && !starfish.collected) {
                with(starfish) {
                    collected = true
                    clearActions()
                    addAction(Actions.fadeOut(1f))
                    addAction(Actions.after(Actions.removeActor()))

                    Whirlpool(0f, 0f, mainStage).apply {
                        centerAtActor(starfish)
                        setOpacity(0.25f)
                    }
                }
            }

        }

        if (BaseActor.count(mainStage, "Starfish") == 0 && !win) {
            win = true
             BaseActor(0f, 0f,uiStage).apply {
                loadTexture("assets/you-win.png")
                centerAtPosition(400f, 300f)
                setOpacity(0f)
                addAction(Actions.delay(1f))
                addAction(Actions.after(Actions.fadeIn(1f)))

            }

        }
    }
}