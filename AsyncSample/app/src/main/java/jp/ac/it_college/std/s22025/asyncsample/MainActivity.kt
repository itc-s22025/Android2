package jp.ac.it_college.std.s22025.asyncsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s22025.asyncsample.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEBUG_TUG = "AsyncSample"
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
        val url = "$WEATHER_INFO_URL&q=$q&appid=$APP_ID"
        val executorService = Executors.newSingleThreadExecutor()
        val backgroundReceiver = WeatherInfoBackgroundReceiver(url)
        val future = executorService.submit(backgroundReceiver)
        val result = future.get()
        showWeatherInfo(result)
    }

    @UiThread
    private fun showWeatherInfo(result: String){
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


    private class WeatherInfoBackgroundReceiver(val urlString: String) : Callable<String> {
        @WorkerThread
        override fun call(): String {
            val url = URL(urlString)

            //urlはプロトコル毎に機能？が違うが、URLクラスは直接文字列を見ないとどのプロトコルか判断できない
            // openConnection->URLコネクションというプロトコル型に統一する
            //その中のHttpのURLコネクション
            val con = url.openConnection() as HttpURLConnection
            con.apply {
                connectTimeout = 1000
                readTimeout = 1000
                requestMethod = "GET"
            }

            return try {
                con.connect()
                val result = con.inputStream.reader().readText()
                //InputStream.reader()で自動的にUTF-8であるとしてByteをStringに変換して読み取るInputStreamReaderを作ってくれる
                //更にInputStreamReader.readText()を呼び出せば、BufferedReader(改行基準で読み取る)を使って一行ずつ読み取ってすべて一つの文字列に組み立てる面倒な工程を全て肩代わりしてくれる
                //(InputStream.reader->一行ずつしか読み取れない)
                con.disconnect()
                result
                //★これだけだとINTERNETのパーミッションエラーが出るので、manifests>AndroidManifests.xml内にuses-permissionタグを記載する
            } catch (ex: SocketTimeoutException) {
                Log.w(DEBUG_TUG, "通常タイムアウト", ex)
                ""
            }
        }
    }
}