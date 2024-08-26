package teamyj.dev.hrd_final_project.Interface;

import android.database.Cursor;

public interface ListDataGettable {
    Cursor searchDrugList(String condition);
}
