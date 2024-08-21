package teamyj.dev.hrd_final_project.data_system;

import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.CHART;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.EE_DOC_DATA;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.ENTP_NAME;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.ETC_OTC_CODE;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.INDUTY_TYPE;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.ITEM_NAME;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.STORAGE_METHOD;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.TABLE_DRUG_PRODUCTS;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.UD_DOC_DATA;
import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.VALID_TERM;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DrugProductsCreate {
    private static final String DRUG_PRODUCTS_FILE = "drug_products/drug_product_final.csv";
    private DrugProductsDBOpenHelper drugProductsDBOpenHelper;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder = new StringBuilder();

    /** --- Drug Products Create --- */
    public void getDrugProducts(AssetManager assetManager, DrugProductsDBOpenHelper drugProductsDBOpenHelper) {
        this.drugProductsDBOpenHelper = drugProductsDBOpenHelper;
        long time = System.currentTimeMillis();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(DRUG_PRODUCTS_FILE)));
            String[] buffer;
            String line;
            int count;
            // 에셋에서 csv 파일을 읽어서 String으로 반환
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                // """ 단위로 split을 했을 때 배열의 크기 = 18개
                buffer = line.split("\"\"\"");
                count = buffer.length;
                if(line.endsWith("\"\"\"")) {
                    count++;
                }
                while (count < 19) {
                    line = bufferedReader.readLine();
                    if (count != 18) {
                        stringBuilder.append(line);
                        buffer = line.split("\"\"\"");
                        count += buffer.length - 1;
                    } else {
                        stringBuilder.append("\n").append(line);
                    }

                    if (line.endsWith("\"\"\"")) {
                        count++;
                    }
                }
                processingDrugProductsData(stringBuilder.toString());

                stringBuilder.setLength(0);
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
        }
    }

    private void processingDrugProductsData(String product_info) {
        // 전처리
        String[] preproessing = product_info.split("\"\"\"");

        addDrugProductsData(preproessing);
    }

    private void addDrugProductsData(String[] data)  {
        SQLiteDatabase db = drugProductsDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, data[1]);
        values.put(ENTP_NAME, data[3]);
        values.put(ETC_OTC_CODE, data[5]);
        values.put(CHART, data[7]);
        values.put(STORAGE_METHOD, data[9]);
        values.put(VALID_TERM, data[11]);
        values.put(INDUTY_TYPE, data[13]);
        values.put(EE_DOC_DATA, data[15]);
        values.put(UD_DOC_DATA, data[17]);
        db.insertWithOnConflict(TABLE_DRUG_PRODUCTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.i("drug", data[1]);
    }
}
