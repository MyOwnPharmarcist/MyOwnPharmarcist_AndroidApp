package teamyj.dev.hrd_final_project.layout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import teamyj.dev.hrd_final_project.R;

public class MainMenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SearchDrugsFragment searchDrugsFragment = new SearchDrugsFragment();
    private SearchPharmacyFragment searchPharmacyFragment = new SearchPharmacyFragment();
    private TimerFragment timerFragment = new TimerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        // main view 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, searchPharmacyFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectListener());
    }

    private class ItemSelectListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            int itemId = menuItem.getItemId();
            if (itemId == R.id.navigation_search_pharmacy) {
                transaction.replace(R.id.menu_frame_layout, searchPharmacyFragment).commitAllowingStateLoss();
            } else if (itemId == R.id.navigation_search_drugs) {
                transaction.replace(R.id.menu_frame_layout, searchDrugsFragment).commitAllowingStateLoss();
            } else if (itemId == R.id.navigation_timer) {
                transaction.replace(R.id.menu_frame_layout, timerFragment).commitAllowingStateLoss();
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this); // app 종료
    }
}