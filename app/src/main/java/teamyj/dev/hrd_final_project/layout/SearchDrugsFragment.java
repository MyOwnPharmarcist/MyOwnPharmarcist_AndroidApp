package teamyj.dev.hrd_final_project.layout;

import static teamyj.dev.hrd_final_project.data_system.DrugListDBOpenHelper.DRUG_LIST_ELEMENTS;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import teamyj.dev.hrd_final_project.Interface.DBHelperGettable;
import teamyj.dev.hrd_final_project.Interface.ListDataGettable;
import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.main_system.CustomApplication;

public class SearchDrugsFragment extends Fragment {
    private StringBuilder stringBuilder = new StringBuilder();

    private View view;
    private EditText editText;
    private ImageView selectedShape;  // 현재 선택된 ImageView
    private ImageView selectedColor;
    private ImageView selectedFormulation;

    private ListDataGettable listHelper;
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> druglistAdapter;

    private String shape = "";
    private String color = "";
    private String codeName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_drugs, container, false);

        DBHelperGettable application = CustomApplication.getInstance();
        listHelper = application.getList();

        editText = view.findViewById(R.id.editSearch);

        selectedShape = view.findViewById(R.id.shape0_image);
        selectedShape.setBackgroundColor(Color.BLUE);
        selectedColor = view.findViewById(R.id.colorall_image);
        selectedColor.setBackgroundColor(Color.BLUE);
        selectedFormulation = view.findViewById(R.id.formulation5_image);
        selectedFormulation.setBackgroundColor(Color.BLUE);

        // 각 스크롤 뷰 내의 이미지들에 클릭 리스너 설정
        setupClickListeners((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.shapesearch)).getChildAt(0));
        setupClickListeners((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.colorsearch)).getChildAt(0));
        setupClickListeners((LinearLayout) ((HorizontalScrollView) view.findViewById(R.id.formulationsearch)).getChildAt(0));

        Button searchButton = view.findViewById(R.id.searchbtn);
        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        druglistAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(druglistAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DrugInfoFragment로 전환
                sendSQLCondition();
//                openDrugInfoFragment();
            }
        });

        return view;
    }

    // 모든 ImageView에 클릭 리스너를 설정하는 메서드
    private void setupClickListeners(LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof FrameLayout) {
                child.setOnClickListener(this::onImageClick);
            }
        }
    }

    // ImageView 클릭 시 호출되는 메서드
    public void onImageClick(View view) {
        ImageView image = (ImageView) ((FrameLayout)view).getChildAt(0);
        image.setBackgroundColor(Color.BLUE);

        TextView text = (TextView) ((FrameLayout)view).getChildAt(1);

        if(view.getParent().equals(selectedShape.getParent().getParent())) {
            if(!selectedShape.equals(image)) {
                selectedShape.setBackgroundColor(Color.TRANSPARENT);
            }
            selectedShape = image;
            shape = text.getText().toString();
        } else if(view.getParent().equals(selectedColor.getParent().getParent())) {
            if(!selectedColor.equals(image)) {
                selectedColor.setBackgroundColor(Color.TRANSPARENT);
            }
            selectedColor = image;
            color = text.getText().toString();
        } else {
            if(!selectedFormulation.equals(image)) {
                selectedFormulation.setBackgroundColor(Color.TRANSPARENT);
            }
            selectedFormulation = image;
            codeName = text.getText().toString();
        }
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
    }

    private void sendSQLCondition() {
        String result;
        stringBuilder.setLength(0);
        if((result = editText.getText().toString()).isEmpty()) {
            stringBuilder.append("0 = 0");
            if(!shape.isEmpty()) {
                stringBuilder.append(" and ").append(DRUG_LIST_ELEMENTS[3]).append(" = '")
                        .append(shape).append("'");
            }
            if(!color.isEmpty()) {
                stringBuilder.append(" and ").append(DRUG_LIST_ELEMENTS[4]).append(" = '")
                        .append(color).append("'");
            }
            if(!codeName.isEmpty()) {
                stringBuilder.append(" and ").append(DRUG_LIST_ELEMENTS[7]).append(" = '")
                        .append(codeName).append("'");
            }
        } else {
            stringBuilder.append(DRUG_LIST_ELEMENTS[0]).append(" LIKE '%").append(result)
                    .append("%'");
        }
        addList(listHelper.searchDrugList(stringBuilder.toString()));
    }

    public void addList(Cursor cursor) {
        arrayList.clear();
        if(cursor.moveToFirst()) {
            arrayList.add(cursor.getString(0));
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(0));
                for (int i = 0; i < 50; i++) {
                    if(cursor.moveToNext()) {
                        arrayList.add(cursor.getString(0));
                    }
                }
                druglistAdapter.notifyDataSetChanged();
            }
        }
        druglistAdapter.notifyDataSetChanged();
    }
}