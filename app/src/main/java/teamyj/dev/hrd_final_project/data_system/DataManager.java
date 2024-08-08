package teamyj.dev.hrd_final_project.data_system;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataManager {
    /** ---  싱글톤 패턴  --- */
    private static final DataManager instance = new DataManager();
    private DataManager() {}
    public static DataManager getInstance() {
        return instance;
    }

    /** ---  Fields  --- */
    private ExtractData extractData = new ExtractData();
    private BufferedReader bufferedReader;

    /** ---  Methods --- */
    public void getSalesStoreData(AssetManager assetManager) {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("test.csv")));
            if (bufferedReader.ready()) {
                Log.i("test:sales_store_data", bufferedReader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
