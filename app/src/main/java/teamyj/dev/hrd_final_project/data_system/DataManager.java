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
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;

import teamyj.dev.hrd_final_project.main_system.CustomApplication;


public class DataManager {
    /** ---  싱글톤 패턴  --- */
    private static final DataManager instance = new DataManager();
    private DataManager() {}
    public static DataManager getInstance() {
        return instance;
    }

    /** ---  Fields  --- */
    private BufferedReader bufferedReader;
    private AssetManager assetManager;

    // SQLite
    private  static final String SALES_STORE_DB_NAME = "sales_store.db";
    private static final int DB_VERSION = 1;

    private SalesStoreDBOpenHelper salesStoreDBOpenHelper;

    /** ---  Methods --- */
    public void initData(AssetManager assetManager, Context context) {
        this.assetManager = assetManager;
        CustomApplication application = CustomApplication.getInstance();
        ExecutorService excutor = application.getExcutor();

        salesStoreDBOpenHelper = new SalesStoreDBOpenHelper(context, SALES_STORE_DB_NAME, null, DB_VERSION);

        excutor.submit(this::getSalesStores);
    }

    /** --- Sales Store --- */
    private void getSalesStores() {

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("sales_store/test.csv")));
            while (bufferedReader.ready()) {
                processingSalesStoreData(bufferedReader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processingSalesStoreData(String store_info) {
        // info를 분리하여 저장할 객체
        String[] data = null;

        // 전처리
        String[] preprocessing = store_info.split("\"");

        // 주소에 ,가 있는 경우 3 / 없는 경우 1
        if(preprocessing.length == 1) {
            data = preprocessing[0].split(",");
        }
        else if (preprocessing.length == 3) {
            data = preprocessing[2].split(",");
        }

        if(data != null) {
            addSalesStoreData(data, data[0].isEmpty() ? preprocessing[1] : data[0]);
        }
    }

    private void addSalesStoreData(String[] data, String addr) {
        SQLiteDatabase db = salesStoreDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORE_ID, data[19]);
        values.put(STORE_NAME, data[1]);
        values.put(STORE_CALL_NUMBER, data[2]);
        values.put(STORE_ADDR, addr);
        values.put(STORE_TIME, calcOpenCloseTime(data));
        values.put(STORE_LATITUDE, data[20]);
        values.put(STORE_LONGITUDE, data[21]);
        db.insertWithOnConflict(TABLE_SALES_STORE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private String calcOpenCloseTime(String[] data) {
        // data[4] = 월요일 영업 시작 시간, data[3] = 월요일 영업 시작 시간
        // 24시간 : xx시 x0분 시작, xx시 x1분 종료로 기록
        if(Integer.parseInt(data[4]) - Integer.parseInt(data[3]) == -1) {
            return "24시간 영업";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 영업 시간 월 ~ 일 + 공휴일
        String[] week = {"월요일: ", "화요일: ", "수요일: ", "목요일: ",
                "금요일: ", "토요일: ", "일요일: ", "공휴일: "};
        // processing
        for(int i = 0 ; i < 8; i++) {
            int index = 4 + i * 2;  // data[3] : 월요일 영업 종료 시간, data[4] : 월요일 영업 시작 시간, ...
            if(data[index].isEmpty()) {
                week[i] += "휴무";
                continue;
            }
            stringBuilder.setLength(0);
            int length;
            for(int j = 0; j < 2; j++) {
                length = data[index].length();
                if (length == 3) {
                    // 900 -> "0" + 9 + ":" + 00 -> 09:00
                    stringBuilder.append("0").append(data[index].charAt(0)).append(":").append(data[index].substring(1));
                } else if (length == 4){
                    // 1800 -> 18 + ":" + 00 -> 18:00
                    stringBuilder.append(data[index].substring(0, 2)).append(":").append(data[index].substring(2));
                } else if(length == 1) {
                    stringBuilder.append("00:0").append(data[index]);
                } else {    // length == 2
                    stringBuilder.append("00:").append(data[index]);
                }

                if(j == 0) {
                    stringBuilder.append(" ~ ");
                    index--;
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

    /** --- Sales Store End --- */


}
