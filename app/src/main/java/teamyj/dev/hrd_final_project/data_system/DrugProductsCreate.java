package teamyj.dev.hrd_final_project.data_system;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DrugProductsCreate {
    private static final String DRUG_PRODUCTS_FILE = "drug_products/drug_products_final.csv";
    private DrugProductsDBOpenHelper drugProductsDBOpenHelper;
    private BufferedReader bufferedReader;

    /** --- Drug Products Create --- */
    public void getDrugProducts(AssetManager assetManager, DrugProductsDBOpenHelper drugProductsDBOpenHelper) {
        this.drugProductsDBOpenHelper = drugProductsDBOpenHelper;
        long time = System.currentTimeMillis();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(DRUG_PRODUCTS_FILE)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                processingDrugProductsData(line);
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
        // info를 분리하여 저장할 객체
        String[] data = null;

        // 전처리
        
    }
}
