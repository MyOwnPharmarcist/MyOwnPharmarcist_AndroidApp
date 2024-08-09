package teamyj.dev.hrd_final_project.layout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.data_system.DataManager;

public class MainMenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SearchDrugsFragment searchDrugsFragment = new SearchDrugsFragment();
    private SearchPharmacyFragment searchPharmacyFragment = new SearchPharmacyFragment();
    private TimerFragment timerFragment = new TimerFragment();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        // 위치 권한 체크 메서드
        checkLocationPermission();
    }

    // 위치 권한 체크 메서드
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 있을 경우 앱 실행
            startApp();
        }
    }

    private void startApp() {
        // 앱의 나머지 초기화 작업 수행
        // Fragment 로드 등
        // main view 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, searchPharmacyFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectListener());

        dataManager = DataManager.getInstance();
        dataManager.initData(getAssets(), this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되었을 때 앱 실행
                startApp();
            } else {
                // 권한이 거부된 경우 사용자에게 알림 후 앱 종료
                showPermissionDeniedDialog();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("권한 필요")
                .setMessage("이 앱은 GPS 권한이 필요합니다. 권한을 부여하지 않으면 앱이 종료됩니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 앱 종료
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
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