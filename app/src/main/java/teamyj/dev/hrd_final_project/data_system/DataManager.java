package teamyj.dev.hrd_final_project.data_system;

import static teamyj.dev.hrd_final_project.data_system.DrugListDBOpenHelper.DRUG_LIST_DB_NAME;
import static teamyj.dev.hrd_final_project.data_system.DrugListDBOpenHelper.DRUG_LIST_DB_VERSION;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.DRUG_PRODUCTS_DB_NAME;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.DRUG_PRODUCTS_DB_VERSION;
import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.EMERGENCY_DRUG_DB_NAME;
import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.EMERGENCY_DRUG_DB_VERSION;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.SALES_STORE_DB_NAME;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.SALES_STORE_DB_VERSION;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.util.concurrent.ExecutorService;

import teamyj.dev.hrd_final_project.main_system.CustomApplication;


public class DataManager {
    /** ---  싱글톤 패턴  --- */
    private static final DataManager instance = new DataManager();
    private DataManager() {}
    public static DataManager getInstance() {
        return instance;
    }

    /** ---  Fields  --- */
    private BufferedReader bufferedReader;
    private AssetManager assetManager;

    private long time;

    /** --- SQLite  --- */
    public static final String FILE_PATH = "/data/data/teamyj.dev.hrd_final_project/databases/";
    public static final int BUFFER_SIZE = 1024;

    SalesStoreDBOpenHelper salesStoreDBOpenHelper;
    DrugProductsDBOpenHelper drugProductsDBOpenHelper;
    DrugListDBOpenHelper drugListDBOpenHelper;
    EmergencyDrugDBOpenHelper emergencyDrugDBOpenHelper;

    /** ---  Methods --- */
    public void initData(AssetManager assetManager, Context context) {
        this.assetManager = assetManager;

        salesStoreDBOpenHelper = new SalesStoreDBOpenHelper(context, SALES_STORE_DB_NAME, null, SALES_STORE_DB_VERSION);
        salesStoreDBOpenHelper.loadDB();
        drugProductsDBOpenHelper = new DrugProductsDBOpenHelper(context, DRUG_PRODUCTS_DB_NAME, null, DRUG_PRODUCTS_DB_VERSION);
        drugProductsDBOpenHelper.loadDB();
        drugListDBOpenHelper = new DrugListDBOpenHelper(context, DRUG_LIST_DB_NAME, null, DRUG_LIST_DB_VERSION);
        drugListDBOpenHelper.loadDB();
    }

    public void createData(AssetManager assetManager, Context context) {
        this.assetManager = assetManager;
        CustomApplication application = CustomApplication.getInstance();
        ExecutorService excutor = application.getExcutor();

//        salesStoreDBOpenHelper = new SalesStoreDBOpenHelper(context, SALES_STORE_DB_NAME, null, SALES_STORE_DB_VERSION);
//        excutor.submit(() -> {
//            SalesStoreCreate salesStoreCreate = new SalesStoreCreate();
//            salesStoreCreate.getSalesStores(assetManager, salesStoreDBOpenHelper);
//        });

//        drugProductsDBOpenHelper = new DrugProductsDBOpenHelper(context, DRUG_PRODUCTS_DB_NAME, null, DRUG_PRODUCTS_DB_VERSION);
//        excutor.submit(() -> {
//            DrugProductsCreate drugProductsCreate = new DrugProductsCreate();
//            drugProductsCreate.getDrugProducts(assetManager, drugProductsDBOpenHelper);
//        });

//        drugListDBOpenHelper = new DrugListDBOpenHelper(context, DRUG_LIST_DB_NAME, null, DRUG_LIST_DB_VERSION);
//        excutor.submit(() -> {
//            DrugListCreate drugListCreate = new DrugListCreate();
//            drugListCreate.getDrugList(assetManager, drugListDBOpenHelper);
//        });

        emergencyDrugDBOpenHelper = new EmergencyDrugDBOpenHelper(context, EMERGENCY_DRUG_DB_NAME, null, EMERGENCY_DRUG_DB_VERSION);
        excutor.submit(() -> {
            EmergencyDrugCreate emergencyDrugCreate = new EmergencyDrugCreate();
            emergencyDrugCreate.getEmergencyDrug(assetManager, emergencyDrugDBOpenHelper);
        });
    }
}
