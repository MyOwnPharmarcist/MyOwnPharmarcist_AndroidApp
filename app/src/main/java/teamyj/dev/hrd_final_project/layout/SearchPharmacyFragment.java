package teamyj.dev.hrd_final_project.layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.location.Location;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.data_system.SalesStoreDBOpenHelper;
import teamyj.dev.hrd_final_project.main_system.CustomApplication;

public class SearchPharmacyFragment extends Fragment implements OnMapReadyCallback {

    private CustomApplication application;

    private Fragment fragment;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private FusedLocationProviderClient locationClient;
    private MapView mapView;
    private NaverMap naverMap;
    private LocationManager locationManager;
    private UiSettings uiSettings;
    private SalesStoreDBOpenHelper salesdbHelper;
    private View viewBottomSheet;
    private BottomSheetBehavior behavior;
    private Marker marker;
//    private Button searchingMapBtn;
    private List<Marker> markerList = new ArrayList<>(); // 표시된 마커 리스트

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("on", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_search_pharmacy, container, false);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Marker의 상세 정보를 출력하기 위한 BottomSheet
        viewBottomSheet = view.findViewById(R.id.viewBottomSheet);
        behavior = BottomSheetBehavior.from(viewBottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        fragment = this;

//        searchingMapBtn = view.findViewById(R.id.btn_search_nearby);

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
            locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng);
                    naverMap.moveCamera(cameraUpdate);

                    LocationOverlay locationOverlay = naverMap.getLocationOverlay();
                    locationOverlay.setPosition(latLng);
                    locationOverlay.setVisible(true);

                    addMarkers(latLng); // 초기 화면 마커 표시
                }
            });
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // mapType
        this.naverMap.setMapType(NaverMap.MapType.Basic);
        // 실내지도 활성화
        this.naverMap.setIndoorEnabled(true);
        // 최소 줌 레벨 설정
        this.naverMap.setMinZoom(7);

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
//                searchingMapBtn.setVisibility(View.GONE);   // 버튼 숨김
            }
        });

        // 카메라 이동 후 정지시 호출 되는 메서드
        this.naverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng position = naverMap.getCameraPosition().target;  // 현재 카메라 위치
                Log.d("CameraIdle", "Camera is idle at: " + position.latitude + ", " + position.longitude);

                deleteMarkers();    // 마커 삭제
                addMarkers(position); // 현재 카메라 위치에 마커 추가

//                searchingMapBtn.setVisibility(View.VISIBLE);    // 버튼 표시
//                searchingMapBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        deleteMarkers();    // 마커 삭제
//                        addMarkers(position); // 현재 카메라 위치에 마커 추가
//                    }
//                });
            }
        });
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
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

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
                String title = cursor.getString(cursor.getColumnIndexOrThrow("store_name"));

                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("store_latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("store_longitude"));

                marker = new Marker();
                marker.setPosition(new LatLng(latitude, longitude));
                marker.setCaptionText(title);
                marker.setCaptionRequestedWidth(200);
                marker.setMinZoom(10);  // 너무 멀어지면 마커 제거
                marker.setWidth(80);
                marker.setHeight(110);
                marker.setMap(naverMap);
                marker.setOnClickListener(overlay -> {
                    // 마커를 클릭할 때 정보 창을 엶
                    new BottomSheetFragment(behavior, fragment);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true;
                });
                markerList.add(marker); // 리스트에 마커 저장
            } while (cursor.moveToNext());
        }

        cursor.close();
        salesdbHelper.close();
    }

    // 마커 핀 삭제 메서드
    private void deleteMarkers() {
        for (Marker marker : markerList) {
            marker.setMap(null);  // 지도에서 마커 삭제
        }
        markerList.clear();  // 리스트 초기화
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