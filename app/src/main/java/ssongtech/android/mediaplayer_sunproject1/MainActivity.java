package ssongtech.android.mediaplayer_sunproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public ArrayList<SongInfo> _songs = new ArrayList<SongInfo>();
    RecyclerView recyclerView;
    SongAdapter songAdapter;

    public static Context Main_Context;

    TextView mTextTitle;
    ImageButton mBtnPlayPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Main_Context = this;

        CheckPermission();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        SongInfo songInfo = new SongInfo("부산 바캉스", "Sun", "http://192.168.0.101/news/MS_insert/Markvard_Forever.mp3");
        SongInfo songInfo1 = new SongInfo("Colada", "LiQWYD", "http://192.168.0.101/news/MS_insert/LiQWYD_Pina%20Colada.mp3");
        SongInfo songInfo2 = new SongInfo("Markvard_Forever", "Gluschenko", "http://192.168.0.101/news/MS_insert/Markvard_Forever.mp3");
        SongInfo songInfo3 = new SongInfo("Gluschenko_Overseas", "Sia", "http://192.168.0.101/news/MS_insert/Vlad%20Gluschenko_Overseas.mp3");
        SongInfo songInfo4 = new SongInfo("Jazz in Paris", "Gong", "http://192.168.0.101/news/MS_insert/LiQWYD_Pina%20Colada.mp3");

        _songs.add(songInfo);
        _songs.add(songInfo1);
        _songs.add(songInfo2);
        _songs.add(songInfo3);
        _songs.add(songInfo4);

        songAdapter = new SongAdapter(this, _songs);
        recyclerView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        songAdapter.setOnItemClickListener(new SongAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(final Button b, View v, final SongInfo obj, int position) {

                if (b.getText().toString().equals("Play")) {
                    b.setText("Stop");
                }else {
                    onDestroy();
                    b.setText("Play");
                }
            }
        });

        mTextTitle = (TextView) findViewById(R.id.txt_title);
        mBtnPlayPause = (ImageButton) findViewById(R.id.btn_play_pause);
        findViewById(R.id.lin_miniplayer).setOnClickListener(this);
        findViewById(R.id.btn_rewind).setOnClickListener(this);
        mBtnPlayPause.setOnClickListener(this);
        findViewById(R.id.btn_forward).setOnClickListener(this);

        registerBroadcast();
        updateUI();
    }

    // 해제
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }


    // 적절한 타이밍에 등록/해제 될 수 있도록 BroadcastReceiver를 생성하고 구현
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    public void registerBroadcast(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastActions.PLAY_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);
    }

    public void unregisterBroadcast(){
        unregisterReceiver(mBroadcastReceiver);
    }

    // UI 업데이트
    private void updateUI() {
        if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
            mBtnPlayPause.setImageResource(R.drawable.pause);
        } else {
            mBtnPlayPause.setImageResource(R.drawable.play);
        }
        SongAdapter.AudioItem audioItem = AudioApplication.getInstance().getServiceInterface().getAudioItem();
        if (audioItem != null) {
            mTextTitle.setText(audioItem.SongName);
        } else {
            mTextTitle.setText("재생중인 음악이 없습니다.");
        }
    }


    /**************************** 권한 허용 ****************************/
    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        }else {
            //loadSongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //loadSongs();
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    CheckPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_miniplayer:
                // 플레이어 화면으로 이동할 코드가 들어갈 예정
                break;
            case R.id.btn_rewind:
                // 이전곡으로 이동
                AudioApplication.getInstance().getServiceInterface().rewind();
                break;
            case R.id.btn_play_pause:
                // 재생 또는 일시정지
                AudioApplication.getInstance().getServiceInterface().togglePlay();
                break;
            case R.id.btn_forward:
                // 다음곡으로 이동
                AudioApplication.getInstance().getServiceInterface().forward();
                break;
        }
    }
    /**************************** 권한 허용 ****************************/
}
