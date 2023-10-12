package jp.ac.it_college.std.s22025.mediasample

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import jp.ac.it_college.std.s22025.mediasample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer()
        val mediaFileUri = Uri.parse("android.resource://${packageName}/${R.raw.sound_of_rain}")
        mediaPlayer?.apply {
            setDataSource(applicationContext, mediaFileUri)
            setOnPreparedListener(::mediaPlayerOnPrepared)
            //データの再生終わったときの通知くれる
            setOnCompletionListener(::mediaPlayerOnCompletion)
            //圧縮されたファイルの伸長に時間かかるから　それを非同期で処理してくれる↓
            prepareAsync()
        }

        binding.btPlay.setOnClickListener(::onPlayButtonClick)
        binding.btBack.setOnClickListener(::onBackButtonClick)
        binding.btForward.setOnClickListener(::onForwardButtonClick)
        binding.swLoop.setOnCheckedChangeListener(::onLoopSwitchChanged)
    }

    private fun mediaPlayerOnPrepared(mediaPlayer: MediaPlayer) {
        binding.btPlay.isEnabled = true
        binding.btBack.isEnabled = true
        binding.btForward.isEnabled = true
    }

    private fun mediaPlayerOnCompletion(mediaPlayer: MediaPlayer) {
        //再生ボタンは再生中だと一時停止表示になったりするのでそれのあれそれ
        binding.btPlay.setText(R.string.bt_play_play)
    }

    private fun onPlayButtonClick(view: View) {
        //再生ボタンは再生中だと一時停止表示になったりするのでそれのあれそれ
        mediaPlayer?.run {
            //再生中のときにonPlayButtonClick実行なら
            if (isPlaying) {
                //一時停止
                pause()
                //テキストを「再生」にする
                binding.btPlay.setText(R.string.bt_play_play)
            } else {
                //停止中なら再生
                start()
                //テキストを「一時停止」にする
                binding.btPlay.setText(R.string.bt_play_pause)
            }
        }
    }

    override fun onStop() {
        //mediaPlayerがnullじゃなかったら
        mediaPlayer?.run {
            if (isPlaying) {
                start()
            }
            //リソースを開放する
            release()
        }
        super.onStop()
    }

    //ファイルの先頭に強制的に戻す
    private fun onBackButtonClick(view: View) {
        mediaPlayer?.seekTo(0)
    }

    private fun onForwardButtonClick(view: View){
        //一旦再生終了する
        mediaPlayer?.run {
            seekTo(duration)
            if (!isPlaying){
                binding.btPlay.setText(R.string.bt_play_pause)
                start()
            }
        }
    }

    private fun onLoopSwitchChanged(buttonView: CompoundButton, isChecked: Boolean){
        mediaPlayer?.isLooping = isChecked
    }
}