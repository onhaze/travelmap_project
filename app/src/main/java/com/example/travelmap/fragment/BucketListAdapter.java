// BucketListAdapter.java
package com.example.travelmap.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelmap.R;
import com.example.travelmap.ReviewActivity;
import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    public interface OnItemRemovedListener {
        void onItemRemoved(String regionName);
    }

    private final Context context;
    private final List<String> regionList;
    private final OnItemRemovedListener listener;

    public BucketListAdapter(Context context, List<String> regionList, OnItemRemovedListener listener) {
        this.context = context;
        this.regionList = regionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bucketlist_region, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String regionName = regionList.get(position);
        holder.checkBox.setText(regionName);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 체크 시 리뷰 등록 창 띄우기
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra("region_code", ""); // region_code 없음 → 시 이름만 전달
                intent.putExtra("region_name", regionName);
                context.startActivity(intent);

                // 리스트에서 삭제 (체크 시 삭제)
                regionList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                listener.onItemRemoved(regionName);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            // X 버튼 클릭 시 삭제
            regionList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            listener.onItemRemoved(regionName);
        });
    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_region);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}
