package com.robolancers.dragonclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.room.entities.DragonMajor;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DragonMajorAdapter extends RecyclerView.Adapter<DragonMajorAdapter.DragonMajorViewHolder>{
    private final LayoutInflater inflater;
    private List<DragonMajor> classes;
    private Context context;

    public DragonMajorAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public DragonMajorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DragonMajorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DragonMajorViewHolder holder, int position) {
        if (classes != null) {
            holder.bind(classes.get(position));
        } else {
            holder.dragonMajorItemView.setText("N/A");
        }
    }

    public void setDragonMajors(List<DragonMajor> dragonMajors) {
        this.classes = dragonMajors;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return classes != null ? classes.size() : 0;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class DragonMajorViewHolder extends RecyclerView.ViewHolder {
        private final TextView dragonMajorItemView;

        private DragonMajorViewHolder(View itemView) {
            super(itemView);
            dragonMajorItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(DragonMajor dragonMajor) {
            dragonMajorItemView.setText(dragonMajor.getMajorName());

            itemView.setOnClickListener(view -> {
                // Send user to main activity
            });
        }
    }
}
