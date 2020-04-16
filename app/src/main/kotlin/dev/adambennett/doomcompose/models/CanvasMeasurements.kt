package dev.adambennett.doomcompose.models

import kotlin.math.ceil

data class CanvasMeasurements(
    private val width: Int,
    private val height: Int
) {
    private val size = 50

    val tallerThanWide: Boolean = width < height

    private val pixelSize: Int =
        ceil((if (tallerThanWide) width else height).toDouble() / size).toInt()

    val widthPixel: Int = when {
        tallerThanWide -> size
        else -> ceil(width.toDouble() / pixelSize).toInt()
    }

    val heightPixel: Int = when {
        !tallerThanWide -> size
        else -> ceil(height.toDouble() / pixelSize).toInt()
    }
}
