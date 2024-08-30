package teamyj.dev.hrd_final_project.layout;

import static teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper.ITEM_NAME;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.data_system.DrugListDBOpenHelper;
import teamyj.dev.hrd_final_project.data_system.DrugProductsDBOpenHelper;


public class DrugInfoFragment extends Fragment {

    private View view;
    private DrugListDBOpenHelper drugListDBOpenHelper;
    private DrugProductsDBOpenHelper drugProductsDBOpenHelper;

    private ImageView drug_image;
    private TextView info_drug_item_name;
    private TextView info_drug_entp_name;
    private TextView info_drug_etcotc;
    private TextView info_drug_chart;
    private TextView info_drug_storage;
    private TextView info_drug_valid;
    private TextView info_drug_induty;
    private TextView info_drug_ee_doc;
    private TextView info_drug_ud_doc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_drug_info, container, false);

        drug_image = view.findViewById(R.id.drug_image);
        info_drug_item_name = view.findViewById(R.id.info_drug_item_name);
        info_drug_entp_name = view.findViewById(R.id.info_drug_entp_name);
        info_drug_etcotc = view.findViewById(R.id.info_drug_etcotc);
        info_drug_chart = view.findViewById(R.id.info_drug_chart);
        info_drug_storage = view.findViewById(R.id.info_drug_storage);
        info_drug_valid = view.findViewById(R.id.info_drug_valid);
        info_drug_induty = view.findViewById(R.id.info_drug_induty);
        info_drug_ee_doc = view.findViewById(R.id.info_drug_ee_doc);
        info_drug_ud_doc = view.findViewById(R.id.info_drug_ud_doc);

        Bundle bundle = getArguments();
        Log.i("bundle", bundle.getString(ITEM_NAME));
        addDrugInfo(bundle.getString(ITEM_NAME));

        return view;
    }


    private void addDrugInfo(String inputItemName) {
        Cursor cursor = new DrugProductsDBOpenHelper(getContext()).getSelectionDrugInfo(inputItemName);
        Cursor cursor_image = new DrugListDBOpenHelper(getContext()).getSelectionDrugImage(inputItemName);

        if(cursor_image.moveToFirst()) {
            int imageIndex = cursor_image.getColumnIndex(DrugListDBOpenHelper.DRUG_LIST_ELEMENTS[2]);
            String imageUrl = imageIndex != -1 ? cursor_image.getString(imageIndex) : "N/A";
            Glide.with(this)
                    .load(imageUrl)
                    .into(drug_image);
        }

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
            info_drug_item_name.setText(itemName);
            info_drug_entp_name.setText(entpName);
            info_drug_etcotc.setText(etcOtcCode);
            info_drug_chart.setText(chart);
            info_drug_storage.setText(storageMethod);
            info_drug_valid.setText(validTerm);
            info_drug_induty.setText(indutyType);
            info_drug_ee_doc.setText(eeDocData);
            info_drug_ud_doc.setText(udDocData);
        }
        cursor_image.close();
        cursor.close();
    }
}