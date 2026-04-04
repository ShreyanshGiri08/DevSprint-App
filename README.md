# 🌀 API Mashup — App Wing Event

## 📌 What is API Mashup?

API Mashup is an App Wing event where you build something *unforgettable*.

You'll be given a fixed set of quirky, completely unrelated APIs and exactly **2.5 hours** to combine them into one single-screen mobile app (Android or iOS) that is creative, hilarious, and surprisingly functional.

The twist? The APIs don't go together. That's the point. Your job is to make them feel like they were *meant* to be together.

---

## 🎯 The Challenge

* Build a **single-screen mobile app** (Android or iOS) in **2.5 hours**
* Integrate **3–4 APIs** from the provided list
* Display their data together in a **creative or entertaining way**
* The combo should feel clever, not forced

---

## 🚀 Submission Guidelines

Please follow these steps carefully to ensure your project is evaluated:

1.  **Fork** this repository.
2.  **Clone** your forked repository to your local machine.
3.  Create a new **directory named after your team**.
4.  **Initialize your project** within this directory (Android Studio or Xcode project).
5.  **Showcase Video:** Record a 1–2 minute screen recording demonstrating your working project. Highlight the specific APIs and UI components you implemented.
6.  **Commit & Push:** Add your project files, App screenshots and the showcase video (`.mp4` or a hosted link in your project's README) to your directory.
7.  Open a **Pull Request (PR)** to this main repository before the **2.5-hour deadline**.

---

## 🔌 Available APIs

| Category | API | What it does | Endpoint |
| :--- | :--- | :--- | :--- |
| Humor | Kanye Rest | Random Kanye West quotes | `https://api.kanye.rest` |
| Humor | Evil Insult | Creative / brutal insults | `https://evilinsult.com/generate_insult.php?lang=en&type=json` |
| Humor | Advice Slip | Random life advice | `https://api.adviceslip.com/advice` |
| Humor | Geek Jokes | Jokes for nerds & devs | `https://geek-jokes.sameerkumar.website/api?format=json` |
| Humor | Dad Jokes | Random dad jokes *(requires Accept header)* | `https://icanhazdadjoke.com/` |
| Humor | Chuck Norris | Chuck Norris facts & jokes | `https://api.chucknorris.io/jokes/random` |
| Humor | Yes/No API | Random yes or no answer + GIF | `https://yesno.wtf/api` |
| Fandom | Rick and Morty API | Characters & episodes | `https://rickandmortyapi.com/api/character` |
| Fandom | SWAPI | Star Wars planets, ships & people | `https://swapi.dev/api/people/1/` |
| Fandom | Disney API | Character filmography | `https://api.disneyapi.dev/character` |
| Fandom | Open Trivia DB |  Trivia questions | `https://opentdb.com/api.php?amount=1` |
| Fun | Deck of Cards API | Shuffle & draw cards | `https://deckofcardsapi.com/api/deck/new/draw/?count=1` |
| Science | Spaceflight News | Real-time space news | `https://api.spaceflightnewsapi.net/v4/articles/` |
| Science | CelesTrak | Satellite / ISS tracking | `https://celestrak.org/NORAD/elements/gp.php?GROUP=stations&FORMAT=json` |
| Creative | Robohash | Unique robot avatars from text | `https://robohash.org/your-text-here` (You can write any name here) |
| Creative | DiceBear Avatars | Cute pixel & custom avatars | `https://api.dicebear.com/7.x/pixel-art/svg?seed=John` (Refer this website if you want to change the style) |
| Animals | The Cat API | Random cat images | `https://api.thecatapi.com/v1/images/search` |
| Animals | The Dog API | Random dog images | `https://api.thedogapi.com/v1/images/search` |
| Animals | Dog CEO API | Random dog images by breed | `https://dog.ceo/api/breeds/image/random` |
| Utility | Random User | Fake identity generator | `https://randomuser.me/api/` |
| Utility | Agify | Predicts age from a name | `https://api.agify.io?name=michael` (You can use another name instead of michael too) |

---

## 🏆 Judging Criteria

| Criterion | What we're looking for |
| :--- | :--- |
| **Creativity** | How originally and imaginatively are the APIs used? |
| **Number of Features** | How many meaningful interactions and features did you build? |
| **Cohesion** | Do the APIs feel naturally connected, or does it feel like a forced mashup? |
| **Code Quality** | Clean, structured code — use of loggers, a repository pattern, smart list updates, a dedicated API client, etc. |

### 💡 Bonus Points for:
* Using a **Repository pattern** to centralize all API calls (no cluttered `Activity.kt` or `ViewController.swift`)
* **DiffUtil** (Android) or **DiffableDataSource / Native SwiftUI Lists** (iOS) for smooth list updates
* Proper **logging** throughout the app
* **Error handling** and elegant loading states
* Smooth and polished UI despite the chaos

---

> ⚠️ PRs submitted after the deadline will not be considered for judging.

---
