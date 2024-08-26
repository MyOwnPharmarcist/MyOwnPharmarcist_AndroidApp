package teamyj.dev.hrd_final_project.layout;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import teamyj.dev.hrd_final_project.Interface.DBHelperGettable;
import teamyj.dev.hrd_final_project.Interface.ListDataGettable;
import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.main_system.CustomApplication;

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

    ListDataGettable listHelper;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_drugs, container, false);

        DBHelperGettable application = CustomApplication.getInstance();
        listHelper = application.getList();
        // 각 스크롤 뷰 내의 이미지들에 클릭 리스너 설정
        setupClickListeners((LinearLayout) ((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.shapesearch)).getChildAt(0)));
        setupClickListeners((LinearLayout) ((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.colorsearch)).getChildAt(0)));
        setupClickListeners((LinearLayout) ((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.formulationsearch)).getChildAt(0)));


        Button searchButton = view.findViewById(R.id.searchbtn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DrugInfoFragment로 전환
                openDrugInfoFragment();
            }
        });

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

//    // DB에서 데이터를 가져오는 함수 (예시)
//    private List<Druglist> fetchDataFromDB() {
//        List<Druglist> list = new ArrayList<>();
//
//        // 예시 데이터 추가 (실제 DB 연동 필요)
//        list.add(new Druglist("Drug 1", "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/1Muwq7fAuBq", "Red", "Round", "Tablet"));
//        list.add(new Druglist("Drug 2", "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/151318001317200082", "Blue", "Oval", "Capsule"));
//
//        return list;
//    }

    private void openDrugInfoFragment() {
        // FragmentTransaction을 통해 DrugInfoFragment로 전환
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_frame_layout, new DrugInfoFragment()); // 올바른 FrameLayout ID 사용
        transaction.addToBackStack(null);  // 뒤로가기 시 이전 Fragment로 돌아가기 위해 추가
        transaction.commit();

        listHelper.searchDrugList(makeSQLCondition());
    }

    private String makeSQLCondition() {
        String shape;
        String colot;
        String codeName;
                                                                                     
        return null;
    }

    public void addList(Cursor cursor) {

    }
}