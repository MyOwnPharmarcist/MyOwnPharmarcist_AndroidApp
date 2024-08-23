package teamyj.dev.hrd_final_project.layout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import teamyj.dev.hrd_final_project.R;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Load AlarmFragment
        if (savedInstanceState == null) {  // 이중 로드 방지
            FragmentManager fragmentManager = getSupportFragmentManager();
            AlarmFragment alarmFragment = new AlarmFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, alarmFragment, AlarmFragment.class.getSimpleName())
                    .commit();
        }
    }
}
