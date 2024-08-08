package teamyj.dev.hrd_final_project.layout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.data_system.DataManager;

public class MainActivity extends AppCompatActivity {
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = DataManager.getInstance();
        dataManager.initData(getAssets(), this);
    }
}