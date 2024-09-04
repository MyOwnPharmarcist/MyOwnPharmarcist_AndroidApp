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

import teamyj.dev.hrd_final_project.Interface.DBCreatable;
import teamyj.dev.hrd_final_project.Interface.DBWritable;

public class DrugListCreate implements DBCreatable {
    private static final String DRUG_PRODUCTS_FILE = "drug_list/drug_list_final.csv";
    private DBWritable dbWritable;
    private BufferedReader bufferedReader;

    /** --- Drug List Create --- */
    // csv 파일에서 데이터를 읽어오는 코드
    @Override
    public void create(AssetManager assetManager, DBWritable dbWritable) {
        this.dbWritable = dbWritable;
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

    // 데이터 전처리
    private void processingDrugListData(String product_info) {
        String[] preproessing = product_info.split(",");

        addDrugListData(preproessing);
    }

    // 전처리한 데이터로 db 파일 생성
    private void addDrugListData(String[] data)  {
        SQLiteDatabase db = dbWritable.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < 3; i++) {
            values.put(DRUG_LIST_ELEMENTS[i], data[i]);
        }
        for(int i = 5; i < 10; i++) {
            values.put(DRUG_LIST_ELEMENTS[i-2], data[i]);
        }
        db.insertWithOnConflict(TABLE_DRUG_LIST, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//        Log.i("drug", data[0]);
    }
}
