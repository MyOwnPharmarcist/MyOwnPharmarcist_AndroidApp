package teamyj.dev.hrd_final_project.main_system;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomApplication extends Application {
    /** --- 어플리케이션 인스턴스 --- */
    private static CustomApplication instance;
    public static CustomApplication getInstance() {
        return instance;
    }

    /** --- Fields  --- */
    private ExecutorService excutor = Executors.newFixedThreadPool(16);

    /** --- Getter and Setter   --- */
    public ExecutorService getExcutor() {
        return excutor;
    }

    /** --- Methods --- */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = CustomApplication.this;

//        DataManager dataManager = DataManager.getInstance();
//        dataManager.createData(getAssets(), this);
    }
}
