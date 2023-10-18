package jp.ac.it_college.std.s22025.media3servicesample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

//専用のクラスMediaSessionServiceを継承する必要がある
class MediaPlayerbackService : MediaSessionService() {
    //クライアント(MediaController)と連携するためのコンポーネント
    private var mediaSession: MediaSession? = null
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    //ExoPlayerに設定するイベントリスナ
    //inner classで定義せずにobject式を使って無名クラスとして作る
    private val playerListener = object : Player.Listener {
        //プレイヤーの再生状態が変化したときに呼ばれるリスナ
        override fun onPlaybackStateChanged(playbackState: Int) {
            //今回は再生終了(STATE_ENDED)だけ実装
            when (playbackState){
                //再生完了
                Player.STATE_ENDED -> this@MediaPlayerbackService.stopSelf()
                //バッファリング中
                Player.STATE_BUFFERING -> {}
                //アイドル(待機状態)
                Player.STATE_IDLE -> {}
                //再生準備完了
                Player.STATE_READY -> {}
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        //プレイヤー本体となるExoPlayerをつくる
        val player = ExoPlayer.Builder(this).build()
        //さっき作ったイベントリスナをセット
        player.addListener(playerListener)

        //作ったExoPlayerをもとにMediaSessionをつくる
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onDestroy() {
        mediaSession?.run {
            //ExoPlayerのリソースの開放
            player.release()

            //MediaSessionそのもののリソース開放
            release()
        }
        //MediaSessionを破棄
        mediaSession = null
        super.onDestroy()
    }
}