package teamyj.dev.hrd_final_project.layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

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
import com.naver.maps.map.CameraAnimation;
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
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;
import java.util.List;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper;
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
    private View viewBottomSheet;
    private BottomSheetBehavior<View> behavior;
    private Marker marker;
    private Bundle result;

    private SalesStoreDBOpenHelper salesdbHelper;
    private EmergencyDrugDBOpenHelper emergencyDrugDBOpenHelper;
    private List<Marker> markerList = new ArrayList<>();

    private TextView title;
    private TextView address;
    private TextView ex1;
    private TextView ex2;

    private TextView storeName;
    private TextView storeEtc;
    private TextView storeAddr;
    private TextView storeTime;// 표시된 마커 리스트

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

        title = view.findViewById(R.id.title);
        address = view.findViewById(R.id.address);
        ex1 = view.findViewById(R.id.ex1);
        ex2 = view.findViewById(R.id.ex2);

        storeName = view.findViewById(R.id.bottom_sheet_name);
        storeEtc = view.findViewById(R.id.bottom_sheet_text);
        storeAddr = view.findViewById(R.id.bottom_sheet_store_address);
        storeTime = view.findViewById(R.id.bottom_sheet_store_time);

        // 위치 소스 초기화 (내 위치 버튼 동작을 위해)
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
        this.uiSettings.setCompassEnabled(true);           // 나침반
        this.uiSettings.setLocationButtonEnabled(true);    // 내 위치
        this.uiSettings.setZoomControlEnabled(false);       // 줌
        this.uiSettings.setIndoorLevelPickerEnabled(true); // 실내지도 층 피커
//        this.uiSettings.setLogoGravity(Gravity.END|Gravity.BOTTOM); // 네이버 로고 Gravity
//        this.uiSettings.setLogoMargin(10,10,10,10); // 네이버 로고 Margin

        // 지도를 클릭 시
        this.naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                // BottomSheet를 숨김 + zoom을 당겨서 애니메이션 느낌 효과 추가
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                CameraUpdate cameraUpdate = CameraUpdate.zoomTo(15).animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
            }
        });

        // 카메라 이동시 호출 되는 메서드
        this.naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int reason, boolean animated) {
                Log.d("CameraChange", "Camera moved: Reason = " + reason + ", Animated = " + animated);
                CameraPosition cameraPosition = naverMap.getCameraPosition();
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
        View mapView = getView().findViewById(R.id.coordinator_layout);
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
        emergencyDrugDBOpenHelper = new EmergencyDrugDBOpenHelper(getContext(), "emergency_drug.db", null, 1);
        Cursor cursorSales = salesdbHelper.getLocations(cameraPosition);
        Cursor cursorEmergency = emergencyDrugDBOpenHelper.getLocations(cameraPosition);

        if(cursorSales.moveToFirst()) {
            do {
                String strStoreName = cursorSales.getString(cursorSales.getColumnIndexOrThrow("store_name"));
                String strStoreCallNumber = cursorSales.getString(cursorSales.getColumnIndexOrThrow("store_call_number"));
                String strStoreAddr = cursorSales.getString(cursorSales.getColumnIndexOrThrow("store_addr"));
                String strStoreTime = cursorSales.getString(cursorSales.getColumnIndexOrThrow("store_time"));
                double latitude = cursorSales.getDouble(cursorSales.getColumnIndexOrThrow("store_latitude"));
                double longitude = cursorSales.getDouble(cursorSales.getColumnIndexOrThrow("store_longitude"));

                marker = new Marker();
                marker.setPosition(new LatLng(latitude, longitude));
                marker.setCaptionText(strStoreName);
                marker.setCaptionRequestedWidth(200);
                marker.setMinZoom(10);  // 너무 멀어지면 마커 제거
                marker.setWidth(80);
                marker.setHeight(110);
                marker.setMap(naverMap);
                marker.setOnClickListener(overlay -> {
                    // 마커를 클릭할 때 해당 마커로 카메라 이동
                    CameraPosition selectedMarkerPosition = new CameraPosition(new LatLng(latitude, longitude), 16);
                    CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(selectedMarkerPosition).animate(CameraAnimation.Easing);
                    naverMap.moveCamera(cameraUpdate);

                    title.setText("약국 명");
                    storeName.setText(strStoreName);
                    address.setText("주소");
                    storeAddr.setText(strStoreAddr);
                    ex1.setText("전화번호");
                    storeEtc.setText(strStoreCallNumber);
                    ex2.setText("영업시간");
                    storeTime.setText(strStoreTime);

                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    return true;
                });
                markerList.add(marker); // 리스트에 마커 저장
            } while (cursorSales.moveToNext());
        }

        if(cursorEmergency.moveToFirst()) {
            do {
                String strName = cursorEmergency.getString(cursorEmergency.getColumnIndexOrThrow("business_name"));
                String strAddr = cursorEmergency.getString(cursorEmergency.getColumnIndexOrThrow("street_address"));
                String strStatus = cursorEmergency.getString(cursorEmergency.getColumnIndexOrThrow("business_status"));
                double latitude = cursorEmergency.getDouble(cursorEmergency.getColumnIndexOrThrow("ratitude"));
                double longitude = cursorEmergency.getDouble(cursorEmergency.getColumnIndexOrThrow("longitude"));

                marker = new Marker();
                marker.setPosition(new LatLng(latitude, longitude));
                marker.setCaptionText(strName);
                marker.setIcon(MarkerIcons.BLACK);
                marker.setIconTintColor(Color.BLUE);
                marker.setCaptionRequestedWidth(200);
                marker.setMinZoom(10);  // 너무 멀어지면 마커 제거
                marker.setWidth(80);
                marker.setHeight(110);
                marker.setMap(naverMap);
                marker.setOnClickListener(overlay -> {
                    // 마커를 클릭할 때 해당 마커로 카메라 이동
                    CameraPosition selectedMarkerPosition = new CameraPosition(new LatLng(latitude, longitude), 16);
                    CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(selectedMarkerPosition).animate(CameraAnimation.Easing);
                    naverMap.moveCamera(cameraUpdate);

                    title.setText("편의점 명");
                    storeName.setText(strName);
                    address.setText("주소");
                    storeAddr.setText(strAddr);
                    ex1.setText("영업 유무");
                    storeEtc.setText(strStatus);
                    ex2.setText("");
                    storeTime.setText("");

                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    return true;
                });
                markerList.add(marker); // 리스트에 마커 저장
            } while (cursorEmergency.moveToNext());
        }

        cursorSales.close();
        salesdbHelper.close();
        cursorEmergency.close();
        emergencyDrugDBOpenHelper.close();
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
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
    public void onDestroy() {
        Log.i("on", "onDestroyView");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        Log.i("on", "onLowMemory");
        super.onLowMemory();
        mapView.onLowMemory();
    }

}