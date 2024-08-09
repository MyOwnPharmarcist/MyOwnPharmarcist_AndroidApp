package teamyj.dev.hrd_final_project.data_system;

import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.STORE_ADDR;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.STORE_CALL_NUMBER;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.STORE_ID;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.STORE_LATITUDE;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.STORE_LONGITUDE;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.STORE_NAME;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.STORE_TIME;
import static teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper.TABLE_SALES_STORE;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SalesStoreCreate {
    private static final String SALES_STORE_FILE = "sales_store/drug_stores_csv_final.csv";
    private SalesStoreDBOpenHelper salesStoreDBOpenHelper;
    private BufferedReader bufferedReader;

    /** --- Sales Store Create --- */
    public void getSalesStores(AssetManager assetManager, SalesStoreDBOpenHelper salesStoreDBOpenHelper) {
        this.salesStoreDBOpenHelper = salesStoreDBOpenHelper;
        long time = System.currentTimeMillis();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(SALES_STORE_FILE)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                processingSalesStoreData(line);
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
        }

        Log.i("sales_store", String.valueOf(Long.sum(System.currentTimeMillis(), -time)) + "ms 소요");
    }

    private void processingSalesStoreData(String store_info) {
        // info를 분리하여 저장할 객체
        String[] data = null;

        // 전처리
        String[] preprocessing = store_info.split("\"");
        String addr = null;
        String name = null;

        // 주소에 ,가 있는 경우 3 / 없는 경우 1
        if(preprocessing.length == 1) {
            data = preprocessing[0].split(",");
            addr = data[0];
            name = data[1];
        }
        else if (preprocessing.length == 3) {
            data = preprocessing[2].split(",");
            if(preprocessing[0].isEmpty()) {
                // addr에 ,가 있을 때
                addr = preprocessing[1];
                name = data[1];
            }
            else {
                // name에 ,가 있을 때
                addr = (preprocessing[0].split(","))[0];
                addr = preprocessing[1];
            }
        }
        else {  // preprocessing.length == 5
            // addr과 name에 ,가 있을 때
            data = preprocessing[4].split(",");
            addr = preprocessing[1];
            name = preprocessing[3];
        }

        addSalesStoreData(data, addr, name);
    }

    // 영업 시작, 종료 시간을 문자열로 정리
    // ex) 1800, 900 -> 월요일: 09:00 ~ 18:00
    private void addSalesStoreData(String[] data, String addr, String name) {
        SQLiteDatabase db = salesStoreDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORE_ID, data[19]);
        values.put(STORE_NAME, name);
        values.put(STORE_CALL_NUMBER, data[2]);
        values.put(STORE_ADDR, addr);
        values.put(STORE_TIME, organizeOpenCloseTime(data));
        values.put(STORE_LATITUDE, data[data.length - 2]);
        values.put(STORE_LONGITUDE, data[data.length - 1]);
        db.insertWithOnConflict(TABLE_SALES_STORE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private String organizeOpenCloseTime(String[] data) {
        // data[4] = 월요일 영업 시작 시간, data[3] = 월요일 영업 시작 시간
        // 24시간 : xx시 x0분 시작, xx시 x1분 종료로 기록
        if(!data[3].isEmpty() && Integer.parseInt(data[4]) - Integer.parseInt(data[3]) == -1) {
            return "24시간 영업";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 영업 시간 월 ~ 일 + 공휴일
        String[] week = {"월요일: ", "화요일: ", "수요일: ", "목요일: ",
                "금요일: ", "토요일: ", "일요일: ", "공휴일: "};
        // processing
        for(int i = 0 ; i < 8; i++) {
            int index = 4 + i * 2;  // data[4] : 월요일 영업 시작 시간, data[6] : 화요일 영업 시작 시간, ...
            if(data[index].isEmpty()) {
                week[i] += "휴무";
                continue;
            }
            stringBuilder.setLength(0);
            int length;
            for(int j = 0; j < 2; j++) {
                length = data[index].length();
                if (length == 4){
                    // 1800 -> 18 + ":" + 00 -> 18:00
                    stringBuilder.append(data[index].substring(0, 2)).append(":").append(data[index].substring(2));
                }

                if(j == 0) {
                    stringBuilder.append(" ~ ");
                    index--;    // 시작
                }
            }
            week[i] += stringBuilder.toString();
        }
        stringBuilder.setLength(0);
        for(String store_time : week) {
            stringBuilder.append(store_time).append("\n");
        }

        return stringBuilder.toString();
    }

    /** --- Sales Store Create End --- */
}
