package teamyj.dev.hrd_final_project.Interface;

import android.database.Cursor;

public interface DetailDataGettable {
    Cursor getSelectionDrugInfo(String itemName);
}
