package teamyj.dev.hrd_final_project.layout;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper;


public class DrugInfoFragment extends Fragment {

    private View view;
    private DrugProductsDBOpenHelper drugProductsDBOpenHelper;

    private TextView itemNameTextView;
    private TextView entpNameTextView;
    private TextView etcOtcCodeTextView;
    private TextView chartTextView;
    private TextView storageMethodTextView;
    private TextView validTermTextView;
    private TextView indutyTypeTextView;
    private TextView eeDocDataTextView;
    private TextView udDocDataTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_drug_info, container, false);

        // TextView 초기화
        itemNameTextView = view.findViewById(R.id.item_name_text_view);
        entpNameTextView = view.findViewById(R.id.entp_name_text_view);
        etcOtcCodeTextView = view.findViewById(R.id.etc_otc_code_text_view);
        chartTextView = view.findViewById(R.id.chart_text_view);
        storageMethodTextView = view.findViewById(R.id.storage_method_text_view);
        validTermTextView = view.findViewById(R.id.valid_term_text_view);
        indutyTypeTextView = view.findViewById(R.id.induty_type_text_view);
        eeDocDataTextView = view.findViewById(R.id.ee_doc_data_text_view);
        udDocDataTextView = view.findViewById(R.id.ud_doc_data_text_view);

        // Drug info 데이터 추가
        addDrugInfo();



        return view;
    }


    private void addDrugInfo() {
        Cursor cursor = new DrugProductsDBOpenHelper(getContext()).getSelectionDrugInfo();;

        if (cursor.moveToFirst()) {
            int itemNameIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.ITEM_NAME);
            int entpNameIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.ENTP_NAME);
            int etcOtcCodeIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.ETC_OTC_CODE);
            int chartIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.CHART);
            int storageMethodIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.STORAGE_METHOD);
            int validTermIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.VALID_TERM);
            int indutyTypeIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.INDUTY_TYPE);
            int eeDocDataIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.EE_DOC_DATA);
            int udDocDataIndex = cursor.getColumnIndex(DrugProductsDBOpenHelper.UD_DOC_DATA);

            // 각 인덱스가 -1이 아닌지 확인 후 데이터 가져오기
            String itemName = itemNameIndex != -1 ? cursor.getString(itemNameIndex) : "N/A";
            String entpName = entpNameIndex != -1 ? cursor.getString(entpNameIndex) : "N/A";
            String etcOtcCode = etcOtcCodeIndex != -1 ? cursor.getString(etcOtcCodeIndex) : "N/A";
            String chart = chartIndex != -1 ? cursor.getString(chartIndex) : "N/A";
            String storageMethod = storageMethodIndex != -1 ? cursor.getString(storageMethodIndex) : "N/A";
            String validTerm = validTermIndex != -1 ? cursor.getString(validTermIndex) : "N/A";
            String indutyType = indutyTypeIndex != -1 ? cursor.getString(indutyTypeIndex) : "N/A";
            String eeDocData = eeDocDataIndex != -1 ? cursor.getString(eeDocDataIndex) : "N/A";
            String udDocData = udDocDataIndex != -1 ? cursor.getString(udDocDataIndex) : "N/A";

            // TextView에 데이터 설정
            itemNameTextView.setText(itemName);
            entpNameTextView.setText(entpName);
            etcOtcCodeTextView.setText(etcOtcCode);
            chartTextView.setText(chart);
            storageMethodTextView.setText(storageMethod);
            validTermTextView.setText(validTerm);
            indutyTypeTextView.setText(indutyType);
            eeDocDataTextView.setText(eeDocData);
            udDocDataTextView.setText(udDocData);
        }
        cursor.close();
    }
}