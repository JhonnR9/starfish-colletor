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
    companion object {
        private var labelStyle: LabelStyle? = null
        var inputMultiplexer: InputMultiplexer = InputMultiplexer()
        lateinit var game: BaseGame

        @JvmStatic
        fun getLabelStyle(fileHandle: FileHandle, fontParameters: FreeTypeFontParameter): LabelStyle {
            if (labelStyle == null) {
                try {
                    val fontGenerator= FreeTypeFontGenerator(fileHandle)
                    labelStyle = LabelStyle().apply {
                        font = fontGenerator.generateFont(fontParameters)
                    }
                    return labelStyle as LabelStyle
                } catch (_: Exception) {

                }
            }
            return labelStyle as LabelStyle
        }

        fun getLabelStyle(fileHandle: FileHandle): LabelStyle {
            val fontParameters =FreeTypeFontParameter().apply {
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

        fun setActiveScreen(s: BaseScreen) {
            if (::game.isInitialized) {
                game.screen = s
                Gdx.input.inputProcessor = inputMultiplexer
            } else {
                throw UninitializedPropertyAccessException("please initialize the var game in current main GameClass before setter active screen")
            }
        }

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


    }


}