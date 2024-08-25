package teamyj.dev.hrd_final_project.layout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import teamyj.dev.hrd_final_project.R;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("on", "onCreateView_BottomSheet");
        view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        return view;
    }

//    public BottomSheetFragment(final BottomSheetBehavior behavior, final Fragment fragment) {
//
//        // 리스너 설정
//        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        break;
//                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        break;
//                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
//    }

}