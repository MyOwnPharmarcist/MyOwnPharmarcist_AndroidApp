package teamyj.dev.hrd_final_project.layout;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import teamyj.dev.hrd_final_project.R;

public class AlarmActivity extends AppCompatActivity {

    // AlarmActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            AlarmFragment alarmFragment = new AlarmFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, alarmFragment, AlarmFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 이미 활성화된 알람 화면에서 중복으로 인스턴스를 생성하지 않도록 처리
        if (getSupportFragmentManager().findFragmentByTag(AlarmFragment.class.getSimpleName()) == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            AlarmFragment alarmFragment = new AlarmFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, alarmFragment, AlarmFragment.class.getSimpleName())
                    .commit();
        }
    }
}