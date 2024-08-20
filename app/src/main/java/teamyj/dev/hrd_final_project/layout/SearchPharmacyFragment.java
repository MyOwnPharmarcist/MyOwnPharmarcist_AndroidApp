package teamyj.dev.hrd_final_project.layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper;
import teamyj.dev.hrd_final_project.main_system.CustomApplication;

public class SearchPharmacyFragment extends Fragment implements OnMapReadyCallback {

    private CustomApplication application;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private MapView mapView;
    private NaverMap naverMap;
    private LocationManager locationManager;
    private UiSettings uiSettings;
    private SalesStoreDBOpenHelper salesdbHelper;
    private Marker marker;

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
//            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, location -> {
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng);
//                naverMap.moveCamera(cameraUpdate);
//            }, Looper.getMainLooper());
        }
        else {
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

        // 카메라 이동시 호출 되는 메서드
        this.naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int reason, boolean animated) {
                Log.d("CameraChange", "Camera moved: Reason = " + reason + ", Animated = " + animated);
                // 카메라 이동이 완료된 후 마커 위치 업데이트
                CameraPosition cameraPosition = naverMap.getCameraPosition();

            }
        });

        // 카메라 이동 후 정지시 호출 되는 메서드
        this.naverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng position = naverMap.getCameraPosition().target;
                Log.d("CameraIdle", "Camera is idle at: " + position.latitude + ", " + position.longitude);
            }
        });

        LatLng cameraPosition = naverMap.getCameraPosition().target;
        addMarkers(cameraPosition);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("on", "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(naverMap != null) {
                    onMapReady(naverMap);
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationManager = (LocationManager)requireContext().getSystemService(Context.LOCATION_SERVICE);

        // 레이아웃 변경 리스너 설정
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 이전의 레이아웃 변경 리스너 제거
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Rect rect = new Rect();
                // 네비게이션 바 높이 계산
                view.getWindowVisibleDisplayFrame(rect);
                int screenY = view.getRootView().getHeight();
                int navigationBarHeight = screenY - rect.bottom;

                // 상태바 높이 계산
                int id = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
                int statusBarHeight = getContext().getResources().getDimensionPixelSize(id);

                adjustMapUI(navigationBarHeight, statusBarHeight);
            }
        });
    }

    private void adjustMapUI(int navigationBarHeight, int statusBarHeight) {
        View mapView = getView().findViewById(R.id.map_view);
        if(mapView != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mapView.getLayoutParams();
            params.bottomMargin = navigationBarHeight;
            params.topMargin = statusBarHeight;
            mapView.setLayoutParams(params);
        }
    }

    // 마커 핀 추가 메서드
    private void addMarkers(LatLng cameraPosition) {
        salesdbHelper = new SalesStoreDBOpenHelper(getContext(), "sales_store.db", null, 1);
        Cursor cursor = salesdbHelper.getLocations(cameraPosition);

        if(cursor.moveToFirst()) {
            do {
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("store_latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("store_longitude"));

                marker = new Marker();
                marker.setPosition(new LatLng(latitude, longitude));
                marker.setWidth(80);
                marker.setHeight(110);
                marker.setMap(naverMap);
            } while (cursor.moveToNext());
        }

        cursor.close();
        salesdbHelper.close();
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