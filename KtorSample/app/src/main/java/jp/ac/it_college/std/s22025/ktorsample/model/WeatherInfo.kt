package jp.ac.it_college.std.s22025.ktorsample.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    //APIからとってくる値は＠SerialName
    @SerialName("coord")
    //SerialNameとプロパティ名が違う↓
    val coordinates: Coordinates,

    val weather: List<Weather>,

    @SerialName("name")
    val cityName: String,
)

@Serializable
data class Coordinates(
    //このなかにlonとか入る
    @SerialName("lon")
    val longitude: Double,

    @SerialName("lat")
    val latitude: Double,
)

@Serializable
data class Weather(
    val id: Int,

    @SerialName("main")
    val groupName: String,

    val description: String,

    val icon: String,
)
