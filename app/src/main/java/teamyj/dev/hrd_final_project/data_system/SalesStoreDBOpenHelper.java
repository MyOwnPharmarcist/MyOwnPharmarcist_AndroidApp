package teamyj.dev.hrd_final_project.data_system;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SalesStoreDBOpenHelper extends SQLiteOpenHelper {
    private StringBuilder stringBuilder = new StringBuilder();
    public static final String TABLE_SALES_STORE =  "Sales_Store";
    public static final String STORE_ADDR =         "store_addr";
    public static final String STORE_NAME =         "store_name";
    public static final String STORE_CALL_NUMBER =  "store_call_number";
    public static final String STORE_TIME =         "store_time";
    public static final String STORE_ID =           "store_ID";
    public static final String STORE_LATITUDE =     "store_latitude";
    public static final String STORE_LONGITUDE =    "store_longitude";

    public SalesStoreDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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
}
