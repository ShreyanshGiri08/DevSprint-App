# 🌌 CosmicChaos — Setup (5 minutes)

## Project structure
```
app/
  App.kt         → Application + MainActivity (20 lines)
  Network.kt     → Models + Retrofit APIs + Hilt module
  ViewModel.kt   → State + parallel API fetch
  Theme.kt       → Colors, typeColor(), ChaosTheme
  UI.kt          → Entire UI: all screens + cards + animations
```

## Step 1: New Project
Android Studio → New Project → **Empty Activity**
- Package: `com.chaos.app`
- Language: Kotlin, Min SDK: 26

## Step 2: Replace gradle files
Copy `build.gradle.kts` (root), `app/build.gradle.kts`,
`settings.gradle.kts`, `gradle/libs.versions.toml` from this zip.

Then **Sync Now**.

## Step 3: Copy Kotlin files
Delete the generated `MainActivity.kt`.
Copy the 5 `.kt` files into `app/src/main/java/com/chaos/app/`.

## Step 4: Replace res files
- `app/src/main/AndroidManifest.xml`
- `app/src/main/res/values/themes.xml`

## Step 5: NASA key (optional but recommended)
In `Network.kt`, line with `"DEMO_KEY"` → replace with your key from **api.nasa.gov** (free, instant).

## Step 6: Run ▶

---

## What you get
| Feature | Where |
|---|---|
| Animated star/nebula Canvas | `StarBackground()` in UI.kt |
| Glassmorphism cards w/ glow | `GlassCard()` in UI.kt |
| NASA image + expandable text | `ApodCard()` |
| Floating Pokémon + stat bars | `PokeCard()` |
| Dad joke card | `JokeCard()` |
| Shimmer skeleton loader | `ShimmerBox()` |
| Parallel API calls | `ChaosVM.refresh()` via `async {}` |
| Chaos FAB + pulse animation | `ChaosButton()` |
| Haptic feedback | on every refresh tap |
| Error screen + retry | `ErrorView()` |

## APIs used (all free, 3 need no key)
| API | Key? |
|---|---|
| api.nasa.gov/planetary/apod | Free at api.nasa.gov |
| pokeapi.co/api/v2/pokemon/{id} | None |
| icanhazdadjoke.com | None |

## Common issues
- **Hilt crash on start** → check `android:name=".App"` in Manifest
- **Images not loading** → check INTERNET permission in Manifest
- **403 from NASA** → replace DEMO_KEY in Network.kt
