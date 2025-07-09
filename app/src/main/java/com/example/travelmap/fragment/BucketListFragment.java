// BucketListFragment.java
package com.example.travelmap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelmap.R;
import com.example.travelmap.model.GeoRegion;
import com.example.travelmap.util.GeoUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BucketListFragment extends Fragment {

    private List<String> regionNames; // 후보 리스트 (시 이름)
    private List<String> bucketList;  // 현재 추가된 리스트
    private BucketListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bucketlist, container, false);

        // 1️⃣ 시 이름 후보 리스트 준비 (GeoRegion 에서 시 이름 추출)
        List<GeoRegion> geoRegions = GeoUtils.loadGeoRegions(requireContext());
        Set<String> nameSet = new HashSet<>();
        for (GeoRegion region : geoRegions) {
            nameSet.add(region.getName()); // 시 이름 중복 제거
        }
        regionNames = new ArrayList<>(nameSet);

        // 2️⃣ 초기 bucketList 리스트 준비
        bucketList = new ArrayList<>();

        // 3️⃣ AutoCompleteTextView 설정
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.auto_complete_region);
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                regionNames
        );
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        // 4️⃣ RecyclerView 설정
        RecyclerView recyclerView = view.findViewById(R.id.bucketlist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BucketListAdapter(requireContext(), bucketList, regionName -> {
            // 아이템 삭제 후 추가 동작 필요시 여기에 작성 가능 (지금은 없음)
        });
        recyclerView.setAdapter(adapter);

        // 5️⃣ 추가 버튼 클릭 시 동작
        Button addButton = view.findViewById(R.id.btn_add_region);
        addButton.setOnClickListener(v -> {
            String selectedRegion = autoCompleteTextView.getText().toString().trim();
            if (!selectedRegion.isEmpty() && regionNames.contains(selectedRegion) && !bucketList.contains(selectedRegion)) {
                bucketList.add(selectedRegion);
                adapter.notifyItemInserted(bucketList.size() - 1);
                autoCompleteTextView.setText(""); // 입력창 초기화
            }
        });

        return view;
    }
}
