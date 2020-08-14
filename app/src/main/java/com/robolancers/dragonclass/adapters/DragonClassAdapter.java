package com.robolancers.dragonclass.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.room.entities.DragonClass;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DragonClassAdapter extends RecyclerView.Adapter<DragonClassAdapter.DragonClassViewHolder>{
    private final LayoutInflater inflater;
    private List<DragonClass> classes;

    DragonClassAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DragonClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DragonClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DragonClassViewHolder holder, int position) {
        if (classes != null) {
            holder.bind(classes.get(position));
        } else {
            holder.dragonClassItemView.setText("N/A");
        }
    }

    void setClasses(List<DragonClass> dragonClasses) {
        this.classes = dragonClasses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return classes != null ? classes.size() : 0;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class DragonClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView dragonClassItemView;

        private DragonClassViewHolder(View itemView) {
            super(itemView);
            dragonClassItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(DragonClass dragonClass) {
            dragonClassItemView.setText(dragonClass.getCourseID());
        }
    }
}
