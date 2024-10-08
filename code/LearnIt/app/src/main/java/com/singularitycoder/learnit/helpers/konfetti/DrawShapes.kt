package com.singularitycoder.learnit.helpers.konfetti

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.os.Build
import com.singularitycoder.learnit.helpers.konfetti.core.models.ReferenceImage
import com.singularitycoder.learnit.helpers.konfetti.core.models.Shape
import com.singularitycoder.learnit.helpers.konfetti.core.models.Shape.Circle
import com.singularitycoder.learnit.helpers.konfetti.core.models.Shape.Circle.rect
import com.singularitycoder.learnit.helpers.konfetti.core.models.Shape.DrawableShape
import com.singularitycoder.learnit.helpers.konfetti.core.models.Shape.Rectangle
import com.singularitycoder.learnit.helpers.konfetti.core.models.Shape.Square
import com.singularitycoder.learnit.helpers.konfetti.image.ImageStore

/**
 * Draw a shape to `canvas`. Implementations are expected to draw within a square of size
 * `size` and must vertically/horizontally center their asset if it does not have an equal width
 * and height.
 */
fun Shape.draw(
    canvas: Canvas,
    paint: Paint,
    size: Float,
    imageStore: ImageStore,
) {
    when (this) {
        Square -> canvas.drawRect(0f, 0f, size, size, paint)
        Circle -> {
            rect.set(0f, 0f, size, size)
            canvas.drawOval(RectF(rect.x, rect.y, rect.width, rect.height), paint)
        }
        is Rectangle -> {
            val height = size * heightRatio
            val top = (size - height) / 2f
            canvas.drawRect(0f, top, size, top + height, paint)
        }
        is DrawableShape -> {
            val referenceImage = image
            if (referenceImage is ReferenceImage) {
                // Making use of the ImageStore for performance reasons, see ImageStore for more info
                val drawable = imageStore.getImage(referenceImage.reference) ?: return

                if (tint) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        drawable.colorFilter = BlendModeColorFilter(paint.color, BlendMode.SRC_IN)
                    } else {
                        drawable.setColorFilter(paint.color, PorterDuff.Mode.SRC_IN)
                    }
                } else if (applyAlpha) {
                    drawable.alpha = paint.alpha
                }

                val height = (size * heightRatio).toInt()
                val top = ((size - height) / 2f).toInt()

                drawable.setBounds(0, top, size.toInt(), top + height)
                drawable.draw(canvas)
            }
        }
    }
}
