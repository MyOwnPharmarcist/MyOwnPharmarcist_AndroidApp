package teamyj.dev.hrd_final_project.data_system;

import static teamyj.dev.hrd_final_project.data_system.DrugListDBOpenHelper.DRUG_LIST_ELEMENTS;
import static teamyj.dev.hrd_final_project.data_system.DrugListDBOpenHelper.TABLE_DRUG_LIST;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DrugListCreate {
    private static final String DRUG_PRODUCTS_FILE = "drug_list/drug_list_final.csv";
    private DrugListDBOpenHelper drugListDBOpenHelper;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder = new StringBuilder();

    /** --- Drug List Create --- */
    public void getDrugList(AssetManager assetManager, DrugListDBOpenHelper drugListDBOpenHelper) {
        this.drugListDBOpenHelper = drugListDBOpenHelper;
        long time = System.currentTimeMillis();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(DRUG_PRODUCTS_FILE)));
            String[] buffer;
            String line;
            int count;
            // 에셋에서 csv 파일을 읽어서 String으로 반환
            while ((line = bufferedReader.readLine()) != null) {
                processingDrugListData(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.i("Error","BufferedReader 생성 실패");
                }
            }
            Log.i("drug_list_create", Long.sum(System.currentTimeMillis(), -time) + "ms");
        }
    }

    private void processingDrugListData(String product_info) {
        // 전처리
        String[] preproessing = product_info.split(",");

        addDrugListData(preproessing);
    }

    private void addDrugListData(String[] data)  {
        SQLiteDatabase db = drugListDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < 3; i++) {
            values.put(DRUG_LIST_ELEMENTS[i], data[i]);
        }
        for(int i = 5; i < 10; i++) {
            values.put(DRUG_LIST_ELEMENTS[i-2], data[i]);
        }
        db.insertWithOnConflict(TABLE_DRUG_LIST, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.i("drug", data[0]);
    }
}
