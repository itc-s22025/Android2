package jp.ac.it_college.std.s22025.asynccoroutinesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s22025.asynccoroutinesample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

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
                receiveWeatherInfo(it.q)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    @UiThread
    private fun receiveWeatherInfo(q: String) {
        lifecycleScope.launch {
            val url = "$WEATHER_INFO_URL&q=$q&appid=$APP_ID"
            val result = weatherIntoBackgroundRunner(url)
            showWeatherInfo(result)
        }
    }

    @WorkerThread
    private suspend fun weatherIntoBackgroundRunner(urlString: String): String {
        return withContext(Dispatchers.IO) {
            val url = URL(urlString)
            val con = url.openConnection() as HttpURLConnection
            con.apply {

                connectTimeout = 1000
                readTimeout = 1000
                requestMethod = "GET"
            }
            try {
                con.connect()
                val result = con.inputStream.reader().readText()
                con.disconnect()
                result
            } catch (ex: SocketTimeoutException) {
                Log.w("DEBUG_TAG", "通信タイムアウト", ex)
                ""
            }
        }
    }

        @UiThread
        private fun showWeatherInfo(result: String) {
            //全体はObject型だからJSONObject
            val root = JSONObject(result)
            //都市名はString　()内部はKey名　->"name"の値をCityNameに
            val cityName = root.getString("name")
            //coordもObject型
            val coord = root.getJSONObject("coord")
            //緯度経度 keyがletとlonのとこ
            val latitude = coord.getDouble("lat")
            val longitude = coord.getDouble("lon")
            //weatherのとこはArray型
            val weatherArray = root.getJSONArray("weather")
            //weather内はObject型　だけどなんかインデックスで指定するらしい Array内のObjectだから？
            val current = weatherArray.getJSONObject(0)
            val weather = current.getString("description")
            binding.tvWeatherTelop.text = getString(R.string.tv_telop, cityName)
            binding.tvWeatherDesc.text = getString(R.string.tv_desc, weather, latitude, longitude)

        }

    }