package com.chaos.app

import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Named
import javax.inject.Singleton

// ── Models ────────────────────────────────────────────────────

data class Apod(
    val title: String,
    val explanation: String,
    val url: String,
    val hdurl: String?,
    @SerializedName("media_type") val mediaType: String,
    val date: String
)

data class Pokemon(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<Stat>,
    val weight: Int // hectograms
) {
    val imageUrl get() = sprites.other?.artwork?.front
        ?: sprites.frontDefault ?: ""
    val primaryType get() = types.firstOrNull()?.type?.name ?: "normal"
    fun stat(n: String) = stats.firstOrNull { it.stat.name == n }?.base ?: 0
}

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?,
    val other: OtherSprites?
)
data class OtherSprites(
    @SerializedName("official-artwork") val artwork: Artwork?
)
data class Artwork(@SerializedName("front_default") val front: String?)
data class TypeSlot(val type: TypeInfo)
data class TypeInfo(val name: String)
data class Stat(@SerializedName("base_stat") val base: Int, val stat: StatInfo)
data class StatInfo(val name: String)

data class Joke(val joke: String, val id: String)

// ── API interfaces ────────────────────────────────────────────

interface NasaApi {
    @GET("planetary/apod")
    suspend fun getApod(@Query("api_key") key: String = "DEMO_KEY"): Apod
    // 💡 Replace DEMO_KEY with your key from api.nasa.gov
}

interface PokeApi {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): Pokemon
}

interface JokeApi {
    @Headers("Accept: application/json")
    @GET(".")
    suspend fun getJoke(): Joke
}

// ── Hilt module ───────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    // one shared client
    @Provides @Singleton
    fun client() = OkHttpClient.Builder().build()

    private fun retrofit(client: OkHttpClient, url: String) =
        Retrofit.Builder().baseUrl(url).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton
    fun nasaApi(client: OkHttpClient) =
        retrofit(client, "https://api.nasa.gov/").create(NasaApi::class.java)

    @Provides @Singleton
    fun pokeApi(client: OkHttpClient) =
        retrofit(client, "https://pokeapi.co/api/v2/").create(PokeApi::class.java)

    @Provides @Singleton
    fun jokeApi(client: OkHttpClient) =
        retrofit(client, "https://icanhazdadjoke.com/").create(JokeApi::class.java)
}
