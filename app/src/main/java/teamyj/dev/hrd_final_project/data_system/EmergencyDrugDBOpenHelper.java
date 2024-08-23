package teamyj.dev.hrd_final_project.data_system;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmergencyDrugDBOpenHelper extends SQLiteOpenHelper {
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

    public EmergencyDrugDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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
}
