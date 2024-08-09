package teamyj.dev.hrd_final_project.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.naver.maps.map.MapFragment;

import teamyj.dev.hrd_final_project.R;

public class SearchPharmacyFragment extends Fragment {

    private View view;
    private MapFragment mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_pharmacy, container, false);

        return view;
    }


}