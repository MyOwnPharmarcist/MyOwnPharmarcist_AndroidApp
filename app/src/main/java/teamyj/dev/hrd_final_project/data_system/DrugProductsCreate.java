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

import teamyj.dev.hrd_final_project.Interface.DBCreatable;
import teamyj.dev.hrd_final_project.Interface.DBWritable;

public class DrugProductsCreate implements DBCreatable {
    private static final String DRUG_PRODUCTS_FILE = "drug_products/drug_product_final.csv";
    private DBWritable dbWritable;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder = new StringBuilder();

    /** --- Drug Products Create --- */
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
                stringBuilder.append(line);
                // """ 단위로 split을 했을 때 배열의 데이터 = 18개 + null
                buffer = line.split("\"\"\"");
                count = buffer.length;
                // 마지막이 """ 인가? => 데이터의 끝 (while문 스킵)
                if(line.endsWith("\"\"\"")) {
                    count++;
                    // 용법 용량이 없는 경우 => 데이터 17개 + null + null
                    if(line.endsWith("\"\"\"\"\"\"")) {
                        count++;
                    }
                }
                while (count < 19) {
                    line = bufferedReader.readLine();
                    // 개행 문자가 포함된 경우
                    if (count != 18) {
                        stringBuilder.append(line);
                        buffer = line.split("\"\"\"");
                        // -1? => 개행 문자로 인해 length가 1 늘어남
                        count += buffer.length - 1;
                    } else {
                        // 18번째 데이터 = 용법 용량
                        // 데이터가 크기 때문에 원본 데이터 그대로
                        // 개행 문자를 추가해서 가독성 높이기
                        stringBuilder.append("\n").append(line);
                    }
                    // 마지막 데이터인가?
                    if (line.endsWith("\"\"\"")) {
                        count++;
                        if(line.endsWith("\"\"\"\"\"\"")) {
                            count++;
                        }
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
                    Log.i("Error", "BufferedReader 생성 실패");
                }
            }
            Log.i("drug_products_create", Long.sum(System.currentTimeMillis(), -time) + "ms");
        }
    }

    // 데이터 전처리
    private void processingDrugProductsData(String product_info) {
        // 전처리
        String[] preproessing = product_info.split("\"\"\"");
        String ud;
        if(preproessing.length == 18) {
            ud = preproessing[17];
        } else {
            ud = "";
        }
        if(preproessing.length > 19) {
            Log.i("test_data", preproessing[1]);
        }

        addDrugProductsData(preproessing, ud);
    }

    // 전처리한 데이터로 db 파일 생성
    private void addDrugProductsData(String[] data, String ud)  {
        SQLiteDatabase db = dbWritable.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, data[1]);
        values.put(ENTP_NAME, data[3]);
        values.put(ETC_OTC_CODE, data[5]);
        values.put(CHART, data[7]);
        values.put(STORAGE_METHOD, data[9]);
        values.put(VALID_TERM, data[11]);
        values.put(INDUTY_TYPE, data[13]);
        values.put(EE_DOC_DATA, data[15]);
        values.put(UD_DOC_DATA, ud);
        db.insertWithOnConflict(TABLE_DRUG_PRODUCTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//        Log.i("drug", data[1]);
    }
}
