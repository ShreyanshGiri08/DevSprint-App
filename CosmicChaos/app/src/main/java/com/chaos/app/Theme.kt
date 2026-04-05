package com.chaos.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Cyan    = Color(0xFF00F5FF)
val Purple  = Color(0xFFBB86FC)
val Pink    = Color(0xFFFF6B9D)
val Gold    = Color(0xFFFFD700)
val Orange  = Color(0xFFFF8C42)
val BgDark  = Color(0xFF060B18)
val BgMid   = Color(0xFF0D1A2E)
val Glass   = Color(0x1AFFFFFF)
val GlassBorder = Color(0x33FFFFFF)

fun typeColor(t: String) = when (t) {
    "fire"     -> Color(0xFFFF6B35)
    "water"    -> Color(0xFF4FC3F7)
    "grass"    -> Color(0xFF66BB6A)
    "electric" -> Color(0xFFFFEE58)
    "psychic"  -> Color(0xFFF48FB1)
    "ice"      -> Color(0xFF80DEEA)
    "dragon"   -> Color(0xFF7C4DFF)
    "dark"     -> Color(0xFF78909C)
    "fairy"    -> Color(0xFFF8BBD9)
    "fighting" -> Color(0xFFE53935)
    "poison"   -> Color(0xFFAB47BC)
    "ghost"    -> Color(0xFF5C6BC0)
    "steel"    -> Color(0xFF90A4AE)
    "bug"      -> Color(0xFF8BC34A)
    "rock"     -> Color(0xFFBCAAA4)
    "ground"   -> Color(0xFFD4A574)
    "flying"   -> Color(0xFF90CAF9)
    else       -> Color(0xFFBDBDBD)
}

@Composable
fun ChaosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Cyan, secondary = Purple,
            background = BgDark, surface = BgMid,
            onBackground = Color.White, onSurface = Color.White
        ),
        content = content
    )
}
