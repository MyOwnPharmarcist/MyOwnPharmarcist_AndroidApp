package teamyj.dev.hrd_final_project.data_system;

import android.content.Context;
import android.content.res.AssetManager;

import java.util.concurrent.ExecutorService;

import teamyj.dev.hrd_final_project.Interface.ApplicationGettable;
import teamyj.dev.hrd_final_project.Interface.DBHelperGettable;
import teamyj.dev.hrd_final_project.Interface.DBWritable;
import teamyj.dev.hrd_final_project.Interface.FileLoadable;


public class DataManager implements FileLoadable {

    /** ---  Fields  --- */
    private AssetManager assetManager;

    private long time;

    /** --- SQLite  --- */
    public static final String FILE_PATH = "/data/data/teamyj.dev.hrd_final_project/databases/";
    public static final int BUFFER_SIZE = 1024;

    /** ---  Methods --- */
    // 만들어진 데이터베이스를 디바이스에 복사
    @Override
    public void loadFile(Context context) {
        new SalesStoreDBOpenHelper(context).loadDB();
        new DrugProductsDBOpenHelper(context).loadDB();
        new DrugListDBOpenHelper(context).loadDB();
        new EmergencyDrugDBOpenHelper(context).loadDB();
    }

    // csv 파일을 통해 데이터베이스 파일 생성
    public void createData(ApplicationGettable application, DBHelperGettable dbHelper, Context context) {
        this.assetManager = application.getAssetManager();
        ExecutorService excutor = application.getExecutor();

        DBWritable sales_store = new SalesStoreDBOpenHelper(context);
        excutor.submit(() -> {
            new SalesStoreCreate().create(assetManager, sales_store);;
        });

        DBWritable drug_products = new DrugProductsDBOpenHelper(context);
        excutor.submit(() -> {
            new DrugProductsCreate().create(assetManager, drug_products);
        });

        DBWritable drug_list = new DrugListDBOpenHelper(context);
        excutor.submit(() -> {
            new DrugListCreate().create(assetManager, drug_list);
        });

        DBWritable emergency_drug = new EmergencyDrugDBOpenHelper(context);
        excutor.submit(() -> {
            new EmergencyDrugCreate().create(assetManager, emergency_drug);;
        });
    }
}
