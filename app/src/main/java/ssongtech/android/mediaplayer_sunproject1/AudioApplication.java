package ssongtech.android.mediaplayer_sunproject1;

/** Application을 상속받는 클래스
 * onCreate 함수에서 AudioServiceInterface 객체를 생성 및 BindService할 수 있도록 구현
 */

import android.app.Application;

public class AudioApplication extends Application {
    private static AudioApplication mInstance;
    private AudioServiceInterface mInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInterface = new AudioServiceInterface(getApplicationContext());
    }

    public static AudioApplication getInstance() {
        return mInstance;
    }

    public AudioServiceInterface getServiceInterface() {
        return mInterface;
    }
}

