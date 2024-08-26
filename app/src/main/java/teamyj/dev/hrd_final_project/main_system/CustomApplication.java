package teamyj.dev.hrd_final_project.main_system;

import android.app.Application;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import teamyj.dev.hrd_final_project.Interface.ApplicationGettable;
import teamyj.dev.hrd_final_project.data_system.DataManager;

public class CustomApplication extends Application implements ApplicationGettable {
    /** --- 어플리케이션 인스턴스 --- */
    private static CustomApplication instance;
    public static CustomApplication getInstance() {
        return instance;
    }

    /** --- Fields  --- */
    private ExecutorService excutor = Executors.newFixedThreadPool(16);

    /** --- Getter and Setter   --- */
    @Override
    public ExecutorService getExecutor() {
        return excutor;
    }

    @Override
    public AssetManager getAssetManager() {
        return getAssets();
    }

    /** --- Methods --- */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = CustomApplication.this;

        new DataManager().loadFile(this);

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
