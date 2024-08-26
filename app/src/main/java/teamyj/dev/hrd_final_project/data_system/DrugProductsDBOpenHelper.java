package teamyj.dev.hrd_final_project.data_system;

import static teamyj.dev.hrd_final_project.data_system.DataManager.BUFFER_SIZE;
import static teamyj.dev.hrd_final_project.data_system.DataManager.FILE_PATH;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import teamyj.dev.hrd_final_project.Interface.DBLoadable;
import teamyj.dev.hrd_final_project.Interface.DBWritable;
import teamyj.dev.hrd_final_project.Interface.DetailDataGettable;

public class DrugProductsDBOpenHelper extends SQLiteOpenHelper implements DBWritable, DBLoadable, DetailDataGettable {
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

    private Context context;

    public DrugProductsDBOpenHelper(Context context) {
        super(context, DRUG_PRODUCTS_DB_NAME, null, DRUG_PRODUCTS_DB_VERSION);
        this.context = context;
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

    @Override
    public void loadDB() {
        context.deleteDatabase(DRUG_PRODUCTS_DB_NAME);
        this.getReadableDatabase();
        copyDB();
    }

    private void copyDB() {
        try {
            InputStream inputStream = context.getAssets().open("drug_products/" + DRUG_PRODUCTS_DB_NAME);
            String outPath = FILE_PATH + DRUG_PRODUCTS_DB_NAME;
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

    public Cursor getFirstDrugInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DRUG_PRODUCTS + " LIMIT 1", null);
    }
}
