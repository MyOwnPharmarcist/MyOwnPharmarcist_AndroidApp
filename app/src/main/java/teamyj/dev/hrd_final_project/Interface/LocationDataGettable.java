package teamyj.dev.hrd_final_project.Interface;

import android.database.Cursor;

import com.naver.maps.geometry.LatLng;

public interface LocationDataGettable {
    Cursor getLocations(LatLng cameraPosition);
}
