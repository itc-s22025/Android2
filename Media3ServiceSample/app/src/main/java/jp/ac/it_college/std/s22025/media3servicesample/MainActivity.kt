package jp.ac.it_college.std.s22025.media3servicesample

import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import jp.ac.it_college.std.s22025.media3servicesample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //MediaControllerのインスタンスを管理するオブジェクト
    private lateinit var controllerFuture: ListenableFuture<MediaController>

    //MediaControllerを使う処理を簡素化するための工夫
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.play.setOnClickListener { playSample() }
        binding.pause.setOnClickListener { pauseSample() }
    }

    override fun onStart() {
        super.onStart()
        controllerFuture = MediaController.Builder(
            this,
            SessionToken(this, ComponentName(this, MediaPlayerbackService::class.java))
        ).buildAsync()
    }

    override fun onStop() {
        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }

    private fun playSample(){
        val controller = this.controller ?: return

        //再生したいデータを指定
        controller.setMediaItem(
            MediaItem.fromUri("android.resource://${packageName}/${R.raw.sound_of_rain}")
        )
        // 指定した番号のデータに切り替えつつ、デフォルト(多分先頭)に再生位置をセット
        controller.seekToDefaultPosition(0)
        //データの再生を指示
        controller.play()
    }

    private fun pauseSample(){
        val controller = this.controller ?: return
        //データの再生を停止
        controller.stop()
    }
}