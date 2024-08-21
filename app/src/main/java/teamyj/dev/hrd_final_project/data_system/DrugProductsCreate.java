package teamyj.dev.hrd_final_project.data_system;

import android.content.res.AssetManager;
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
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                buffer = line.split("\"\"\"");
                if((count = buffer.length) == 19) {
                    processingDrugProductsData(stringBuilder.toString());
                } else {
                    while (count != 19) {
                        line = bufferedReader.readLine();
                        stringBuilder.append(line);
                        buffer = line.split("\"\"\"");
                        count += buffer.length - 1;
                        if(line.endsWith("\"\"\"")) {
                            count++;
                        }
                    }
                    processingDrugProductsData(stringBuilder.toString());
                }

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
        // info를 분리하여 저장할 객체
        String[] data = null;

        // 전처리
        String[] preproessing = product_info.split("\"\"\"");
        System.out.printf("");

    }
}
