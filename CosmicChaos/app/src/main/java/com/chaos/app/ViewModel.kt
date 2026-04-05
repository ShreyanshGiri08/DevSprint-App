package com.chaos.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class ChaosState(
    val apod: Apod? = null,
    val pokemon: Pokemon? = null,
    val joke: Joke? = null,
    val loading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ChaosVM @Inject constructor(
    private val nasa: NasaApi,
    private val poke: PokeApi,
    private val jokes: JokeApi
) : ViewModel() {

    private val _state = MutableStateFlow(ChaosState())
    val state = _state.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _state.value = ChaosState(loading = true)
            try {
                // fire all 3 at the same time
                val apodDef   = async { nasa.getApod() }
                val pokeDef   = async { poke.getPokemon(Random.nextInt(1, 899)) }
                val jokeDef   = async { jokes.getJoke() }

                _state.value = ChaosState(
                    apod    = apodDef.await(),
                    pokemon = pokeDef.await(),
                    joke    = jokeDef.await(),
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = ChaosState(loading = false, error = e.message)
            }
        }
    }
}
