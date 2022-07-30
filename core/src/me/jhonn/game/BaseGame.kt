package me.jhonn.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import me.jhonn.game.screens.BaseScreen
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable

abstract class BaseGame : Game() {
    var game: BaseGame? = null
    private var labelStyle: LabelStyle? = null

    var textButtonStyle: TextButtonStyle? = null
        get() {
            if (field == null) {
                try {
                    field = TextButtonStyle().apply {
                        val buttonTexture = Texture(Gdx.files.internal("assets/button.png"))
                        val buttonPatch = NinePatch(buttonTexture, 24, 24, 24, 24)
                        val fileHandle: FileHandle = Gdx.files.local("assets/OpenSans.ttf")
                        font = getLabelStyle(fileHandle).font
                        fontColor = Color.GRAY
                        up = NinePatchDrawable(buttonPatch)
                    }
                } catch (e: ExceptionInInitializerError) {
                    e.printStackTrace()
                    println("Error")
                }
            }
            return field
        }

    var inputMultiplexer: InputMultiplexer = InputMultiplexer()
        get() {
            if (Gdx.input.inputProcessor !is InputMultiplexer) {
                Gdx.input.inputProcessor = field
            }
            return field
        }

    fun getLabelStyle(fileHandle: FileHandle, fontParameters: FreeTypeFontParameter): LabelStyle {
        if (labelStyle == null) {
            labelStyle = LabelStyle()
        }
        val fontGenerator = FreeTypeFontGenerator(fileHandle)
        labelStyle?.font = fontGenerator.generateFont(fontParameters)

        return labelStyle as LabelStyle
    }

    fun getLabelStyle(fileHandle: FileHandle): LabelStyle {
        val fontParameters = FreeTypeFontParameter().apply {
            size = 48
            color = Color.WHITE
            borderWidth = 2f
            borderColor = Color.BLACK
            borderStraight = true
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }
        return getLabelStyle(fileHandle, fontParameters)
    }

}