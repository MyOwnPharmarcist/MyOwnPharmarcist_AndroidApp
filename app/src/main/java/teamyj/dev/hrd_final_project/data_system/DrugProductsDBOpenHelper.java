package teamyj.dev.hrd_final_project.data_system;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DrugProductsDBOpenHelper extends SQLiteOpenHelper {
    public static final String DRUG_PRODUCTS_DB_NAME = "drug_products.db";
    public static final int DRUG_PRODUCTS_DB_VERSION = 1;

    private StringBuilder stringBuilder = new StringBuilder();
    public static final String TABLE_DRUG_PRODUCTS = "Drug_Products";
    public static final String ITEM_NAME = "item_name";
    public static final String ENTP_NAME = "entp_name";
    public static final String ETC_OTC_CODE = "etc_otc_code";
    public static final String CHART = "chart";
    public static final String STORAGE_METHOD = "storage_method";
    public static final String VALID_TERM = "valid_term";
    public static final String INDUTY_TYPE = "induty_type";
    public static final String EE_DOC_DATA = "ee_doc_data";
    public static final String UD_DOC_DATA = "ud_doc_data";

    public DrugProductsDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        stringBuilder.setLength(0);
        stringBuilder.append("CREATE TABLE ").append(TABLE_DRUG_PRODUCTS).append(" (");
        stringBuilder.append(ITEM_NAME).append(" TEXT PRIMARY KEY,");
        stringBuilder.append(ENTP_NAME).append(" TEXT,");
        stringBuilder.append(ETC_OTC_CODE).append(" TEXT,");
        stringBuilder.append(CHART).append(" TEXT,");
        stringBuilder.append(STORAGE_METHOD).append(" TEXT,");
        stringBuilder.append(VALID_TERM).append(" TEXT,");
        stringBuilder.append(INDUTY_TYPE).append(" TEXT,");
        stringBuilder.append(EE_DOC_DATA).append(" TEXT,");
        stringBuilder.append(UD_DOC_DATA).append(" TEXT);");
        sqLiteDatabase.execSQL(stringBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG_PRODUCTS);
        onCreate(sqLiteDatabase);
    }
}
