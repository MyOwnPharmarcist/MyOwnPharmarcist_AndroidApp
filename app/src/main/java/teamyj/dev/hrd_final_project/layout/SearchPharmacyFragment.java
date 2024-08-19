package teamyj.dev.hrd_final_project.layout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

import teamyj.dev.hrd_final_project.R;

public class SearchPharmacyFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private MapView mapView;
    private NaverMap naverMap;
    private UiSettings uiSettings;
//    private Marker marker;

    private LatLng coord = new LatLng(36.336590, 127.459220);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("on", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_search_pharmacy, container, false);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // 위치 소스 초기화
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        return view;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.i("on", "onMapReady");
        this.naverMap = naverMap;
        // 지도 초기화 로직

        // 위치 사용 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 위치 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // mapType
        this.naverMap.setMapType(NaverMap.MapType.Basic);

        // 실내지도 활성화
        this.naverMap.setIndoorEnabled(true);

        // 네이버 지도 UI 세팅 (네이버 로고를 가리면 안됨)
        this.uiSettings = naverMap.getUiSettings();
        this.uiSettings.setCompassEnabled(true);            // 나침반
        this.uiSettings.setLocationButtonEnabled(true);     // 내 위치
        this.uiSettings.setZoomControlEnabled(false);       // 줌

//        // 카메라 이동 리스너 등록
//        this.naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(int reason, boolean animated) {
//                // 카메라 이동이 완료된 후 마커 위치 업데이트
//                CameraPosition cameraPosition = naverMap.getCameraPosition();
//                updateMarkerPosition(cameraPosition.target);
//            }
//        });
    }

//    // 마커 위치 업데이트 메서드
//    private void updateMarkerPosition(LatLng position) {
//        if (marker != null) {
//            marker.setPosition(position);
//        } else {
//            marker = new Marker();
//            marker.setPosition(position);
//            marker.setMap(naverMap);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("on", "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우, 위치 추적 모드 설정
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    @Override
    public void onStart() {
        Log.i("on", "onStart");
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        Log.i("on", "onResume");
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        Log.i("on", "onPause");
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        Log.i("on", "onStop");
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i("on", "onDestroyView");
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        Log.i("on", "onLowMemory");
        super.onLowMemory();
        mapView.onLowMemory();
    }

}