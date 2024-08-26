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

import teamyj.dev.hrd_final_project.Interface.ApplicationGettable;
import teamyj.dev.hrd_final_project.Interface.DBWritable;
import teamyj.dev.hrd_final_project.Interface.FileLoadable;


public class DataManager implements FileLoadable {

    /** ---  Fields  --- */
    private BufferedReader bufferedReader;
    private AssetManager assetManager;
    private ApplicationGettable applicaion;

    private long time;

    /** --- SQLite  --- */
    public static final String FILE_PATH = "/data/data/teamyj.dev.hrd_final_project/databases/";
    public static final int BUFFER_SIZE = 1024;

    /** ---  Methods --- */
    public DataManager(ApplicationGettable application) {
        this.applicaion = application;
    }

    @Override
    public void loadFile(Context context) {
        new SalesStoreDBOpenHelper(context, SALES_STORE_DB_NAME, null, SALES_STORE_DB_VERSION).loadDB();
        new DrugProductsDBOpenHelper(context, DRUG_PRODUCTS_DB_NAME, null, DRUG_PRODUCTS_DB_VERSION).loadDB();
        new DrugListDBOpenHelper(context, DRUG_LIST_DB_NAME, null, DRUG_LIST_DB_VERSION).loadDB();
        new EmergencyDrugDBOpenHelper(context, EMERGENCY_DRUG_DB_NAME, null, EMERGENCY_DRUG_DB_VERSION).loadDB();
    }

    public void createData(AssetManager assetManager, Context context) {
        this.assetManager = assetManager;
        ExecutorService excutor = applicaion.getExecutor();

        DBWritable sales_store = new SalesStoreDBOpenHelper(context, SALES_STORE_DB_NAME, null, SALES_STORE_DB_VERSION);
        excutor.submit(() -> {
            new SalesStoreCreate().create(assetManager, sales_store);;
        });

        DBWritable drug_products = new DrugProductsDBOpenHelper(context, DRUG_PRODUCTS_DB_NAME, null, DRUG_PRODUCTS_DB_VERSION);
        excutor.submit(() -> {
            new DrugProductsCreate().create(assetManager, drug_products);
        });

        DBWritable drug_list = new DrugListDBOpenHelper(context, DRUG_LIST_DB_NAME, null, DRUG_LIST_DB_VERSION);
        excutor.submit(() -> {
            new DrugListCreate().create(assetManager, drug_list);
        });

        DBWritable emergency_drug = new EmergencyDrugDBOpenHelper(context, EMERGENCY_DRUG_DB_NAME, null, EMERGENCY_DRUG_DB_VERSION);
        excutor.submit(() -> {
            new EmergencyDrugCreate().create(assetManager, emergency_drug);;
        });
    }
}
