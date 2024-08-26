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

public class SalesStoreDBOpenHelper extends SQLiteOpenHelper implements DBWritable, DBLoadable {
    public static final String SALES_STORE_DB_NAME = "sales_store.db";
    public static final int SALES_STORE_DB_VERSION = 1;

    private StringBuilder stringBuilder = new StringBuilder();
    public static final String TABLE_SALES_STORE =  "Sales_Store";
    public static final String STORE_ADDR =         "store_addr";
    public static final String STORE_NAME =         "store_name";
    public static final String STORE_CALL_NUMBER =  "store_call_number";
    public static final String STORE_TIME =         "store_time";
    public static final String STORE_ID =           "store_ID";
    public static final String STORE_LATITUDE =     "store_latitude";
    public static final String STORE_LONGITUDE =    "store_longitude";

    private final Context context;

    public SalesStoreDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        stringBuilder.setLength(0);
        stringBuilder.append("CREATE TABLE ").append(TABLE_SALES_STORE).append(" (");
        stringBuilder.append(STORE_ID).append(" TEXT PRIMARY KEY,");
        stringBuilder.append(STORE_NAME).append(" TEXT,");
        stringBuilder.append(STORE_CALL_NUMBER).append(" TEXT,");
        stringBuilder.append(STORE_ADDR).append(" TEXT,");
        stringBuilder.append(STORE_TIME).append(" TEXT,");
        stringBuilder.append(STORE_LATITUDE).append(" TEXT,");
        stringBuilder.append(STORE_LONGITUDE).append(" TEXT);");
        sqLiteDatabase.execSQL(stringBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_STORE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void loadDB() {
        context.deleteDatabase(SALES_STORE_DB_NAME);
        this.getReadableDatabase();
        copyDB();
    }

    private boolean checkDBExist() {
        String db_Path = FILE_PATH + SALES_STORE_DB_NAME;
        try (SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(db_Path, null, SQLiteDatabase.OPEN_READONLY)) {
            checkDB.execSQL("SELECT load_extension('libspatialite');");
            return checkDB.isOpen();
        } catch (Exception e) {
            return false;
        }
    }

    private void copyDB() {
        try {
            InputStream inputStream = context.getAssets().open("sales_store/" + SALES_STORE_DB_NAME);
            String outPath = FILE_PATH + SALES_STORE_DB_NAME;
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

    // 위도, 경도 값 가져오는 메서드
    public Cursor getLocations(LatLng cameraPosition) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SALES_STORE +
                " WHERE " + STORE_LATITUDE + " > " + (cameraPosition.latitude - 0.015) + " and "
                + STORE_LATITUDE + " < " + (cameraPosition.latitude + 0.015) + " and "
                + STORE_LONGITUDE + " > " + (cameraPosition.longitude - 0.015) + " and "
                + STORE_LONGITUDE + " < " + (cameraPosition.longitude + 0.015), null);
    }
}
