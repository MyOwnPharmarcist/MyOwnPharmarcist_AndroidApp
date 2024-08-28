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
import teamyj.dev.hrd_final_project.Interface.ListDataGettable;

public class DrugListDBOpenHelper extends SQLiteOpenHelper implements DBWritable, DBLoadable, ListDataGettable {
    public static final String DRUG_LIST_DB_NAME = "drug_list.db";
    public static final int DRUG_LIST_DB_VERSION = 1;

    private StringBuilder stringBuilder = new StringBuilder();
    public static final String TABLE_DRUG_LIST = "Drug_List";

    /**
     * 약품 리스트 요소 (앞의 번호는 인덱스 번호)
     * 0. 아이템 이름
     * 1. 업체 이름
     * 2. 이미지 링크
     * 3. 모양
     * 4. 색
     * 5. 분류명
     * 6. 전문/일반의약품
     * 7. 코드명
     */
    public static final String[] DRUG_LIST_ELEMENTS = {
            "item_name",
            "entp_name",
            "item_image_link",
            "shape",
            "color",
            "class_name",
            "etc_otc_name",
            "code_name"
    };

    private Context context;

    public DrugListDBOpenHelper(Context context){
        super(context, DRUG_LIST_DB_NAME, null, DRUG_LIST_DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        stringBuilder.setLength(0);
        stringBuilder.append("CREATE TABLE ").append(TABLE_DRUG_LIST).append(" (");
        stringBuilder.append(DRUG_LIST_ELEMENTS[0]).append(" TEXT PRIMARY KEY,");
        for(int i = 1; i < 7; i++) {
            stringBuilder.append(DRUG_LIST_ELEMENTS[i]).append(" TEXT,");
        }
        stringBuilder.append(DRUG_LIST_ELEMENTS[7]).append(" TEXT);");
        sqLiteDatabase.execSQL(stringBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG_LIST);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void loadDB() {
        context.deleteDatabase(DRUG_LIST_DB_NAME);
        this.getReadableDatabase();
        copyDB();
    }

    private void copyDB() {
        try {
            InputStream inputStream = context.getAssets().open("drug_list/" + DRUG_LIST_DB_NAME);
            String outPath = FILE_PATH + DRUG_LIST_DB_NAME;
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

    @Override
    public Cursor searchDrugList(String condition) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DRUG_LIST + " WHERE " + condition, null);
    }
}
