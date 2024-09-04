package teamyj.dev.hrd_final_project.layout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import teamyj.dev.hrd_final_project.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 2000);
        // postDelayed 메서드를 통해 3초 후에 SplashHandler가 작동함
    }

    private class splashHandler implements Runnable {
        @Override
        public void run() {
            startActivity(new Intent(getApplication(), MainMenuActivity.class));
            SplashActivity.this.finish();
        }
    }
}