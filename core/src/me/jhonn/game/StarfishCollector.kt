package me.jhonn.game

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import me.jhonn.game.entities.*

class StarfishCollector : GameBeta() {
    private lateinit var turtle: Turtle
    private lateinit var starfish: Starfish
    private lateinit var ocean: BaseActor
    private lateinit var heart: Heart
    override fun initialize() {
        ocean = BaseActor(0f, 0f, mainStage).apply {
            loadTexture("water.jpg")
            setSize(800f, 600f)
        }

        starfish = Starfish(380f, 380f, mainStage)
        turtle = Turtle(20f, 20f, mainStage)
        heart = Heart(600f, 400f, mainStage)
    }

    override fun update(deltaTime: Float) {

        if (turtle.overlaps(starfish) && !starfish.collected) {
            starfish.collect()
            val whirl = Whirlpool(0f, 0f, mainStage).apply {
                centerAtActor(starfish)
                setOpacity(0.25f)
            }

            val youWinMessage = BaseActor(0f, 0f, mainStage).apply {
                loadTexture("you-win.png")
                centerAtPosition(400f, 300f)
                setOpacity(0f)
                addAction(Actions.delay(1f))
                addAction(Actions.after(Actions.fadeIn(1f)))
            }
        }
    }
}