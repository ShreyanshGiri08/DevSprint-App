package com.chaos.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import kotlin.math.*

// ─────────────────────────────────────────────────────────────
//  ROOT
// ─────────────────────────────────────────────────────────────

@Composable
fun ChaosApp(vm: ChaosVM = androidx.hilt.navigation.compose.hiltViewModel()) {
    val state by vm.state.collectAsState()
    ChaosTheme {
        Box(Modifier.fillMaxSize()) {
            StarBackground()
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 100.dp)
            ) {
                Header()
                Spacer(Modifier.height(12.dp))

                when {
                    state.loading -> LoadingView()
                    state.error != null -> ErrorView(state.error!!) { vm.refresh() }
                    else -> {
                        state.apod?.let    { ApodCard(it) }
                        Spacer(Modifier.height(14.dp))
                        state.pokemon?.let { PokeCard(it) }
                        Spacer(Modifier.height(14.dp))
                        state.joke?.let    { JokeCard(it) }
                    }
                }
            }

            // Chaos FAB
            ChaosButton(
                loading = state.loading,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
                onClick = vm::refresh
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  STAR BACKGROUND  (canvas — no external lib needed)
// ─────────────────────────────────────────────────────────────

// Stable random stars, computed once
private val stars = List(80) {
    Triple(
        Math.random().toFloat(),   // x fraction
        Math.random().toFloat(),   // y fraction
        (Math.random() * 2f + 0.4f).toFloat() // radius
    )
}

@Composable
fun StarBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val t by inf.animateFloat(
        0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(10_000, easing = LinearEasing)), label = "t"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(BgDark, Color(0xFF0D0A1E), BgMid, BgDark))
            )
    ) {
        Canvas(Modifier.fillMaxSize()) {
            // Drifting nebula blobs
            listOf(
                Triple(0.15f, 0.2f,  Purple.copy(alpha = 0.07f)),
                Triple(0.8f,  0.15f, Cyan.copy(alpha = 0.06f)),
                Triple(0.5f,  0.7f,  Pink.copy(alpha = 0.07f)),
            ).forEach { (fx, fy, color) ->
                val cx = fx * size.width  + sin(t + fx * 10) * 30f
                val cy = fy * size.height + cos(t + fy * 10) * 25f
                drawCircle(
                    Brush.radialGradient(listOf(color, Color.Transparent),
                        center = Offset(cx, cy), radius = size.width * 0.4f),
                    radius = size.width * 0.4f, center = Offset(cx, cy)
                )
            }
            // Stars
            stars.forEach { (fx, fy, r) ->
                val alpha = (sin(t * (0.5f + r * 0.3f) + fx * 20f) * 0.4f + 0.6f)
                    .coerceIn(0.1f, 1f)
                drawCircle(Color.White.copy(alpha = alpha * 0.8f),
                    radius = r, center = Offset(fx * size.width, fy * size.height))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  HEADER
// ─────────────────────────────────────────────────────────────

@Composable
fun Header() {
    Column(Modifier.padding(top = 8.dp)) {
        Text("🌌 COSMIC CHAOS", color = Cyan,
            fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
        Text("NASA · Pokémon · Jokes  in one multiverse",
            color = Color.White.copy(alpha = 0.45f), fontSize = 12.sp)
    }
}

// ─────────────────────────────────────────────────────────────
//  GLASS CARD  (base component)
// ─────────────────────────────────────────────────────────────

@Composable
fun GlassCard(
    accent: Color = Cyan,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val inf = rememberInfiniteTransition(label = "glow")
    val glow by inf.animateFloat(0.25f, 0.65f,
        infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse), label = "g")

    Column(
        modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(accent.copy(alpha = glow * 0.35f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(22.dp.toPx()),
                    style = Stroke(6f))
            }
            .background(Glass, RoundedCornerShape(22.dp))
            .border(1.dp,
                Brush.linearGradient(listOf(
                    Color.White.copy(0.35f), accent.copy(0.25f), Color.White.copy(0.1f)
                )), RoundedCornerShape(22.dp))
            .padding(16.dp),
        content = content
    )
}

// ─────────────────────────────────────────────────────────────
//  NASA APOD CARD
// ─────────────────────────────────────────────────────────────

@Composable
fun ApodCard(apod: Apod) {
    var expanded by remember { mutableStateOf(false) }
    GlassCard(accent = Gold) {
        // Label
        Text("🌌  NASA · APOD  ·  ${apod.date}",
            color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(10.dp))

        // Image
        Box(
            Modifier.fillMaxWidth().height(190.dp)
                .clip(RoundedCornerShape(14.dp))
        ) {
            if (apod.mediaType == "image") {
                SubcomposeAsyncImage(
                    model = apod.hdurl ?: apod.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = { ShimmerBox(Modifier.fillMaxSize()) },
                    error   = { Box(Modifier.fillMaxSize().background(BgMid),
                        Alignment.Center) { Text("🌠", fontSize = 36.sp) } }
                )
                // dark gradient at bottom for readability
                Box(Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(0.65f)), startY = 80f
                    )
                ))
            } else {
                Box(Modifier.fillMaxSize().background(BgMid), Alignment.Center) {
                    Text("▶  Video on NASA website", color = Color.White.copy(0.7f), fontSize = 13.sp)
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Text(apod.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(apod.explanation,
            color = Color.White.copy(0.65f), fontSize = 13.sp, lineHeight = 19.sp,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis)
        TextButton(onClick = { expanded = !expanded }, contentPadding = PaddingValues(0.dp)) {
            Text(if (expanded) "Show less ▲" else "Read more ▼",
                color = Gold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  POKÉMON CARD
// ─────────────────────────────────────────────────────────────

@Composable
fun PokeCard(p: Pokemon) {
    val accent = typeColor(p.primaryType)

    // float animation
    val inf = rememberInfiniteTransition(label = "float")
    val dy by inf.animateFloat(-5f, 5f,
        infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "dy")

    GlassCard(accent = accent) {
        Text("⚡  POKÉDEX  ·  #${"%03d".format(p.id)}",
            color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Pokémon image with glow
            Box(Modifier.size(110.dp).offset(y = dy.dp), Alignment.Center) {
                Box(Modifier.size(90.dp).background(
                    Brush.radialGradient(listOf(accent.copy(0.3f), Color.Transparent)), CircleShape))
                AsyncImage(p.imageUrl, contentDescription = p.name,
                    modifier = Modifier.size(100.dp))
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(p.name.replaceFirstChar { it.uppercase() },
                    color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    p.types.forEach { TypeBadge(it.type.name) }
                }
                Spacer(Modifier.height(8.dp))
                Text("${p.weight / 10f} kg", color = Color.White.copy(0.55f), fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(12.dp))
        Divider(color = Color.White.copy(0.08f))
        Spacer(Modifier.height(10.dp))

        // Stats
        listOf(
            "HP"  to p.stat("hp"),
            "ATK" to p.stat("attack"),
            "DEF" to p.stat("defense"),
            "SPD" to p.stat("speed"),
        ).forEach { (label, value) ->
            StatRow(label, value, accent)
            Spacer(Modifier.height(5.dp))
        }
    }
}

@Composable
fun TypeBadge(type: String) {
    val c = typeColor(type)
    Box(
        Modifier
            .background(c.copy(0.2f), RoundedCornerShape(50))
            .border(1.dp, c.copy(0.6f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(type.replaceFirstChar { it.uppercase() },
            color = c, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatRow(label: String, value: Int, color: Color) {
    val anim by animateFloatAsState(
        value / 255f, tween(700, easing = FastOutSlowInEasing), label = label)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color.White.copy(0.5f), fontSize = 11.sp,
            modifier = Modifier.width(32.dp))
        Text("$value", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.width(28.dp))
        Box(
            Modifier.weight(1f).height(4.dp)
                .background(Color.White.copy(0.1f), RoundedCornerShape(50))
        ) {
            Box(Modifier.fillMaxWidth(anim).fillMaxHeight()
                .background(
                    Brush.horizontalGradient(listOf(color.copy(0.7f), color)),
                    RoundedCornerShape(50)
                ))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  JOKE CARD
// ─────────────────────────────────────────────────────────────

@Composable
fun JokeCard(joke: Joke) {
    GlassCard(accent = Orange) {
        Text("😂  DAD JOKE  ·  TRANSMISSION", color = Orange,
            fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(14.dp))
        Text("\"${joke.joke}\"",
            color = Color.White, fontSize = 15.sp, fontStyle = FontStyle.Italic,
            lineHeight = 22.sp, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Text("ba dum tss 🥁", color = Orange.copy(0.6f), fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

// ─────────────────────────────────────────────────────────────
//  CHAOS FAB
// ─────────────────────────────────────────────────────────────

@Composable
fun ChaosButton(loading: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val inf = rememberInfiniteTransition(label = "fab")
    val pulse by inf.animateFloat(1f, 1.18f,
        infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "pulse")

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(64.dp).scale(if (loading) pulse else 1f),
            Alignment.Center
        ) {
            // glow ring
            Box(Modifier.size(72.dp).background(Cyan.copy(0.15f), CircleShape))
            // button
            FloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
                modifier = Modifier.size(56.dp),
                containerColor = Color.Transparent,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                shape = CircleShape,
            ) {
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.radialGradient(listOf(Cyan, Purple, Pink))
                    ),
                    Alignment.Center
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            Modifier.size(24.dp), color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Icon(Icons.Default.Refresh, null, Modifier.size(26.dp))
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text("CHAOS", color = Color.White.copy(0.4f),
            fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
    }
}

// ─────────────────────────────────────────────────────────────
//  LOADING / ERROR
// ─────────────────────────────────────────────────────────────

@Composable
fun LoadingView() {
    Column(Modifier.fillMaxWidth().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)) {

        val inf = rememberInfiniteTransition(label = "spin")
        val angle by inf.animateFloat(0f, 360f,
            infiniteRepeatable(tween(1200, easing = LinearEasing)), label = "a")

        Text("🌌", fontSize = 52.sp,
            modifier = Modifier.graphicsLayer { rotationZ = angle * 0.1f })
        Text("Scanning the multiverse...", color = Cyan, fontSize = 15.sp)
        Text("NASA  ·  Pokémon  ·  Jokes", color = Color.White.copy(0.4f), fontSize = 12.sp)
        Spacer(Modifier.height(8.dp))
        ShimmerBox(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(22.dp)))
        ShimmerBox(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(22.dp)))
        ShimmerBox(Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(22.dp)))
    }
}

@Composable
fun ShimmerBox(modifier: Modifier) {
    val inf = rememberInfiniteTransition(label = "shimmer")
    val x by inf.animateFloat(-300f, 900f,
        infiniteRepeatable(tween(1100, easing = LinearEasing)), label = "x")
    Box(modifier.background(
        Brush.linearGradient(
            listOf(Color.White.copy(0.04f), Color.White.copy(0.12f), Color.White.copy(0.04f)),
            start = Offset(x, 0f), end = Offset(x + 300f, 200f)
        )
    ))
}

@Composable
fun ErrorView(msg: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(top = 80.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("💥", fontSize = 56.sp)
        Text("Multiverse unstable", color = Pink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(msg, color = Color.White.copy(0.5f), fontSize = 13.sp, textAlign = TextAlign.Center)
        Button(onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Pink),
            shape = RoundedCornerShape(50)) {
            Text("Retry", fontWeight = FontWeight.Bold)
        }
    }
}
