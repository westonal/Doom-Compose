package dev.adambennett.doomcompose

import android.graphics.Bitmap
import android.view.Choreographer
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.foundation.Canvas
import androidx.ui.geometry.Offset
import androidx.ui.graphics.Paint
import androidx.ui.graphics.asImageAsset
import androidx.ui.layout.fillMaxSize
import dev.adambennett.doomcompose.models.CanvasMeasurements
import dev.adambennett.doomcompose.models.WindDirection
import kotlin.math.max
import kotlin.random.Random

@Model
data class DoomState(var pixels: Bitmap? = null)

@Composable
fun DoomCompose(
    state: DoomState = DoomState()
) {
    DoomCanvas(state) { canvas ->
        setupFireView(canvas, state)
    }
}

@Composable
fun DoomCanvas(
    state: DoomState,
    measurements: (CanvasMeasurements) -> Unit
) {
    val paint = remember { Paint() }
    var measured = false

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasState = CanvasMeasurements(
            size.width.value.toInt(),
            size.height.value.toInt()
        )

        if (!measured) {
            measured = true
            measurements(canvasState)
        }

        val pixels = state.pixels
        if (pixels != null) {
            val imageAsset = pixels.asImageAsset()
            scale(size.width.value / pixels.width, size.height.value / pixels.height)
            drawImage(imageAsset, Offset.zero, paint)
        }
    }
}

private fun setupFireView(
    canvas: CanvasMeasurements,
    doomState: DoomState,
    windDirection: WindDirection = WindDirection.Left
) {
    val pixelArray =
        Bitmap.createBitmap(canvas.widthPixel, canvas.heightPixel, Bitmap.Config.ARGB_8888)
            .apply { createFireSource(this) }

    val callback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            calculateFirePropagation(pixelArray, canvas, windDirection)
            doomState.pixels = pixelArray

            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    Choreographer.getInstance().postFrameCallback(callback)
}

private fun createFireSource(firePixels: Bitmap) {
    android.graphics.Canvas(firePixels).apply {
        drawColor(fireColors.bottomColor)
        drawLine(0f,
            (firePixels.height - 1).toFloat(),
            firePixels.width.toFloat(),
            (firePixels.height - 1).toFloat(),
            android.graphics.Paint().apply { color = fireColors.topColor }
        )
    }
}

private fun calculateFirePropagation(
    firePixels: Bitmap,
    canvasMeasurements: CanvasMeasurements,
    windDirection: WindDirection
) {
    val limit = fireColors.size * 2
    for (x in 0 until firePixels.width) {
        for (y in max(0, firePixels.height - limit) until firePixels.height - 1) {
            val currentPixelIndex = x + (canvasMeasurements.widthPixel * y)
            updateFireIntensityPerPixel(
                currentPixelIndex,
                firePixels,
                x, y,
                canvasMeasurements,
                windDirection
            )
        }
    }
}

private fun updateFireIntensityPerPixel(
    currentPixelIndex: Int,
    firePixels: Bitmap,
    x: Int, y: Int,
    measurements: CanvasMeasurements,
    windDirection: WindDirection
) {
    val offset = if (measurements.tallerThanWide) 2 else 3
    val decay = Random.nextInt(offset)
    val bellowPixelFireIntensity = fireColors.index(firePixels.getPixel(x, y + 1))
    val newFireIntensity = max(bellowPixelFireIntensity - decay, 0)

    val newPosition = when (windDirection) {
        WindDirection.Right -> if (currentPixelIndex - decay >= 0) currentPixelIndex - decay else currentPixelIndex
        WindDirection.Left -> if (currentPixelIndex + decay >= 0) currentPixelIndex + decay else currentPixelIndex
        WindDirection.None -> currentPixelIndex
    }

    firePixels.setPixel(
        newPosition % measurements.widthPixel,
        newPosition / measurements.widthPixel,
        fireColors.color(newFireIntensity)
    )
}



