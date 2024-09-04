package teamyj.dev.hrd_final_project.data_system;

import static teamyj.dev.hrd_final_project.data_system.DataManager.BUFFER_SIZE;
import static teamyj.dev.hrd_final_project.data_system.DataManager.FILE_PATH;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.naver.maps.geometry.LatLng;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import teamyj.dev.hrd_final_project.Interface.DBLoadable;
import teamyj.dev.hrd_final_project.Interface.DBWritable;
import teamyj.dev.hrd_final_project.Interface.LocationDataGettable;

public class EmergencyDrugDBOpenHelper extends SQLiteOpenHelper implements DBWritable, DBLoadable, LocationDataGettable {
    public static final String EMERGENCY_DRUG_DB_NAME = "emergency_drug.db";
    public static final int EMERGENCY_DRUG_DB_VERSION = 1;

    private StringBuilder stringBuilder = new StringBuilder();
    public static final String TABLE_EMERGENCY_DRUG = "Emergency_Drug";
    public static final String BUSINESS_STATUS = "business_status";
    public static final String STREET_ADDRESS = "street_address";
    public static final String BUSINESS_NAME = "business_name";
    public static final String EMERGENCY_LATITUDE = "ratitude";
    public static final String EMERGENCY_LONGITUDE = "longitude";

    private final Context context;

    public EmergencyDrugDBOpenHelper(Context context) {
        super(context, EMERGENCY_DRUG_DB_NAME, null, EMERGENCY_DRUG_DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        stringBuilder.setLength(0);
        stringBuilder.append("CREATE TABLE ").append(TABLE_EMERGENCY_DRUG).append(" (");
        stringBuilder.append(BUSINESS_STATUS).append(" TEXT,");
        stringBuilder.append(STREET_ADDRESS).append(" TEXT,");
        stringBuilder.append(BUSINESS_NAME).append(" TEXT PRIMARY KEY,");
        stringBuilder.append(EMERGENCY_LATITUDE).append(" TEXT,");
        stringBuilder.append(EMERGENCY_LONGITUDE).append(" TEXT);");
        sqLiteDatabase.execSQL(stringBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EMERGENCY_DRUG);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void loadDB() {
        // 기존 db 파일 삭제
        context.deleteDatabase(EMERGENCY_DRUG_DB_NAME);

        this.getReadableDatabase();
        copyDB();
    }

    // db 파일 복사
    private void copyDB() {
        try {
            InputStream inputStream = context.getAssets().open("emergency_drug/" + EMERGENCY_DRUG_DB_NAME);
            String outPath = FILE_PATH + EMERGENCY_DRUG_DB_NAME;
            OutputStream outputStream = new FileOutputStream(outPath);

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** ---     db 검색 후 반환하는 코드     ----- */
    // 위도, 경도 값 가져오는 메서드
    @Override
    public Cursor getLocations(LatLng cameraPosition) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EMERGENCY_DRUG +
                " WHERE " + EMERGENCY_LATITUDE + " > " + (cameraPosition.latitude - 0.015) + " and "
                + EMERGENCY_LATITUDE + " < " + (cameraPosition.latitude + 0.015) + " and "
                + EMERGENCY_LONGITUDE + " > " + (cameraPosition.longitude - 0.015) + " and "
                + EMERGENCY_LONGITUDE + " < " + (cameraPosition.longitude + 0.015), null);
    }
}
