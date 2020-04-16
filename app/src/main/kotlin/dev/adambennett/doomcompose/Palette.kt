package dev.adambennett.doomcompose

import androidx.annotation.ColorInt

class Pallet(private val colors: Array<Int> = fireColors2) {

    val size = colors.size

    @ColorInt
    val bottomColor: Int = colors[0]

    @ColorInt
    val topColor: Int = colors[colors.size - 1]

    private val reverse = colors.mapIndexed { i, c -> c to i }.toMap()

    init {
        if (reverse.size != colors.size) {
            error("Duplicate colors supplied")
        }
    }

    @ColorInt
    fun color(index: Int): Int {
        return colors[index]
    }

    fun index(@ColorInt color: Int): Int {
        return reverse[color] ?: error("")
    }
}

private val fireColors2 = arrayOf(
    color(7, 7, 7),
    color(31, 7, 7),
    color(47, 15, 7),
    color(71, 15, 7),
    color(87, 23, 7),
    color(103, 31, 7),
    color(119, 31, 7),
    color(143, 39, 7),
    color(159, 47, 7),
    color(175, 63, 7),
    color(191, 71, 7),
    color(199, 71, 7),
    color(223, 79, 7),
    color(223, 87, 7),
    //color(223, 87, 7),
    color(215, 95, 7),
    //color(215, 95, 7),
    //color(215, 95, 7),
    color(215, 103, 15),
    color(207, 111, 15),
    color(207, 119, 15),
    color(207, 127, 15),
    color(207, 135, 23),
    color(199, 135, 23),
    color(199, 143, 23),
    color(199, 151, 31),
    color(191, 159, 31),
    //color(191, 159, 31),
    color(191, 167, 39),
    //color(191, 167, 39),
    color(191, 175, 47),
    color(183, 175, 47),
    color(183, 183, 47),
    color(183, 183, 55),
    color(207, 207, 111),
    color(223, 223, 159),
    color(239, 239, 199),
    color(255, 255, 255)
)

val fireColors = Pallet(fireColors2)

@ColorInt
fun color(r: Int, g: Int, b: Int): Int {
    return (0xff shl 24) + (r shl 16) + (g shl 8) + b
}
