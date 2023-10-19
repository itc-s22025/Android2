package jp.ac.it_college.std.s22025.implicit_intent_sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import jp.ac.it_college.std.s22025.implicit_intent_sample.databinding.ActivityMainBinding
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //位置情報を取得するためのライブラリ↓
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //位置情報の更新に関する設定情報が格納されている↓
    private lateinit var locationRequest: LocationRequest

    //位置情報が取得できた・変わった等位置情報に関するイベントが発生したときのリスナメソッドがで意義されている↓
    private lateinit var locationCallback: LocationCallback

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGrantedFineLocation =
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        val isGrantedCoarseLocation =
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        //どちらかの権限が許可もらえたという場合
        if(isGrantedFineLocation || isGrantedCoarseLocation){
            requestLocationUpdates()
            return@registerForActivityResult
        }
        //結局権限の許可をもらえなかったとき とりあえずログだけだしとく
        Log.w("CHAPTER14", "許可がもらえなかったのでいじけました;-(")
    }

    private var latitude = 0.0
    private var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btMapSearch.setOnClickListener(::onMapSearchButton)
        binding.btMapShowCurrent.setOnClickListener(::onMapShowCurrentButtonClick)

        //位置情報取得関連
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 5000
        ).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                //lastLocation: 最新の位置情報
                result.lastLocation?.also { location ->
                    //緯度経度取る
                    latitude = location.latitude
                    longitude = location.longitude

                    //緯度経度を表示する
                    binding.tvLatitude.text = latitude.toString()
                    binding.tvLongitude.text = longitude.toString()
                }
            }
        }

    }

    //取得開始のリクエストを投げる↓
    override fun onResume() {
        super.onResume()
        requestLocationUpdates()
    }

    //リソースの開放
    override fun onPause() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onPause()
    }

    private fun onMapSearchButton(view: View) {
        //パーセントエンコーディングしてね: URLEncoder
        val searchWord = URLEncoder.encode(
            //"utf-8"で指定しないとパーセントエンコーディングの結果が変わってしまうのでちゃんと指定する
            binding.etSearcWord.text.toString(), "UTF-8"
        )
        //geo: のとこが地図に関するURIだよーて判定になる(webだとhttps)　0,0は緯度経度　?q= で検索する言葉を指定
        val uri = Uri.parse("geo:0,0?q=${searchWord}")
        //action_view...とりあえずなにか渡すからそれを表示してね -> uriみてね
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun onMapShowCurrentButtonClick(view: View) {
        val uri = Uri.parse("geo:${latitude},${longitude}")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun requestLocationUpdates(){
        //true or false
        val isGrantedFineLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        //true or false
        val isGrantedCoarseLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        //位置情報取得の権限↑のうち、どちらか一方でも権限あれば(=trueなら)OKなので位置情報取得開始
        if (isGrantedFineLocation || isGrantedCoarseLocation){
            //権限チェック処理入れないと赤線が消えない
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
            return
        }
        //ここまで来たらどの権限も無いのでリクエスト
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

}