# 🌌 Cosmic Chaos

> NASA astronomy pictures. A random Pokémon. A terrible dad joke. All at once. Because why not.

Cosmic Chaos is a single-screen Android app that brings together three completely unrelated APIs into one beautifully chaotic experience. Hit the button, get something new from outer space, the Pokemon,space news and  dad jokes

##APK Link##: https://drive.google.com/file/d/1ObO0mttTna_6e3DfnfzN1LW50HvpaXnc/view?usp=sharing


##Why are the API's well related despite being weirdly different?##
   
Meet Leo, a 10-year-old kid who just got grounded for failing his science test on space. As punishment, his dad makes him use a boring old space news app to "learn something." But the app is broken — every time a space news article loads, it glitches and spits out a random Pokemon alongside it. Leo starts noticing the Pokemon actually match the news perfectly. A story about a meteor shower? Jolteon shows up. A black hole discovery? Gengar. A Mars dust storm? Sandslash. He becomes obsessed, convinced the universe is sending him coded messages through Pokémon. And every time he figures out a "match," his dad walks in, reads the headline, and responds with the most painfully unfunny dad joke about space. Leo groans every single time — but secretly, his dad's dumb jokes are the only reason he keeps reading the articles

## UI & Animations

The whole app is built in **Jetpack Compose** with a dark space theme. A few things worth noting:

-**Animated star background** 
- **Glassmorphism cards** —
- **Shimmer loading skeletons** 
- **Stat bar animation** — 
- **CHAOS FAB** — the floating action button at the bottom pulses while loading. Tapping it triggers haptic feedback and fires a fresh set of API calls.
- **Error state** 
---

## Tech Stack

| Layer | What's Used |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 | |
| Image loading | Coil (`AsyncImage`, `SubcomposeAsyncImage`) |
| Concurrency | Kotlin Coroutines (`async`/`await`) |

---

## APIs Used

| API | What For | Auth |
|---|---|---|
| [Spaceflight News](    https://api.spaceflightnewsapi.net/v4/articles/) | Astronomy Picture of the Day |
| [PokéAPI](https://pokeapi.co/) | Pokémon data & sprites | None required |
| [icanhazdadjoke](https://icanhazdadjoke.com/) | Dad jokes | None required |



---

## Getting Started

1. Clone the repo
2. Open in Android Studio
3. Run on a device or emulator (API 26+)
4. Tap **CHAOS**

---



*Built for the joy of it. Three APIs walk into a bar...*
