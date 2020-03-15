package ssongtech.android.mediaplayer_sunproject1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private ArrayList<SongInfo> _songs = new ArrayList<SongInfo>();
    private Context context;

    private OnitemClickListener onItemClickListener;

    public SongAdapter(Context context, ArrayList<SongInfo> _songs) {
        this.context = context;
        this._songs = _songs;
    }

    public interface OnitemClickListener{
        void onItemClick(Button b, View v, SongInfo obj, int position);
    }

    public void setOnItemClickListener(final OnitemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.song_item, viewGroup, false);
        return new SongHolder(myview);
    }

    @Override
    public void onBindViewHolder(final SongHolder songHolder, final int position) {
        AudioItem audioItem = AudioItem.bindCursor(_songs, position);
        ((SongHolder) songHolder).setAudioItem(audioItem, position);

        final SongInfo c = _songs.get(position);
        songHolder.tvSongName.setText(c.SongName);
        songHolder.tvArtistName.setText(c.ArtistName);
        songHolder.btnAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(songHolder.btnAction, v, c, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public static class AudioItem {
        public String SongName; // 타이틀 정보
        public String ArtistName; // 아티스트 정보
        public String SongUrl; // 실제 데이터위치

        public static AudioItem bindCursor(ArrayList<SongInfo> _songs, int position) {
            AudioItem audioItem = new AudioItem();
            audioItem.SongName = _songs.get(position).getSongName().toString();
            audioItem.ArtistName = _songs.get(position).getArtistName().toString();
            audioItem.SongUrl = _songs.get(position).getSongUrl().toString();

            return audioItem;
        }
    }

    public ArrayList<Long> getAudioIds() {
        int count = getItemCount();
        ArrayList<Long> audioIds = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            audioIds.add(getItemId(i));
        }
        return audioIds;
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView tvSongName, tvArtistName;
        Button btnAction;
        private AudioItem mItem;
        private int mPosition;

        public SongHolder(View itemView) {
            super(itemView);

            tvSongName = (TextView) itemView.findViewById(R.id.tvSongName);
            tvArtistName = (TextView) itemView.findViewById(R.id.tvArtistName);
            btnAction = (Button) itemView.findViewById(R.id.btnAction);

            // 리스트 목록 클릭시 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioApplication.getInstance().getServiceInterface().setPlayList(getAudioIds()); // 재생목록등록
                    AudioApplication.getInstance().getServiceInterface().play(mPosition); // 선택한 오디오재생
                }
            });
        }

        // 여러곡을 재생할 수 있도록 AudioItem과 position을 set시킨다.
        public void setAudioItem(AudioItem item, int position) {
            mItem = item;
            mPosition = position;
        }
    }
}

