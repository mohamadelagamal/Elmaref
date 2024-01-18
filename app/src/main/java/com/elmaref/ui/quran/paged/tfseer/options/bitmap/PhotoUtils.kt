package com.elmaref.ui.quran.paged.tfseer.options.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

object PhotoUtils {
    var photoSize: Int? = null
        get() {
            if (field == null) {
                field = 1080
            }
            return field
        }
        private set

//    fun roundImage(source: Bitmap): Bitmap {
//        val size = Math.min(source.width, source.height)
//        val x = (source.width - size) / 2
//        val y = (source.height - size) / 2
//        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
//        if (squaredBitmap != source) {
//            source.recycle()
//        }
//        val bitmap = Bitmap.createBitmap(size, size, source.config)
//        val canvas = Canvas(bitmap)
//        val paint = Paint()
//        val shader = BitmapShader(
//            squaredBitmap,
//            BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP
//        )
//        paint.shader = shader
//        paint.isAntiAlias = true
//        val r = size / 2f
//        canvas.drawCircle(r, r, r, paint)
//        squaredBitmap.recycle()
//        return bitmap
//    }

    fun scaleImage(source: Bitmap): Bitmap? {
        val targetWidth: Int
        val targetHeight: Int
        val aspectRatio: Double
        if (source.width > photoSize!!) {
            targetWidth = photoSize!!
            aspectRatio = source.height.toDouble() / source.width.toDouble()
            targetHeight = (targetWidth * aspectRatio).toInt()
        } else {
            targetHeight = source.height
            targetWidth = source.width
            //            targetHeight = getPhotoSize();
//            aspectRatio = (double) source.getWidth() / (double) source.getHeight();
//            targetWidth = (int) (targetHeight * aspectRatio);
        }
        //        if (result != source) {
//            source.recycle();
//        }
        return scaleImage(source, targetWidth.toFloat(), targetHeight.toFloat())
    }

    @JvmOverloads
    fun scaleImage(
        bitmap: Bitmap?, maxWidth: Float, maxHeight: Float, minWidth: Int = 0,
        minHeight: Int = 0
    ): Bitmap? {
        if (bitmap == null) {
            return null
        }
        val photoW = bitmap.width.toFloat()
        val photoH = bitmap.height.toFloat()
        if (photoW == 0f || photoH == 0f) {
            return null
        }
        var scaleAnyway = false
        var scaleFactor = Math.max(photoW / maxWidth, photoH / maxHeight)
        if (minWidth != 0 && minHeight != 0 && (photoW < minWidth || photoH < minHeight)) {
            scaleFactor = if (photoW < minWidth && photoH > minHeight) {
                photoW / minWidth
            } else if (photoW > minWidth && photoH < minHeight) {
                photoH / minHeight
            } else {
                Math.max(photoW / minWidth, photoH / minHeight)
            }
            scaleAnyway = true
        }
        val w = (photoW / scaleFactor).toInt()
        val h = (photoH / scaleFactor).toInt()
        return if (h == 0 || w == 0) {
            null
        } else try {
            scaleImage(bitmap, w, h, scaleFactor, scaleAnyway)
        } catch (e: Throwable) {
            try {
                scaleImage(bitmap, w, h, scaleFactor, scaleAnyway)
            } catch (e2: Throwable) {
                null
            }
        }
    }

    @Throws(Exception::class)
    private fun scaleImage(
        bitmap: Bitmap, w: Int, h: Int, scaleFactor: Float,
        scaleAnyway: Boolean
    ): Bitmap {
        val scaledBitmap: Bitmap
        scaledBitmap = if (scaleFactor > 1 || scaleAnyway) {
            Bitmap.createScaledBitmap(bitmap, w, h, true)
        } else {
            bitmap
        }
        return scaledBitmap
    }

    fun scaleCenterCrop(source: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        val xScale = newWidth.toFloat() / sourceWidth
        val yScale = newHeight.toFloat() / sourceHeight
        val scale = Math.max(xScale, yScale)

        // Now get the size of the source bitmap when scaled
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        val dest = Bitmap.createBitmap(newWidth, newHeight, source.config)
        val canvas = Canvas(dest)
        canvas.drawBitmap(source, null, targetRect, null)
        return dest
    }
}
