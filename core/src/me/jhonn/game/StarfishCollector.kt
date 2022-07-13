package me.jhonn.game

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import me.jhonn.game.entities.*
import kotlin.random.Random

class StarfishCollector : GameBeta() {
    private lateinit var turtle: Turtle
    private var win: Boolean = false
    lateinit var ocean: BaseActor


    override fun initialize() {
        ocean = BaseActor(0f, 0f, mainStage)
        ocean.loadTexture("water-border.jpg")
        ocean.setSize(1200f, 900f)
        BaseActor.createWorldBounds(ocean)


        for (i in 0..12) {
            Starfish(randonX(), randonY(), mainStage).boundToWorld()
        }
        for (i in 0..5) {
            Rock(randonX(), randonY(), mainStage).boundToWorld()
        }



        turtle = Turtle(200f, 20f, mainStage)
    }

    private fun randonX(): Float {
        return Random.nextInt(0, ocean.width.toInt()).toFloat()
    }

    fun randonY(): Float {
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

                    val whirl = Whirlpool(0f, 0f, mainStage).apply {
                        centerAtActor(starfish)
                        setOpacity(0.25f)
                    }
                }
            }

        }

        if (BaseActor.count(mainStage, "Starfish") == 0 && !win) {
            win = true
            val youWinMessage = BaseActor(0f, 0f, mainStage).apply {
                loadTexture("assets/you-win.png")
                centerAtPosition(400f, 300f)
                setOpacity(0f)
                addAction(Actions.delay(1f))
                addAction(Actions.after(Actions.fadeIn(1f)))

            }

        }


    }
}