package teamyj.dev.hrd_final_project.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import teamyj.dev.hrd_final_project.R;

public class BottomSheetFragment extends Fragment {

    private View view;
    private TextView storeName;
    private TextView storeCallNumber;
    private TextView storeAddr;
    private TextView storeTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        storeName = view.findViewById(R.id.bottom_sheet_store_name);
        storeCallNumber = view.findViewById(R.id.bottom_sheet_store_call_number);
        storeAddr = view.findViewById(R.id.bottom_sheet_store_address);
        storeTime = view.findViewById(R.id.bottom_sheet_store_time);

        getParentFragmentManager().setFragmentResultListener("storeKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // 번들 키 값 입력
                String strStoreName = bundle.getString("store_name");
                String strStoreCallNumber = bundle.getString("store_call_number");
                String strStoreAddr = bundle.getString("store_address");
                String strStoreTime = bundle.getString("store_time");
                // 전달 받은 result 이용하여 코딩
                storeName.setText(strStoreName);
                storeCallNumber.setText(strStoreCallNumber);
                storeAddr.setText(strStoreAddr);
                storeTime.setText(strStoreTime);
            }
        });

        return view;
    }

    public BottomSheetFragment(final BottomSheetBehavior behavior, final Fragment fragment) {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // 리스너 설정
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
}