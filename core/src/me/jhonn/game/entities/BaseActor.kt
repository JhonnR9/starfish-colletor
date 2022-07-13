package me.jhonn.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Rectangle
import kotlin.reflect.KClass


open class BaseActor(x: Float, y: Float, stage: Stage) : Actor() {

    private var elapsedTime: Float = 0f
    var animationPaused: Boolean = false

    private val velocityVec: Vector2 = Vector2(0f, 0f)
    private val accelerationVec: Vector2 = Vector2(0f, 0f)

    var acceleration: Float = 0f
    var deceleration = 200f
    var maxSpeed = 1000f


    init {
        this.setPosition(x, y)
        stage.addActor(this)
    }

    companion object {
        lateinit var worldBounds: Rectangle

        fun createWorldBounds(width: Float, height: Float) {
            worldBounds = Rectangle(0f, 0f, width, height)
        }

        fun createWorldBounds(ba: BaseActor) {
            createWorldBounds(ba.width, ba.height)
        }

        fun getList(stage: Stage, className: String): ArrayList<BaseActor> {
            val list = ArrayList<BaseActor>()
            var theClass: KClass<*>? = null
            try {
                theClass = Class.forName("me.jhonn.game.entities.$className").kotlin
            } catch (error: Exception) {
                error.printStackTrace()
            }
            for (actor in stage.actors) {
                if (theClass!!.isInstance(actor)) {
                    list.add(actor as BaseActor)
                }
            }
            return list
        }

        fun count(stage: Stage, className: String): Int {
            return getList(stage, className).size
        }
    }

    fun alignCamera() {
        val camera = stage.camera

        camera.position.set(x + originX, y + originY, 0f)

        camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth/2, worldBounds.width - camera.viewportWidth/2)
        camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight/2, worldBounds.height- camera.viewportHeight/2)
        camera.update()

    }

    fun boundToWorld() {
        if (x < 0) {
            x = 0f
        }
        if (x + width > worldBounds.width) {
            x = worldBounds.width - width
        }
        if (y < 0) {
            y = 0f
        }
        if (y + height > worldBounds.height) {
            y = worldBounds.height - height
        }
    }


    var boundaryPolygon: Polygon = createBoundaryRectangle()
        get() {
            field.setPosition(x, y)
            field.setOrigin(originX, originY)
            field.rotation = rotation
            field.setScale(scaleX, scaleY)
            return field
        }

    fun createBoundaryRectangle(): Polygon {
        val w = width
        val h = height
        val vertices: FloatArray = floatArrayOf(0f, 0f, w, 0f, w, h, 0f, h)
        return Polygon(vertices)
    }

    fun centerAtPosition(x: Float, y: Float) {
        setPosition(x - width / 2, y - height / 2)
    }

    fun centerAtActor(other: BaseActor) {
        centerAtPosition(other.x + other.width / 2, other.y + other.height / 2)
    }

    fun setOpacity(opacity: Float) {
        color.a = opacity
    }

    fun overlaps(other: BaseActor): Boolean {
        val poly1 = boundaryPolygon
        val poly2 = other.boundaryPolygon
        // initial test to improve performance
        return if (!poly1.boundingRectangle.overlaps(poly2.boundingRectangle)) false
        else Intersector.overlapConvexPolygons(poly1, poly2)
    }


    fun createBoundaryPolygon(numSides: Int): Polygon {
        val w = width
        val h = height
        val vertices = FloatArray(2 * numSides)
        for (i in 0 until numSides) {
            val angle = i * 6.28f / numSides
            // x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2
            // y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2
        }
        return Polygon(vertices)

    }

    fun preventOverlap(other: BaseActor): Vector2? {
        val poly1 = boundaryPolygon
        val poly2 = other.boundaryPolygon

        if (!poly1.boundingRectangle.overlaps(poly2.boundingRectangle)) {
            return null
        }
        val mtv = MinimumTranslationVector()
        val polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv)
        if (!polygonOverlap) {
            return null
        }
        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth)
        return mtv.normal
    }

    var speed: Float
        get() {
            return velocityVec.len()
        }
        set(value) {
            // if length is zero, then assume motion angle is zero degrees
            if (velocityVec.len() == 0f) {
                velocityVec.set(value, 0f)
            } else {
                velocityVec.setLength(value)
            }
        }

    var motionAngle: Float
        get() = velocityVec.angleDeg()
        set(angle) {
            velocityVec.setAngleDeg(angle)
        }


    fun isMoving(): Boolean {
        return speed > 0
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (!animationPaused) {
            elapsedTime += delta
        }
    }


    fun accelerateAtAngle(angle: Float) {
        accelerationVec.add(Vector2(acceleration, 0f).setAngleDeg(angle))
    }

    fun accelerateForward() {
        accelerateAtAngle(rotation)
    }

    fun applyPhysics(delta: Float) {
        // apply acceleration
        velocityVec.add(accelerationVec.x * delta, accelerationVec.y * delta)

        var speed = speed

        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0f) {
            speed -= deceleration * delta
        }

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0f, maxSpeed)

        // update velocity
        this.speed = speed

        // apply velocity
        moveBy(velocityVec.x * delta, velocityVec.y * delta)

        // reset acceleration
        accelerationVec.set(0f, 0f)

    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        // apply color tint effect
        val c: Color = color
        batch.setColor(c.r, c.g, c.b, c.a)
        val frame = animation?.getKeyFrame(elapsedTime)
        if (animation != null && isVisible) {
            batch.draw(frame, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
        }
    }

    private var animation: Animation<TextureRegion>? = null
        set(animation) {
            field = animation
            val tr: TextureRegion = field!!.getKeyFrame(0f)
            val w: Float = tr.regionWidth.toFloat()
            val h: Float = tr.regionHeight.toFloat()
            setSize(w, h)
            setOrigin(w / 2, h / 2)

        }

    fun loadAnimationFromFiles(
        fileNames: kotlin.Array<String>,
        frameDuration: Float,
        loop: Boolean
    ): Animation<TextureRegion> {

        val textureArray: Array<TextureRegion> = Array<TextureRegion>()
        for (fileName in fileNames) {
            val texture = Texture(Gdx.files.internal(fileName))
            texture.setFilter(Linear, Linear)
            textureArray.add(TextureRegion(texture))
        }
        val anim: Animation<TextureRegion> = Animation<TextureRegion>(frameDuration, textureArray)
        if (loop) {
            anim.playMode = Animation.PlayMode.LOOP
        } else {
            anim.playMode = Animation.PlayMode.NORMAL
        }
        if (animation == null) {
            animation = anim
        }
        return anim
    }

    fun loadAnimationFromSheet(
        filename: String,
        rows: Int,
        cols: Int,
        frameDuration: Float,
        loop: Boolean
    ): Animation<TextureRegion> {
        val texture = Texture(Gdx.files.internal(filename))
        texture.setFilter(Linear, Linear)
        val frameWidth = texture.width / cols
        val frameHeight = texture.height / rows

        val temp = TextureRegion.split(texture, frameWidth, frameHeight)
        val textureArray: Array<TextureRegion> = Array<TextureRegion>()

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                textureArray.add(temp[r][c])
            }
        }

        val anim: Animation<TextureRegion> = Animation<TextureRegion>(frameDuration, textureArray)
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        if (animation == null)
            animation = anim
        return anim

    }

    fun loadTexture(fileName: String): Animation<TextureRegion> {
        val fileNames = arrayOf(fileName)
        return loadAnimationFromFiles(fileNames, 1f, true)
    }

    fun isAnimationFinished(): Boolean {
        return animation!!.isAnimationFinished(elapsedTime)
    }

}