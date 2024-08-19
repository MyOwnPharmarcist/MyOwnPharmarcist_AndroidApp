package teamyj.dev.hrd_final_project.layout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import teamyj.dev.hrd_final_project.R;

public class SearchDrugsFragment extends Fragment {

    View view;
    private ImageView selectedImageView;  // 현재 선택된 ImageView

    // shape의 id 배열
    int[] shapeIds = {
            R.id.shape0, R.id.shape1, R.id.shape2, R.id.shape3, R.id.shape4,
            R.id.shape5, R.id.shape6, R.id.shape7, R.id.shape8, R.id.shape9,
            R.id.shape10
    };

    // color의 id 배열
    int[] colorIds = {
            R.id.colorall, R.id.colorwhite, R.id.coloryellow, R.id.colorornage, R.id.colorpink,
            R.id.colorred, R.id.colorbrown, R.id.colorlightgreen, R.id.colorgreen, R.id.colorturquoise,
            R.id.colorblue, R.id.colorsodomy, R.id.coloramethyst, R.id.colorpurple, R.id.colorgray,
            R.id.colorblack, R.id.colornull
    };

    // formulation의 id 배열
    int[] formulationIds = {
            R.id.formulation5, R.id.formulation1, R.id.formulation2, R.id.formulation3, R.id.formulation4
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_drugs, container, false);

        // 각 스크롤 뷰 내의 이미지들에 클릭 리스너 설정
        setupClickListeners((LinearLayout) ((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.shapesearch)).getChildAt(0)));
        setupClickListeners((LinearLayout) ((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.colorsearch)).getChildAt(0)));
        setupClickListeners((LinearLayout) ((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.formulationsearch)).getChildAt(0)));

        return view;
    }

    // 모든 ImageView에 클릭 리스너를 설정하는 메서드
    private void setupClickListeners(LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ImageView) {
                child.setOnClickListener(this::onImageClick);
            }
        }
    }

    // ImageView 클릭 시 호출되는 메서드
    public void onImageClick(View view) {
        int id = view.getId();

        // 기존에 선택된 ImageView의 테두리를 제거
        if (selectedImageView != null) {
            selectedImageView.setBackgroundColor(Color.TRANSPARENT);
        }

        // 선택된 ImageView에 테두리 추가
        view.setBackgroundColor(Color.BLUE);
        selectedImageView = (ImageView) view;  // 현재 선택된 ImageView를 저장

        // 각 그룹의 ID 배열에 해당하는지 확인
        if (isInArray(shapeIds, id)) {
            Toast.makeText(getContext(), "Shape 선택됨: " + id, Toast.LENGTH_SHORT).show();
        } else if (isInArray(colorIds, id)) {
            Toast.makeText(getContext(), "Color 선택됨: " + id, Toast.LENGTH_SHORT).show();
        } else if (isInArray(formulationIds, id)) {
            Toast.makeText(getContext(), "Formulation 선택됨: " + id, Toast.LENGTH_SHORT).show();
        }
    }

    // ID가 주어진 배열에 포함되어 있는지 확인하는 메서드
    private boolean isInArray(int[] ids, int id) {
        for (int i : ids) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }


}