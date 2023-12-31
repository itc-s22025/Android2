package jp.ac.it_college.std.s22025.ktorsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import jp.ac.it_college.std.s22025.ktorsample.databinding.ActivityMainBinding
import jp.ac.it_college.std.s22025.ktorsample.model.WeatherInfo
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private val ktorClient = HttpClient(CIO) {
    //engineを指定->CIO
    engine {
        endpoint {
            connectTimeout = 1000
            requestTimeout = 1000
            socketTimeout = 1000
        }
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            }
        )
    }
}


class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEBUG_TUG = "AsyncCoroutineSample"
        private const val WEATHER_INFO_URL =
            "https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID = BuildConfig.APP_ID
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCityList.apply {
            adapter = CityAdapter {
                getWeatherInfo(it.q)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    @UiThread
    private fun getWeatherInfo(q: String) {
        lifecycleScope.launch {
            //データ取得部分
            val url = "$WEATHER_INFO_URL&q=$q&appid=$APP_ID"
            val result = ktorClient.get {
                url(url)
            }.body<WeatherInfo>()

            //取得したデータをUIに反映
            result.run {
                binding.tvWeatherTelop.text = getString(R.string.tv_telop, result.cityName)
                binding.tvWeatherDesc.text = getString(R.string.tv_desc, weather[0].description, coordinates.longitude, coordinates.latitude)
            }

        }
    }
}