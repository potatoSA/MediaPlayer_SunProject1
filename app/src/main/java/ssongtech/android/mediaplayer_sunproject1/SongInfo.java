package ssongtech.android.mediaplayer_sunproject1;

public class SongInfo {
    public String SongName, ArtistName, SongUrl;

    public SongInfo() {

    }

    public SongInfo(String songName, String artistName, String songUrl) {
        this.SongName = songName;
        this.ArtistName = artistName;
        this.SongUrl = songUrl;
    }

    public String getSongName() {
        return SongName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public String getSongUrl() {
        return SongUrl;
    }

}
