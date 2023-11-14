package jp.ac.it_college.std.s22025.pokeapitest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeInfo(
    @SerialName("name")
    val name: String
)