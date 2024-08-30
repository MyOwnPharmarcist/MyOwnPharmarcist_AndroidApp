package teamyj.dev.hrd_final_project.main_system;

import android.app.Application;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import teamyj.dev.hrd_final_project.Interface.ApplicationGettable;
import teamyj.dev.hrd_final_project.Interface.DBHelperGettable;
import teamyj.dev.hrd_final_project.Interface.DetailDataGettable;
import teamyj.dev.hrd_final_project.Interface.ListDataGettable;
import teamyj.dev.hrd_final_project.Interface.LocationDataGettable;
import teamyj.dev.hrd_final_project.Interface.TimerSavable;
import teamyj.dev.hrd_final_project.data_system.DataManager;
import teamyj.dev.hrd_final_project.data_system.DrugListDBOpenHelper;
import teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper;
import teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper;
import teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper;

public class CustomApplication extends Application implements ApplicationGettable, DBHelperGettable, TimerSavable {
    /** --- 어플리케이션 인스턴스 --- */
    private static CustomApplication instance;
    public static CustomApplication getInstance() {
        return instance;
    }

    /** --- Fields  --- */
    private ExecutorService excutor = Executors.newFixedThreadPool(16);

    private ListDataGettable drug_list;
    private DetailDataGettable drug_products;
    private LocationDataGettable emergency_drug;
    private LocationDataGettable sales_store;

    private long timerValue = 100000;

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

        drug_list = new DrugListDBOpenHelper(this);
        drug_products = new DrugProductsDBOpenHelper(this);
        emergency_drug = new EmergencyDrugDBOpenHelper(this);
        sales_store = new SalesStoreDBOpenHelper(this);

        new DataManager().loadFile(this);

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public ListDataGettable getList() {
        return drug_list;
    }

    @Override
    public DetailDataGettable getProducts() {
        return drug_products;
    }

    @Override
    public LocationDataGettable getEmergency() {
        return emergency_drug;
    }

    @Override
    public LocationDataGettable getSales() {
        return sales_store;
    }

    @Override
    public void setTimer(long time) {
        timerValue = time;
    }

    @Override
    public long getTimer() {
        return timerValue;
    }
}
